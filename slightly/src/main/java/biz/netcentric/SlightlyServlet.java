package biz.netcentric;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Slightly servlet.
 *
 * A Java servlet that responds to requests for HTML files. The servlet reads
 * the requested file, parses the HTML, and processes the server-side javascript
 * and server-side data-attributes, and finally sends the resulting HTML to the
 * browser.
 */
@WebServlet(description = "Slightly Servlet", loadOnStartup = 1, name = "slightlyServlet", urlPatterns = { "*.html" })
public class SlightlyServlet extends HttpServlet {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -5551085985947954282L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SlightlyServlet.class);

	@Override
	public void init() throws ServletException {
		LOGGER.info("Entered on init() method");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LOGGER.info("Entered on doGet() method");

		SlightlyParser parser = new SlightlyParser(request.getServletContext());
		parser.parse();

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
}
