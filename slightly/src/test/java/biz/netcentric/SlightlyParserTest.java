package biz.netcentric;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Person.class)
public class SlightlyParserTest {

	@Mock
	Logger logger;

	@Mock
	HttpServletRequest request;

	@Mock
	ServletContext context;

	@InjectMocks
	SlightlyParser slightlyParser;

	@BeforeClass
	public static void init() {
		// do nothing
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	// ===========
	// parse tests
	// ===========

	@Test
	public void parseNoTemplateTest() {
		// given
		BDDMockito.given(request.getServletContext()).willReturn(context);
		BDDMockito.given(context.getRealPath(Mockito.anyString())).willReturn("index.html");

		// when
		String content = slightlyParser.parse(request);

		// then
		Mockito.verify(request, Mockito.times(1)).getServletContext();
		Mockito.verify(context, Mockito.times(1)).getRealPath(Mockito.anyString());

		Assert.assertNotNull(content);
		Assert.assertEquals(StringUtils.EMPTY, content);
	}

	@Test
	public void parseTest() {
		Person person = new Person("Erik", "Dora", true, 3);

		// given
		BDDMockito.given(request.getServletContext()).willReturn(context);
		BDDMockito.given(context.getRealPath(Mockito.anyString())).willReturn("src/test/resources/template/index.html");
		BDDMockito.given(request.getParameter(Mockito.anyString())).willReturn("2");

		PowerMockito.mockStatic(Person.class);
		BDDMockito.given(Person.lookup(Mockito.anyString())).willReturn(person);

		// when
		String content = slightlyParser.parse(request);

		// then
		PowerMockito.verifyStatic(Mockito.times(1));

		Assert.assertNotNull(content);
		Assert.assertNotEquals(StringUtils.EMPTY, content);
		Assert.assertEquals(loadResponse(2).replaceAll(">\\s+<", "><"), content.replaceAll(">\\s+<", "><"));
	}

	@Test
	public void parseNoSpouseTest() {
		Person person = new Person("Kerstin", "Jose", false, 1);

		// given
		BDDMockito.given(request.getServletContext()).willReturn(context);
		BDDMockito.given(context.getRealPath(Mockito.anyString())).willReturn("src/test/resources/template/index.html");
		BDDMockito.given(request.getParameter(Mockito.anyString())).willReturn("1");

		PowerMockito.mockStatic(Person.class);
		BDDMockito.given(Person.lookup(Mockito.anyString())).willReturn(person);

		// when
		String content = slightlyParser.parse(request);

		// then
		PowerMockito.verifyStatic(Mockito.times(1));

		Assert.assertNotNull(content);
		Assert.assertNotEquals(StringUtils.EMPTY, content);
		Assert.assertEquals(loadResponse(1).replaceAll(">\\s+<", "><"), content.replaceAll(">\\s+<", "><"));
	}

	@Test
	public void parseNoChildrenTest() {
		Person person = new Person("Svajune", "Thomas", true, 0);

		// given
		BDDMockito.given(request.getServletContext()).willReturn(context);
		BDDMockito.given(context.getRealPath(Mockito.anyString())).willReturn("src/test/resources/template/index.html");
		BDDMockito.given(request.getParameter(Mockito.anyString())).willReturn("3");

		PowerMockito.mockStatic(Person.class);
		BDDMockito.given(Person.lookup(Mockito.anyString())).willReturn(person);

		// when
		String content = slightlyParser.parse(request);

		// then
		PowerMockito.verifyStatic(Mockito.times(1));

		Assert.assertNotNull(content);
		Assert.assertNotEquals(StringUtils.EMPTY, content);
		Assert.assertEquals(loadResponse(3).replaceAll(">\\s+<", "><"), content.replaceAll(">\\s+<", "><"));
	}

	@Test
	public void parseNoPersonTest() {
		Person person = new Person("Empty Name", "Empty spouse", false, 0);

		// given
		BDDMockito.given(request.getServletContext()).willReturn(context);
		BDDMockito.given(context.getRealPath(Mockito.anyString())).willReturn("src/test/resources/template/index.html");
		BDDMockito.given(request.getParameter(Mockito.anyString())).willReturn("4");

		PowerMockito.mockStatic(Person.class);
		BDDMockito.given(Person.lookup(Mockito.anyString())).willReturn(person);

		// when
		String content = slightlyParser.parse(request);

		// then
		PowerMockito.verifyStatic(Mockito.times(1));

		Assert.assertNotNull(content);
		Assert.assertNotEquals(StringUtils.EMPTY, content);
		Assert.assertEquals(loadResponse("response/noperson.html").replaceAll(">\\s+<", "><"),
				content.replaceAll(">\\s+<", "><"));
	}

	@Test
	@PrepareForTest({ SlightlyParser.class, Person.class })
	public void parseNoScriptTest() {
		Person person = new Person("Erik", "Dora", true, 3);

		// given
		BDDMockito.given(request.getServletContext()).willReturn(context);
		BDDMockito.given(context.getRealPath(Mockito.anyString()))
				.willReturn("src/test/resources/template/noscript.html");
		BDDMockito.given(request.getParameter(Mockito.anyString())).willReturn("3");

		PowerMockito.mockStatic(Person.class);
		BDDMockito.given(Person.lookup(Mockito.anyString())).willReturn(person);

		// when
		String content = slightlyParser.parse(request);

		// then
		PowerMockito.verifyStatic(Mockito.times(1));
		Assert.assertNotEquals(StringUtils.EMPTY, content);
		Assert.assertEquals(loadResponse("response/noscript.html").replaceAll(">\\s+<", "><"),
				content.replaceAll(">\\s+<", "><"));
	}

	/**
	 * Loads the file that represents an HTML response from Slightly.
	 *
	 * @return the string representation of the contents of the file that
	 *         represents an HTML response from Slightly
	 */
	private static String loadResponse(final int id) {
		return loadResponse("response/person" + id + ".html");
	}

	private static String loadResponse(final String path) {
		InputStream inputStream = loadResource(path);
		String response = StringUtils.EMPTY;

		if (inputStream != null) {
			try {
				Document document = Jsoup.parse(inputStream, "utf-8", StringUtils.EMPTY);
				response = document.html();
			} catch (IOException ioe) {
				ioe.printStackTrace();

				return StringUtils.EMPTY;
			}
		}

		return response;
	}

	/**
	 * Loads a resource given its path.
	 *
	 * @param path
	 *            the resource path
	 * @return the string representation of the resource content
	 */
	private static InputStream loadResource(final String path) {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(SlightlyParserTest.class.getClassLoader().getResource(path).getFile());
		} catch (final FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		return inputStream;
	}
}
