package biz.netcentric.servlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;

@WebServlet(description = "Slightly Servlet", loadOnStartup = 1, name = "slightlyServlet", urlPatterns = { "*.html" })
public class SlightlyServlet extends HttpServlet {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -5551085985947954282L;

	@Inject
	private Logger logger;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		logger.info("Entered on init() method");
	}
}
