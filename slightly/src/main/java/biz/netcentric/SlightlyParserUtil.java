package biz.netcentric;

/**
 * Utility class for the Slightly parser.
 */
public final class SlightlyParserUtil {

	public static final String ENCODING = "UTF-8";

	// HTML constants
	public static final String SCRIPT = "script";
	public static final String SCRIPT_TYPE = "type";
	public static final String SCRIPT_TYPE_VAL = "server/javascript";

	// Nashorn engine constants
	public static final String JS_ENGINE = "nashorn";
	public static final String ENABLE_RHINO = "load(\"nashorn:mozilla_compat.js\");";

	// Slightly constants
	public static final String HTTP_REQUEST = "request";
	public static final String SLIGHTLY_PACKAGE = "biz.netcentric.";
	public static final String DATA_IF = "data-if";

	public static final String DOLLAR_EXPRESSION_PATTERN = "\\$\\{(.*?)\\}";

	/**
	 * Private constructor used to hide the implicit public one
	 */
	private SlightlyParserUtil() {
	}
}
