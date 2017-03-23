package biz.netcentric;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlightlyParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlightlyParser.class);

	private static Document document;
	private static Pattern dollarExpressionPattern;

	private HttpServletRequest request;
	private Map<String, Object> instanceMap;
	private List<Node> nodesToRemove;

	/**
	 * Enumeration for the possible types of Java methods.s
	 */
	protected enum MethodType {
		GET, SET, IS;
	}

	static {
		dollarExpressionPattern = Pattern.compile(SlightlyParserUtil.DOLLAR_EXPRESSION_PATTERN);
	}

	/**
	 * Constructor of this class.
	 *
	 * @param request
	 *            the HTTP servlet request
	 * @throws IOException
	 *             if an error occurs while parsing the HTML document
	 */
	public SlightlyParser(final HttpServletRequest request) throws IOException {
		setRequest(request);

		setInstanceMap(new TreeMap<>());

		setNodesToRemove(new ArrayList<>());

		if (document == null) {
			String path = request.getServletContext().getRealPath("index.html");

			document = Jsoup.parse(new File(path), SlightlyParserUtil.ENCODING);
		}
	}

	public String parse() {

		if (request.getParameterMap().isEmpty()) {
			return StringUtils.EMPTY;
		}

		// builds map of Java instances from the script element
		buildInstanceMap();

		// removes the script element
		Document newDocument = document.clone();
		newDocument.getElementsByTag(SlightlyParserUtil.SCRIPT).remove();

		// traverses the new document without the script element
		newDocument.traverse(new NodeVisitor() {

			@Override
			public void tail(Node node, int depth) {
				// Do nothing
				LOGGER.info("Exiting tag: " + node.nodeName());
			}

			@Override
			public void head(Node node, int depth) {
				LOGGER.info("Entering tag: " + node.nodeName());
				LOGGER.warn(node.nodeName() + ": " + node.toString());
				LOGGER.error(node.nodeName() + " depth: " + depth);

				processNode(node);
			}
		});

		// removes nodes previously marked to be removed
		nodesToRemove.forEach(node -> node.remove());

		return newDocument.toString();
	}

	/**
	 * Builds a map containing all Java instances created during the evaluation
	 * of the script element.
	 *
	 * @param request
	 *            the HTTP servlet request
	 */
	private void buildInstanceMap() {
		Elements script = document.getElementsByTag(SlightlyParserUtil.SCRIPT);

		if (script.hasAttr(SlightlyParserUtil.SCRIPT_TYPE)
				&& (script.attr(SlightlyParserUtil.SCRIPT_TYPE).equals(SlightlyParserUtil.SCRIPT_TYPE_VAL))) {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName(SlightlyParserUtil.JS_ENGINE);

			try {
				engine.eval(SlightlyParserUtil.ENABLE_RHINO);
				engine.put(SlightlyParserUtil.HTTP_REQUEST, this.request);
				engine.eval(script.first().html());

				engine.getBindings(ScriptContext.ENGINE_SCOPE).keySet().forEach(key -> {
					Class<?> clazz = engine.get(key).getClass();

					if (classExists(clazz)) {
						this.instanceMap.put(key, engine.get(key));
					}
				});
			} catch (ScriptException se) {
				LOGGER.error("An error occurred while processing Javascript code", se);
			}
		}
	}

	/**
	 * Validates if the class passed as argument exists in the current package
	 * scope.
	 *
	 * @param clazz
	 *            the class to validate
	 * @return true if the class exists; false otherwise
	 */
	private boolean classExists(final Class<?> clazz) {
		boolean result = false;

		try {
			Class.forName(SlightlyParserUtil.SLIGHTLY_PACKAGE + clazz.getSimpleName());
			result = true;
		} catch (ClassNotFoundException cnfe) {
			LOGGER.info(clazz.getName() + " isn't part of " + SlightlyParserUtil.SLIGHTLY_PACKAGE + " package");
		}

		return result;
	}

	private void processNode(final Node node) {
		List<String> attrsToRemove = new ArrayList<>();

		// checks each attribute
		node.attributes().forEach(attribute -> {
			// process data-for-x
			Collection<? extends Object> collection = processDataForX(attribute);

			if (collection != null) {
				String variableName = attribute.getKey().replaceFirst(SlightlyParserUtil.DATA_FOR_X, StringUtils.EMPTY);

				collection.forEach(object -> {
					Node newNode = node.clone();
					newNode.removeAttr(attribute.getKey());
					String html = newNode.toString().replace(
							String.format(SlightlyParserUtil.DATA_FOR_X_PATTERN, variableName), object.toString());
					node.before(html);
				});

				nodesToRemove.add(node);
			}

			// process data-if
			Boolean result = processDataIf(attribute);

			if (result != null) {
				if (result) {
					attrsToRemove.add(attribute.getKey());
				} else {
					nodesToRemove.add(node);
				}
			}

			// process $-expression
			processDollarExpressions(attribute);
		});

		// cannot remove attribute during check: ConcurrentModificationException
		// occurs
		attrsToRemove.forEach(attributeKey -> node.removeAttr(attributeKey));
	}

	private Collection<?> processDataForX(final Attribute attribute) {
		Collection<?> collection = null;

		if (attribute != null && attribute.getKey().startsWith(SlightlyParserUtil.DATA_FOR_X)) {
			String[] javaElems = attribute.getValue().split("\\.");

			if (javaElems.length >= 2 && instanceMap.containsKey(javaElems[0])) {
				String javaElem = javaElems[0];
				String javaAttr = javaElems[1];

				if (instanceMap.containsKey(javaElem)) {
					collection = (Collection<?>) processMethodInvocation(javaElem, javaAttr, MethodType.GET);
				}
			}
		}

		return collection;
	}

	private Boolean processDataIf(final Attribute attribute) {
		Boolean result = null;

		if ((attribute != null) && attribute.getKey().matches(SlightlyParserUtil.DATA_IF)) {
			String[] javaElems = attribute.getValue().split("\\.");

			if (javaElems.length >= 2) {
				String javaElem = javaElems[0];
				String javaAttr = javaElems[1];

				if (instanceMap.containsKey(javaElem)) {
					result = (boolean) processMethodInvocation(javaElem, javaAttr, MethodType.IS);
				}
			}
		}

		return result;
	}

	private void processDollarExpressions(final Attribute attribute) {
		if (attribute != null) {
			String attrValue = attribute.getValue();
			Matcher matcher = dollarExpressionPattern.matcher(attrValue);

			if (matcher.find()) {
				String expression = matcher.group();
				String[] javaElems = matcher.group(1).split("\\.");

				if (javaElems.length >= 2 && instanceMap.containsKey(javaElems[0])) {
					String javaElem = javaElems[0];
					String javaAttr = javaElems[1];

					String result = String.valueOf(processMethodInvocation(javaElem, javaAttr, MethodType.GET));
					attribute.setValue(attrValue.replace(expression, StringEscapeUtils.escapeHtml4(result)));
				}
			}
		}
	}

	private Object processMethodInvocation(final String instanceName, final String instanceAttribute,
			final MethodType type) {
		Object result = null;
		Object instance = instanceMap.get(instanceName);

		try {
			Method method = instance.getClass()
					.getMethod(type.name().toLowerCase() + WordUtils.capitalize(instanceAttribute), new Class[] {});
			result = method.invoke(instance, (Object[]) null);
		} catch (NoSuchMethodException nsme) {
			LOGGER.error("An error occurred while trying to attain a method of " + instance.getClass().getName(), nsme);
		} catch (ReflectiveOperationException roe) {
			LOGGER.error("An error occurred while trying to invoke a method on " + instance.getClass().getName(), roe);
		}

		return result;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the instanceMap
	 */
	public Map<String, Object> getInstanceMap() {
		return instanceMap;
	}

	/**
	 * @param instanceMap
	 *            the instanceMap to set
	 */
	public void setInstanceMap(Map<String, Object> instanceMap) {
		this.instanceMap = instanceMap;
	}

	/**
	 * @return the nodesToRemove
	 */
	public List<Node> getNodesToRemove() {
		return nodesToRemove;
	}

	/**
	 * @param nodesToRemove
	 *            the nodesToRemove to set
	 */
	public void setNodesToRemove(List<Node> nodesToRemove) {
		this.nodesToRemove = nodesToRemove;
	}
}
