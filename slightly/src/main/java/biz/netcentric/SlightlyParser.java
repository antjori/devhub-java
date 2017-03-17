package biz.netcentric;

import java.io.File;
import java.io.IOException;

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
		Elements scripting = document.getElementsByTag("script");
		Elements head = document.getElementsByTag("head");
		Elements body = document.getElementsByTag("body");

		body.stream().forEach(element -> LOGGER.info(element.toString()));
	}
}
