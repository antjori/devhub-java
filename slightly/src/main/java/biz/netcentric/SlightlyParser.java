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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlightlyParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlightlyParser.class);

	private static Document document;

	private Map<String, Class<?>> classMap;

	public SlightlyParser(ServletContext context) throws IOException {
		if (document == null) {
			String path = context.getRealPath("index.html");

			document = Jsoup.parse(new File(path), SlightlyParserUtil.ENCODING);
		}

		setClassMap(new TreeMap<>());
	}

	public void parse(final HttpServletRequest request) {
		Elements script = document.getElementsByTag("script");

		if (script.hasAttr(SlightlyParserUtil.SCRIPT_TYPE)
				&& (script.attr(SlightlyParserUtil.SCRIPT_TYPE).equals(SlightlyParserUtil.SCRIPT_TYPE_VAL))) {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName(SlightlyParserUtil.JS_ENGINE);

			/*
			 * script.first().children().forEach(element -> { try { Object
			 * result = engine.eval(element.toString());
			 * LOGGER.info(result.toString());
			 * LOGGER.info(result.getClass().getName()); } catch
			 * (ScriptException se) { LOGGER.error(
			 * "An error occurred while processing Javascript code", se); } });
			 */

			try {
				engine.eval(SlightlyParserUtil.ENABLE_RHINO);
				engine.put("request", request);
				engine.eval(script.first().html());

				engine.getBindings(ScriptContext.ENGINE_SCOPE).keySet().forEach(key -> {
					Class<?> clazz = engine.get(key).getClass();
					if (classExists(clazz)) {
						classMap.put(key, clazz);
					}
				});
			} catch (ScriptException se) {
				LOGGER.error("An error occurred while processing Javascript code", se);
			}
		}

		classMap.keySet().forEach(key -> LOGGER.info(key));

		Elements head = document.getElementsByTag("head");
		// head.stream().forEach(element -> LOGGER.info(element.toString()));

		Elements body = document.getElementsByTag("body");
		// body.stream().forEach(element -> LOGGER.info(element.toString()));
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
	 * @return the classMap
	 */
	public Map<String, Class<?>> getClassMap() {
		return classMap;
	}

	/**
	 * @param classMap
	 *            the classMap to set
	 */
	public void setClassMap(Map<String, Class<?>> classMap) {
		this.classMap = classMap;
	}
}
