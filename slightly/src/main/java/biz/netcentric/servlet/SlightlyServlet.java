package biz.netcentric.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(description = "Slightly Servlet", loadOnStartup = 1, name = "slightlyServlet", urlPatterns = { "*.html" })
public class SlightlyServlet extends HttpServlet {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -5551085985947954282L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SlightlyServlet.class);

	@Override
	public void init() throws ServletException {
		super.init();
		LOGGER.info("Entered on init() method");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LOGGER.info("Entered on doGet() method");
		PrintWriter writer = null;

		try {
			writer = response.getWriter();

			writer.println("<!DOCTYPE html><html><title>Slightly</title></html>");
		} catch (IOException e) {
			LOGGER.error("An error occurred while writting servlet response", e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);
		LOGGER.info("Entered on doPost() method");
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.service(request, response);
		LOGGER.info("Entered on service() method");
	}
}
