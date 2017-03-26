package biz.netcentric;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String responseContent = StringUtils.EMPTY;

		if ((request.getParameterMap() != null) && (!request.getParameterMap().isEmpty())) {
			SlightlyParser parser = new SlightlyParser();

			responseContent = parser.parse(request);	
		}

		try (PrintWriter writer = response.getWriter()) {
			writer.println(responseContent);
		} catch (IOException ioe) {
			LOGGER.error("An error occurred while writing servlet response", ioe);
		}
	}
}
