package biz.netcentric;

import java.io.File;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlightlyParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlightlyParser.class);

	private static Document document;

	public SlightlyParser(ServletContext context) throws IOException {
		if (document == null) {
			String path = context.getRealPath("index.html");

			document = Jsoup.parse(new File(path), "UTF-8");
		}
	}

	public void parse() {
		Elements script = document.getElementsByTag("script");

		if (script.hasAttr("type") && (script.attr("type").equals("server/javascript"))) {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

			script.first().children().forEach(element -> {
				try {
					engine.eval(element.toString());
					engine.get
				} catch (ScriptException se) {
					LOGGER.error("An error occurred while processing Javascript code", se);
				}

				Invocable invocable = (Invocable) engine;
				invocable.
			});

			LOGGER.warn(script.first().html());
		}

		Elements head = document.getElementsByTag("head");
//		head.stream().forEach(element -> LOGGER.info(element.toString()));

		Elements body = document.getElementsByTag("body");
//		body.stream().forEach(element -> LOGGER.info(element.toString()));
	}
}
