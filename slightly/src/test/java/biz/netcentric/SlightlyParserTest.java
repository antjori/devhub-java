package biz.netcentric;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonReader;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class SlightlyParserTest {

	@Mock
	Logger logger;

	@Mock
	MongoDBConnector mongoDBConnector;

	@Mock
	HubbleServerService hubbleServerService;

	@Mock
	HubbleMapRepository hubbleMapRepository;

	@Mock
	HubbleMetricService hubbleMetricService;

	@Mock
	HubbleGraphRestClient hubbleGraphRestClient;

	@Mock
	HubbleUserService hubbleUserService;

	@Mock
	TeamService teamService;

	@Mock
	HttpServletRequest request;

	@Mock
	HttpSession session;

	@InjectMocks
	HubbleMapService hubbleMapService;

	@Spy
	static HubbleMap hubbleMap;

	@Captor
	ArgumentCaptor<HubbleMap> captor;

	static List<HubbleServer> serverList;

	static String dashboard;

	static String hubbleMapAsString;

	@Spy
	Response response;

	static RemoveSnapshotRequest removeSnapshotRequest;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	static HubbleUser hubbleUser;

	static HubbleGraphOrganization organization;

	static HubbleServer hubbleServer;

	@BeforeClass
	public static void init() {
		hubbleMap = getHubbleMap();
		serverList = getServerList();
		dashboard = loadDashboard();
		hubbleMapAsString = loadMap();
		removeSnapshotRequest = getRemoveSnapshotRequest();
		hubbleUser = getHubbleUser();
		organization = getOrganization();
		hubbleServer = getHubbleServer();
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	// =============
	// saveMap tests
	// =============

	@Test
	public void saveMapTest() {

		/*
		 * Instruct Mockito to return a response Status.OK (200) when
		 * hubbleMapService.saveMap will be called.
		 */
		doNothing().when(hubbleMapRepository).saveMapState(hubbleMap);

		final Response response = hubbleMapService.saveMap(hubbleMap);

		/*
		 * Verify that hubbleMapService.saveMap was indeed called one time.
		 */
		verify(hubbleMapRepository, times(1)).saveMapState(captor.capture());

		/*
		 * Assert that hubbleMapService.saveMap was called with a particular
		 * HubbleMap, assert hubbleMap details
		 */
		assertThat(captor.getValue(), notNullValue());
		assertThat(captor.getValue().getUserId(), is("123456"));
		assertThat(captor.getValue().getGroups(), notNullValue());
		assertThat(captor.getValue().getGroups().size(), is(2));
		assertThat(captor.getValue().getFavorites(), notNullValue());
		assertThat(captor.getValue().getFavorites().size(), is(2));
		assertThat(captor.getValue().getZoom(), is(150));

		/*
		 * Assert that hubbleMapService.saveMap return a response 200 when
		 * called
		 */
		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
	}

	@Test
	public void saveMapNotOKTest() {
		/*
		 * Instruct Mockito to return a response Status.INTERNAL_SERVER_ERROR
		 * (500) when hubbleMapService.saveMap will be called.
		 */
		// when(hubbleMapService.saveMap(hubbleMap)).thenReturn(responseNotOk);
		doThrow(HubbleMapRepositoryException.class).when(hubbleMapRepository).saveMapState(any(HubbleMap.class));

		final Response response = hubbleMapService.saveMap(any(HubbleMap.class));

		/*
		 * Verify that hubbleMapService.saveMap was indeed called one time.
		 */
		verify(hubbleMapRepository, times(1)).saveMapState(captor.capture());

		/*
		 * Assert that hubbleMapService.saveMap return a response 200 when
		 * called
		 */
		assertThat(response.getStatus(), is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
		assertThat(response.getEntity(), nullValue());
	}

	// ==================
	// loadMapState tests
	// ==================

	@Test
	public void loadMapStateTest() {
		when(hubbleMapRepository.loadMapState(anyString())).thenReturn(hubbleMap);
		when(hubbleUserService.findHubbleUser(anyString())).thenReturn(hubbleUser);
		when(teamService.findByNames(anyListOf(String.class))).thenReturn(getTeams());
		when(hubbleServerService.findByNames(anyListOf(String.class))).thenReturn(getServerList());

		final Response response = hubbleMapService.loadMap(anyString());

		verify(hubbleMapRepository, times(1)).loadMapState(anyString());

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
		assertThat((HubbleMap) response.getEntity(), is(hubbleMap));
	}

	@Test
	public void loadFilteredMapStateTest() {
		when(hubbleMapRepository.loadMapState(anyString())).thenReturn(getFilteredHubbleMap());
		when(hubbleUserService.findHubbleUser(anyString())).thenReturn(hubbleUser);
		when(teamService.findByNames(anyListOf(String.class))).thenReturn(getTeams());
		when(hubbleServerService.findByNames(anyListOf(String.class))).thenReturn(getServerList());

		final Response response = hubbleMapService.loadMap(anyString());

		verify(hubbleMapRepository, times(1)).loadMapState(anyString());

		assertThat(response.getStatus(), is(Status.PARTIAL_CONTENT.getStatusCode()));
		assertThat(HubbleUtils.pojoToJson((HubbleMap) response.getEntity()),
				is(HubbleUtils.pojoToJson(getEmptyHubbleMap())));
	}

	@Test
	public void loadMapStateNotOkTest() {
		doThrow(HubbleMapRepositoryException.class).when(hubbleMapRepository).loadMapState(anyString());

		final Response response = hubbleMapService.loadMap(anyString());

		verify(hubbleMapRepository, times(1)).loadMapState(anyString());

		assertThat(response.getStatus(), is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
		assertThat(response.getEntity(), nullValue());
	}

	@Test
	public void loadMapStateNullUserIdTest() {
		doThrow(LoadMapStateException.class).when(hubbleMapRepository).loadMapState(null);

		final Response response = hubbleMapService.loadMap(null);

		verify(hubbleMapRepository, times(1)).loadMapState(null);

		assertThat(response.getStatus(), is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
		assertThat(response.getEntity(), nullValue());
	}

	// ======================
	// getServersStatus tests
	// ======================

	@Test
	public void getServersStatusTest() {
		final String serverToUpdate = "testv0085,testv0086";
		final Map<String, ServerStatus> serverUpdated = new HashMap<>();
		serverUpdated.put("testv0085", new ServerStatus());
		serverUpdated.put("testv0086", new ServerStatus());

		when(hubbleServerService.findByNames(anyListOf(String.class))).thenReturn(serverList);
		when(hubbleMetricService.getServerStatus(anyListOf(HubbleServer.class))).thenReturn(serverUpdated);

		final Response response = hubbleMapService.getServersStatus(serverToUpdate);

		verify(hubbleServerService, times(1)).findByNames(anyListOf(String.class));
		verify(hubbleMetricService, times(1)).getServerStatus(anyListOf(HubbleServer.class));

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
	}

	@Test
	public void getServersStatusNotOkTest() {
		doThrow(Exception.class).when(hubbleMetricService).getServerStatus(anyListOf(HubbleServer.class));

		final Response response = hubbleMapService.getServersStatus(anyString());

		verify(hubbleMetricService, times(1)).getServerStatus(anyListOf(HubbleServer.class));

		assertThat(response.getStatus(), is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
		assertThat(response.getEntity(), nullValue());
	}

	// ==================
	// hasDashboard tests
	// ==================

	@Test
	public void hasDashboardTest() {
		when(hubbleGraphRestClient.getOrganization(anyString())).thenReturn(organization);
		when(hubbleGraphRestClient.getDashboardByUser(anyString(), anyString())).thenReturn(response);
		when(response.getStatusInfo()).thenReturn(Status.OK);
		when(response.readEntity(String.class)).thenReturn(dashboard);

		final Response response = hubbleMapService.hasDashboard("testv0085", "admin");

		verify(hubbleGraphRestClient, times(1)).getDashboardByUser(anyString(), anyString());

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
		assertThat((Boolean) response.getEntity(), is(Boolean.TRUE));
	}

	@Test
	public void hasDashboardInvalidOrganizationTest() {
		when(hubbleGraphRestClient.getOrganization(anyString())).thenReturn(null);

		final Response response = hubbleMapService.hasDashboard("testv0085", "admin");

		verify(hubbleGraphRestClient, times(1)).getOrganization(anyString());

		assertThat(response.getStatus(), is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
		assertThat((Boolean) response.getEntity(), is(Boolean.FALSE));
	}

	@Test
	public void hasDashboardDashboardNotFoundTest() {
		when(hubbleGraphRestClient.getOrganization(anyString())).thenReturn(organization);
		when(hubbleGraphRestClient.getDashboardByUser(anyString(), anyString())).thenReturn(response);
		when(response.getStatusInfo()).thenReturn(Status.NOT_FOUND);
		when(response.readEntity(String.class)).thenReturn("{\"message\":\"Dashboard not found\"}");

		final Response response = hubbleMapService.hasDashboard("myDashboard", "admin");

		verify(hubbleGraphRestClient, times(1)).getOrganization(anyString());
		verify(hubbleGraphRestClient, times(1)).getDashboardByUser(anyString(), anyString());

		assertThat(response.getStatus(), is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
		assertThat((Boolean) response.getEntity(), is(Boolean.FALSE));
	}

	@Test
	public void hasDashboardNotAuthorizedTest() {
		when(hubbleGraphRestClient.getOrganization(anyString())).thenReturn(organization);
		when(hubbleGraphRestClient.getDashboardByUser(anyString(), anyString())).thenReturn(response);
		when(response.getStatusInfo()).thenReturn(Status.UNAUTHORIZED);
		when(response.readEntity(String.class)).thenReturn("{\"message\":\"Unauthorized\"}");

		final Response response = hubbleMapService.hasDashboard("testv0085", "a12345");

		verify(hubbleGraphRestClient, times(1)).getOrganization(anyString());
		verify(hubbleGraphRestClient, times(1)).getDashboardByUser(anyString(), anyString());

		assertThat(response.getStatus(), is(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
		assertThat((Boolean) response.getEntity(), is(Boolean.FALSE));
	}

	// ===============
	// uploadMap tests
	// ===============

	@Test
	public void uploadMapTest() throws IOException, MimeException {
		when(hubbleUserService.findHubbleUser(anyString())).thenReturn(hubbleUser);

		final Response response = hubbleMapService.uploadMap(createMultipartFormDataInput());

		verify(hubbleUserService, times(1)).findHubbleUser(anyString());
		verify(hubbleUserService, times(1)).addHubbleMapSnapshot(anyString(), any(HubbleMapSnapshot.class));

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
		assertNotNull(response.getEntity());
	}

	/**
	 * Creates a multiple part form data input instance.
	 *
	 * @return an instance of a multiple part form data input.
	 * @throws IOException
	 *             if an error occurs while build the imported file body
	 * @throws MimeException
	 *             if an error occurs while parsing the imported file header
	 */
	private MultipartFormDataInput createMultipartFormDataInput() throws IOException, MimeException {
		final ResteasyProviderFactory factory = new ResteasyProviderFactory();
		factory.register(StringTextStar.class);

		final MultipartFormDataInputImpl input = new MultipartFormDataInputImpl(MediaType.APPLICATION_OCTET_STREAM_TYPE,
				factory);

		// add map file

		final BodyPart fileBodyPart = new BodyPart();
		Header header = new Header();
		header.addField(AbstractField
				.parse("Content-Disposition: form-data; name=\"file\"; filename=\"123456_20160202000000_TRS.map\""));
		fileBodyPart.setHeader(header);
		fileBodyPart.setBody(new BodyFactory().binaryBody(loadMapAsStream()));

		final InputPart fileInputPart = input.new PartImpl(fileBodyPart);

		final List<InputPart> fileInputPartList = new ArrayList<InputPart>();
		fileInputPartList.add(fileInputPart);

		input.getFormDataMap().put("file", fileInputPartList);

		// add user identifier

		BodyPart userIdBodyPart = new BodyPart();
		userIdBodyPart.setHeader(new Header());
		userIdBodyPart.setBody(
				new BodyFactory().binaryBody(new ByteArrayInputStream("123456".getBytes(StandardCharsets.UTF_8))));

		final InputPart userIdInputPart = input.new PartImpl(userIdBodyPart);

		final List<InputPart> userIdInputPartList = new ArrayList<InputPart>();
		userIdInputPartList.add(userIdInputPart);

		input.getFormDataMap().put("userId", userIdInputPartList);

		return input;
	}

	@Test
	public void uploadMapNullInputTest() {

		Response response = hubbleMapService.uploadMap(null);

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
		assertNotNull(response.getEntity());
		assertThat(response.getEntity().toString(), is("The multi-part form data input cannot be null."));
	}

	@Test
	public void uploadMapNoFileTest() {
		final MultipartFormDataInputImpl input = new MultipartFormDataInputImpl(MediaType.APPLICATION_OCTET_STREAM_TYPE,
				new ResteasyProviderFactory());

		Response response = hubbleMapService.uploadMap(input);

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
		assertNotNull(response.getEntity());
		assertThat(response.getEntity().toString(), is("The form data map does not contain any file to be uploaded."));
	}

	@Test
	public void uploadMapNoPartsTest() {
		final MultipartFormDataInputImpl input = new MultipartFormDataInputImpl(MediaType.APPLICATION_OCTET_STREAM_TYPE,
				new ResteasyProviderFactory());

		input.getFormDataMap().put("file", new ArrayList<InputPart>());

		Response response = hubbleMapService.uploadMap(input);

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
		assertNotNull(response.getEntity());
		assertThat(response.getEntity().toString(),
				is("The multipart message received does not contain any parts to be uploaded."));
	}

	@Test
	public void uploadMapNoUserIdTest() throws IOException, MimeException {
		final MultipartFormDataInputImpl input = new MultipartFormDataInputImpl(MediaType.APPLICATION_OCTET_STREAM_TYPE,
				new ResteasyProviderFactory());

		// add map file

		final BodyPart fileBodyPart = new BodyPart();
		Header header = new Header();
		header.addField(AbstractField
				.parse("Content-Disposition: form-data; name=\"file\"; filename=\"123456_20160202000000_TRS.map\""));
		fileBodyPart.setHeader(header);
		fileBodyPart.setBody(new BodyFactory().binaryBody(loadMapAsStream()));

		final InputPart fileInputPart = input.new PartImpl(fileBodyPart);

		final List<InputPart> fileInputPartList = new ArrayList<InputPart>();
		fileInputPartList.add(fileInputPart);

		input.getFormDataMap().put("file", fileInputPartList);

		Response response = hubbleMapService.uploadMap(input);
		assertNotNull(response.getEntity());
		assertThat(response.getEntity().toString(), is("The form data map does not contain any user identifier."));
	}

	@Test
	@Ignore
	public void uploadMapInvalidFilePartBodyTest() throws IOException {
		final MultipartFormDataInputImpl input = new MultipartFormDataInputImpl(MediaType.APPLICATION_OCTET_STREAM_TYPE,
				new ResteasyProviderFactory());

		// add map file

		final InputPart fileInputPart = null;

		final List<InputPart> fileInputPartList = new ArrayList<InputPart>();
		fileInputPartList.add(fileInputPart);

		input.getFormDataMap().put("file", fileInputPartList);

		// add user identifier

		BodyPart userIdBodyPart = new BodyPart();
		userIdBodyPart.setHeader(new Header());
		userIdBodyPart.setBody(
				new BodyFactory().binaryBody(new ByteArrayInputStream("123456".getBytes(StandardCharsets.UTF_8))));

		final InputPart userIdInputPart = input.new PartImpl(userIdBodyPart);

		final List<InputPart> userIdInputPartList = new ArrayList<InputPart>();
		userIdInputPartList.add(userIdInputPart);

		input.getFormDataMap().put("userId", userIdInputPartList);

		Response response = hubbleMapService.uploadMap(input);
		assertNotNull(response.getEntity());
		assertThat(response.getEntity().toString(), is("An error occurred while processing the map file."));
	}

	@Test
	public void uploadMapUserNotFoundTest() throws IOException, MimeException {
		when(hubbleUserService.findHubbleUser(anyString())).thenReturn(null);

		Response response = hubbleMapService.uploadMap(createMultipartFormDataInput());
		assertNotNull(response.getEntity());
		assertThat(response.getEntity().toString(), is("The user with identifier 123456 does not exist."));
	}

	// =====================
	// createDashboard tests
	// =====================

	@Test
	public void createDashboardTest() {
		when(hubbleServerService.findByName(anyString())).thenReturn(hubbleServer);
		when(hubbleGraphRestClient.createDashboard(any(HubbleServer.class), anyString())).thenReturn(response);
		when(response.getStatusInfo()).thenReturn(Status.OK);
		when(response.readEntity(String.class)).thenReturn("{\"status\":\"success\"}");

		final Response response = hubbleMapService.createDashboard("name1", anyString());

		verify(hubbleGraphRestClient, times(1)).createDashboard(any(HubbleServer.class), anyString());

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
		assertThat((String) response.getEntity(), is("{\"status\":\"success\"}"));
	}

	@Test
	public void createDashboardDashboardNotSuccessTest() {
		when(hubbleGraphRestClient.createDashboard(any(HubbleServer.class), anyString())).thenReturn(response);
		when(response.getStatusInfo()).thenReturn(Status.BAD_REQUEST);
		when(response.readEntity(String.class)).thenReturn("{\"status\":\"version-mismatch\"}");

		final Response response = hubbleMapService.createDashboard("name1", anyString());

		verify(hubbleGraphRestClient, times(1)).createDashboard(any(HubbleServer.class), anyString());

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
		assertThat((String) response.getEntity(), is("{\"status\":\"version-mismatch\"}"));
	}

	// ====================
	// removeSnapshot tests
	// ====================

	@Test
	public void removeSnapshotTest() {
		doNothing().when(hubbleUserService).removeHubbleMapSnapshot(anyString(), anyString());

		Response response = hubbleMapService.removeSnapshot(removeSnapshotRequest);

		verify(hubbleUserService, times(1)).removeHubbleMapSnapshot(anyString(), anyString());

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
	}

	@Test
	public void removeSnapshotNullRequestTest() {
		doNothing().when(hubbleUserService).removeHubbleMapSnapshot(anyString(), anyString());

		Response response = hubbleMapService.removeSnapshot(null);

		verify(hubbleUserService, times(0)).removeHubbleMapSnapshot(anyString(), anyString());

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
	}

	@Test
	public void removeSnapshotNullUserIdTest() {
		doNothing().when(hubbleUserService).removeHubbleMapSnapshot(anyString(), anyString());

		RemoveSnapshotRequest request = new RemoveSnapshotRequest();
		request.setSnapshotId(String.valueOf(DateTime.now().getMillis()));

		Response response = hubbleMapService.removeSnapshot(request);

		verify(hubbleUserService, times(0)).removeHubbleMapSnapshot(anyString(), anyString());

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
	}

	@Test
	public void removeSnapshotNullSnapshotIdTest() {
		doNothing().when(hubbleUserService).removeHubbleMapSnapshot(anyString(), anyString());

		RemoveSnapshotRequest request = new RemoveSnapshotRequest();
		request.setUserId("132456");

		Response response = hubbleMapService.removeSnapshot(request);

		verify(hubbleUserService, times(0)).removeHubbleMapSnapshot(anyString(), anyString());

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
	}

	// ===================================
	// sendUnprofiledUserNotification test
	// ===================================

	// =================
	// auxiliary methods
	// =================

	/**
	 * Provides an instance of Hubble map.
	 *
	 * @return the Hubble map
	 */
	private static HubbleMap getHubbleMap() {
		final HubbleMap hubbleMap = new HubbleMap();

		hubbleMap.setUserId("123456");

		// groups
		final List<HubbleMapElement> groups = new ArrayList<>();

		final HubbleMapGroup element1 = new HubbleMapGroup();
		element1.setIndex(1);
		element1.setName("New Group 1");
		element1.setHeight(1);
		element1.setWidth(1);
		final List<String> servers = new ArrayList<>();
		servers.add("testv0086");
		element1.setServers(servers);
		element1.setPosx(1);
		element1.setPosy(1);
		groups.add(element1);

		final HubbleMapServer element2 = new HubbleMapServer();
		element2.setId("server0_1");
		element2.setIndex(2);
		element2.setName("testv0085");
		element2.setPosx(2);
		element2.setPosy(2);
		element2.setFavorite(Boolean.TRUE);
		groups.add(element2);

		hubbleMap.setGroups(groups);

		// favorites
		final List<HubbleMapServer> favorites = new ArrayList<>();
		final HubbleMapServer server1 = new HubbleMapServer();
		server1.setId("server0_1");
		server1.setName("testv0085");
		server1.setPosx(3);
		server1.setPosy(3);
		favorites.add(server1);

		final HubbleMapServer server2 = new HubbleMapServer();
		server2.setId("server0_1");
		server2.setName("tiths107");
		server2.setPosx(4);
		server2.setPosy(4);
		favorites.add(server2);

		hubbleMap.setFavorites(favorites);

		// zoom
		hubbleMap.setZoom(150);

		return hubbleMap;
	}

	/**
	 * Provides an instance of Hubble map.
	 *
	 * @return the Hubble map
	 */
	private static HubbleMap getFilteredHubbleMap() {
		final HubbleMap hubbleMap = new HubbleMap();

		hubbleMap.setUserId("123456");

		// groups
		final List<HubbleMapElement> groups = new ArrayList<>();

		final HubbleMapGroup element1 = new HubbleMapGroup();
		element1.setIndex(1);
		element1.setName("New Group 1");
		element1.setHeight(1);
		element1.setWidth(1);
		final List<String> servers = new ArrayList<>();
		servers.add("tiths001");
		element1.setServers(servers);
		element1.setPosx(1);
		element1.setPosy(1);
		groups.add(element1);

		final HubbleMapServer element2 = new HubbleMapServer();
		element2.setId("server0_1");
		element2.setIndex(2);
		element2.setName("tiths003");
		element2.setPosx(2);
		element2.setPosy(2);
		element2.setFavorite(Boolean.TRUE);
		groups.add(element2);

		hubbleMap.setGroups(groups);

		// favorites
		final List<HubbleMapServer> favorites = new ArrayList<>();
		final HubbleMapServer server1 = new HubbleMapServer();
		server1.setId("server0_1");
		server1.setName("tiths005");
		server1.setPosx(3);
		server1.setPosy(3);
		favorites.add(server1);

		final HubbleMapServer server2 = new HubbleMapServer();
		server2.setId("server0_1");
		server2.setName("tiths343");
		server2.setPosx(4);
		server2.setPosy(4);
		favorites.add(server2);

		hubbleMap.setFavorites(favorites);

		// zoom
		hubbleMap.setZoom(150);

		return hubbleMap;
	}

	/**
	 * Provides an instance of an empty Hubble map.
	 *
	 * @return the Hubble map
	 */
	private static HubbleMap getEmptyHubbleMap() {
		final HubbleMap hubbleMap = new HubbleMap();

		hubbleMap.setUserId("123456");

		// groups
		final List<HubbleMapElement> groups = new ArrayList<>();

		final HubbleMapGroup element1 = new HubbleMapGroup();
		element1.setIndex(1);
		element1.setName("New Group 1");
		element1.setHeight(1);
		element1.setWidth(1);
		final List<String> servers = new ArrayList<>();
		element1.setServers(servers);
		element1.setPosx(1);
		element1.setPosy(1);
		groups.add(element1);

		hubbleMap.setGroups(groups);

		// favorites
		hubbleMap.setFavorites(new ArrayList<HubbleMapServer>());

		// zoom
		hubbleMap.setZoom(150);

		return hubbleMap;
	}

	/**
	 * Provides the list of documents representing the available servers for
	 * monitoring.
	 *
	 * @return the list of documents representing the available servers for
	 *         monitoring
	 */
	private static List<HubbleServer> getServerList() {
		final List<HubbleServer> serverList = new ArrayList<>();

		HubbleServer server = new HubbleServer();
		server.setId(1L);
		server.setName("testv0085");
		server.setFullName("testv0085.fr.net.intra");
		server.setOs(SystemType.LINUX.getSystem());
		serverList.add(server);

		server = new HubbleServer();
		server.setId(2L);
		server.setName("testv0086");
		server.setFullName("testv0086.fr.net.intra");
		server.setOs(SystemType.LINUX.getSystem());
		serverList.add(server);

		server = new HubbleServer();
		server.setId(3L);
		server.setName("testv0087");
		server.setFullName("testv0087.fr.net.intra");
		server.setOs(SystemType.WINDOWS.getSystem());
		serverList.add(server);

		server = new HubbleServer();
		server.setId(4L);
		server.setName("tiths107");
		server.setFullName("tiths107.fr.net.intra");
		server.setOs(SystemType.UNIX.getSystem());
		serverList.add(server);

		return serverList;
	}

	/**
	 * Loads the JSON file that represents a Hubble graph dashboard.
	 *
	 * @return the string representation of the JSON file that represents a
	 *         Hubble graph dashboard
	 */
	private static String loadDashboard() {
		return loadResource("graph/dashboard.json");
	}

	/**
	 * Loads the file that represents a stored Hubble map.
	 *
	 * @return the string representation of the contents of the file that
	 *         represents a stored Hubble map
	 */
	private static String loadMap() {
		return loadResource("map/123456_20160101000000_TRS.map");
	}

	/**
	 * Loads the file that represents a stored Hubble map as an input stream.
	 *
	 * @return the input stream of the contents of the file that represents a
	 *         stored Hubble map
	 */
	private static InputStream loadMapAsStream() {
		return loadResourceAsStream("map/123456_20160202000000_TRS.map");
	}

	/**
	 * Loads a resource given its path.
	 *
	 * @param path
	 *            the resource path
	 * @return the string representation of the resource content
	 */
	private static String loadResource(final String path) {
		String resource = StringUtils.EMPTY;

		try {
			final FileReader fileReader = new FileReader(
					HubbleMapServiceTest.class.getClassLoader().getResource(path).getFile());
			final JsonReader jsonReader = Json.createReader(fileReader);

			resource = jsonReader.readObject().toString();
		} catch (final FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return resource;
	}

	/**
	 * Loads a resource as stream given its path.
	 *
	 * @param path
	 *            the resource path
	 * @return the input stream associated with the loaded resource
	 */
	private static InputStream loadResourceAsStream(final String path) {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(HubbleMapServiceTest.class.getClassLoader().getResource(path).getFile());
		} catch (final FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * Provides an instance of a removeSnapshot operation request.
	 *
	 * @return an instance of a removeSnapshot operation request
	 */
	private static RemoveSnapshotRequest getRemoveSnapshotRequest() {
		RemoveSnapshotRequest request = new RemoveSnapshotRequest();

		request.setSnapshotId(String.valueOf(DateTime.now().getMillis()));
		request.setUserId("123456");

		return request;
	}

	/**
	 * Provides an instance of Hubble user.
	 *
	 * @return an instance of Hubble user
	 */
	private static HubbleUser getHubbleUser() {
		final HubbleUser hubbleUser = new HubbleUser();

		hubbleUser.setUserId("123456");
		hubbleUser.setFirstName("John");
		hubbleUser.setLastName("DOE");
		hubbleUser.setFullName("DOE John");
		hubbleUser.setEmail("john.doe@externe.bnpparibas.com");
		hubbleUser.setPhone("+33 1 57 43 86 64");
		hubbleUser.setMobile("+33 6 43 94 25 76");
		hubbleUser.setTeams(getHubbleUserTeams());

		return hubbleUser;
	}

	/**
	 * Provides a set of Hubble user teams.
	 *
	 * @return a set of Hubble user teams
	 */
	private static Set<String> getHubbleUserTeams() {
		final Set<String> hubbleUserTeams = new HashSet<>();

		hubbleUserTeams.add("CCS");
		hubbleUserTeams.add("EPG");
		hubbleUserTeams.add("MFS");
		hubbleUserTeams.add("OMS");
		hubbleUserTeams.add("TRS");
		hubbleUserTeams.add("SDI");

		return hubbleUserTeams;
	}

	/**
	 * Provides an instance of an Hubble graph organization.
	 *
	 * @return an instance of an Hubble graph organization
	 */
	private static HubbleGraphOrganization getOrganization() {
		HubbleGraphOrganization organization = new HubbleGraphOrganization();

		organization.setId(1);
		organization.setName("admin");

		return organization;
	}

	/**
	 * Provides a list of teams.
	 *
	 * @return a list of teams
	 */
	private static List<Team> getTeams() {
		final List<Team> teams = new ArrayList<>();

		Team amdTeam = new Team();
		amdTeam.setName("AMD");
		amdTeam.setServers(Arrays.asList(new String[] { "tiths001" }));
		teams.add(amdTeam);

		Team ccsTeam = new Team();
		ccsTeam.setName("CCS");
		ccsTeam.setServers(Arrays.asList(new String[] { "tiths002" }));
		teams.add(ccsTeam);

		Team dmsTeam = new Team();
		dmsTeam.setName("DMS");
		dmsTeam.setServers(Arrays.asList(new String[] { "tiths003" }));
		teams.add(dmsTeam);

		Team epgTeam = new Team();
		epgTeam.setName("EPG");
		epgTeam.setServers(Arrays.asList(new String[] { "tiths004" }));
		teams.add(epgTeam);

		Team esmTeam = new Team();
		esmTeam.setName("ESM");
		esmTeam.setServers(Arrays.asList(new String[] { "tiths005" }));
		teams.add(esmTeam);

		Team mfsTeam = new Team();
		mfsTeam.setName("MFS");
		mfsTeam.setServers(Arrays.asList(new String[] { "tiths006" }));
		teams.add(mfsTeam);

		Team omsTeam = new Team();
		omsTeam.setName("OMS");
		omsTeam.setServers(Arrays.asList(new String[] { "tiths007" }));
		teams.add(omsTeam);

		Team trsTeam = new Team();
		trsTeam.setName("TRS");
		trsTeam.setServers(
				Arrays.asList(new String[] { "tiths008", "tiths107", "testv0085", "testv0086", "testv0087" }));
		teams.add(trsTeam);

		Team sdiTeam = new Team();
		sdiTeam.setName("SDI");
		sdiTeam.setServers(Arrays.asList(new String[] { "tiths009" }));
		teams.add(sdiTeam);

		return teams;
	}

	/**
	 * Provides an instance of Hubble server.
	 *
	 * @return an instance of Hubble server
	 */
	private static HubbleServer getHubbleServer() {
		return new HubbleServerBuilder().id(1).name("name1").fullName("fullName1").os(SystemType.LINUX).build();
	}

	/**
	 * Class that allows the build of HubbleServer instances.
	 *
	 */
	private static class HubbleServerBuilder {
		private HubbleServer hubbleServer;

		public HubbleServerBuilder() {
			hubbleServer = new HubbleServer();
		}

		HubbleServerBuilder id(long id) {
			hubbleServer.setId(id);
			return this;
		}

		HubbleServerBuilder name(String name) {
			hubbleServer.setName(name);
			return this;
		}

		HubbleServerBuilder fullName(String fullName) {
			hubbleServer.setFullName(fullName);
			return this;
		}

		HubbleServerBuilder os(SystemType os) {
			hubbleServer.setOs(os.getSystem());
			return this;
		}

		HubbleServer build() {
			return hubbleServer;
		}
	}
}
