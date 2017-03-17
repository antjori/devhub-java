package biz.netcentric;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlightlyParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlightlyParser.class);

	private static Document DOCUMENT;

	static {
		File file = new File("index.html");

		try {
			DOCUMENT = Jsoup.parse(file, "UTF-8");
		} catch (IOException e) {
			LOGGER.error("An error occurred while parsing " + file.getName());
		}
	}

	public String parse() {
		DOCUMENT.getAllElements().stream().m
	}
}
