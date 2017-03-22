package biz.netcentric;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlightlyParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlightlyParser.class);

	private static Document document;

	private Map<String, Object> instanceMap;

	public SlightlyParser(ServletContext context) throws IOException {
		if (document == null) {
			String path = context.getRealPath("index.html");

			document = Jsoup.parse(new File(path), SlightlyParserUtil.ENCODING);
		}

		setInstanceMap(new TreeMap<>());
	}

	public String parse(final HttpServletRequest request) {

		if (request.getParameterMap().isEmpty()) {
			return StringUtils.EMPTY;
		}

		StringBuilder responseContent = new StringBuilder();

		// builds the map of Java instances from the script element
		buildInstanceMap(request);

		Document newDocument = document.clone();
		newDocument.getElementsByTag(SlightlyParserUtil.SCRIPT).remove();
		LOGGER.error(newDocument.html());

		for (Element element : newDocument.getElementsContainingOwnText("$")) {
			LOGGER.debug(element.toString());
			element.replaceWith();
		}

		Elements head = document.getElementsByTag("head");
		// head.stream().forEach(element -> LOGGER.info(element.toString()));

		Elements body = document.getElementsByTag("body");
		// body.stream().forEach(element -> LOGGER.info(element.toString()));

		return responseContent.toString();
	}

	/**
	 * Builds a map containing all Java instances created during the evaluation
	 * of the script element.
	 * 
	 * @param request
	 *            the HTTP servlet request
	 */
	private void buildInstanceMap(final HttpServletRequest request) {
		Elements script = document.getElementsByTag(SlightlyParserUtil.SCRIPT);

		if (script.hasAttr(SlightlyParserUtil.SCRIPT_TYPE)
				&& (script.attr(SlightlyParserUtil.SCRIPT_TYPE).equals(SlightlyParserUtil.SCRIPT_TYPE_VAL))) {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName(SlightlyParserUtil.JS_ENGINE);

			try {
				engine.eval(SlightlyParserUtil.ENABLE_RHINO);
				engine.put(SlightlyParserUtil.HTTP_REQUEST, request);
				engine.eval(script.first().html());

				engine.getBindings(ScriptContext.ENGINE_SCOPE).keySet().forEach(key -> {
					Class<?> clazz = engine.get(key).getClass();

					if (classExists(clazz)) {
						instanceMap.put(key, engine.get(key));
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
}
