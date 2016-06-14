//package kz.essc.qtrack.rpn.com.ibm.oauth;
//
//import java.io.*;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.apache.commons.codec.binary.Base64;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URLEncodedUtils;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//public class OAuthUtils {
//
//	public static OAuth2Details createOAuthDetails(Properties config) {
//		OAuth2Details oauthDetails = new OAuth2Details();
//		oauthDetails.setAccessToken((String) config
//				.get(OAuthConstants.ACCESS_TOKEN));
//		oauthDetails.setRefreshToken((String) config
//				.get(OAuthConstants.REFRESH_TOKEN));
//		oauthDetails.setGrantType((String) config
//				.get(OAuthConstants.GRANT_TYPE));
//		oauthDetails.setClientId((String) config.get(OAuthConstants.CLIENT_ID));
//		oauthDetails.setClientSecret((String) config
//				.get(OAuthConstants.CLIENT_SECRET));
//		oauthDetails.setScope((String) config.get(OAuthConstants.SCOPE));
//		oauthDetails.setAuthenticationServerUrl((String) config
//				.get(OAuthConstants.AUTHENTICATION_SERVER_URL));
//		oauthDetails.setUsername((String) config.get(OAuthConstants.USERNAME));
//		oauthDetails.setPassword((String) config.get(OAuthConstants.PASSWORD));
//
//		return oauthDetails;
//	}
//
//	public static Properties getClientConfigProps(String path) {
////		InputStream is = OAuthUtils.class.getClassLoader().getResourceAsStream(path);
//
//		Properties config = new Properties();
////		try {
////			InputStream is = new FileInputStream(new File(path));
////			config.load(is);
////		} catch (IOException e) {
////			System.out.println("Could not load properties from " + path);
////			e.printStackTrace();
////			return null;
////		}
//
////		config.put("access_token", "access_token");
//		config.put("client_id", "ada0b759f52b4647b423c3a05d4236e8");
//		config.put("client_secret", "AMaKImPFIiARGwF2bw66YV/DsNXe7tfA9kQwuOUC3h4=");
////		config.put("refresh_token", "refresh_token");
//		config.put("username", "911020350941");
//		config.put("password", "zxc@123");
//		config.put("authentication_server_url", "http://5.104.236.197:777/oauth/token");
////		config.put("CONFIG_FILE_PATH", "/home/alpamys/dev/app/qtrack/api/src/main/resources/com/ibm/oauth/Oauth2Client.config");
//		config.put("resource_server_url", "http://5.104.236.197:22999/services/api/person?fioiin=911020350941");
//		config.put("grant_type", "password");
//		config.put("scope", "profile");
////		config.put("Authorization", "Basic"); // YWRhMGI3NTlmNTJiNDY0N2I0MjNjM2EwNWQ0MjM2ZTg6QU1hS0ltUEZJaUFSR3dGMmJ3NjZZVi9Ec05YZTd0ZkE5a1F3dU9VQzNoND0=");
////		config.put("Bearer", "Bearer");
////		config.put("Basic", "YWRhMGI3NTlmNTJiNDY0N2I0MjNjM2EwNWQ0MjM2ZTg6QU1hS0ltUEZJaUFSR3dGMmJ3NjZZVi9Ec05YZTd0ZkE5a1F3dU9VQzNoND0=");
//		//  YWRhMGI3NTlmNTJiNDY0N2I0MjNjM2EwNWQ0MjM2ZTg=:QU1hS0ltUEZJaUFSR3dGMmJ3NjZZVi9Ec05YZTd0ZkE5a1F3dU9VQzNoND0=");
////		config.put("application/json", "application/json");
////		config.put("application/xml", "application/xml");
////		config.put("application/x-www-form-urlencoded", "application/x-www-form-urlencoded");
//
//		return config;
//	}
//
//	public static void getProtectedResource(Properties config) {
//		String resourceURL = config
//				.getProperty(OAuthConstants.RESOURCE_SERVER_URL);
//		OAuth2Details oauthDetails = createOAuthDetails(config);
//		HttpGet get = new HttpGet(resourceURL);
//		get.addHeader(OAuthConstants.AUTHORIZATION,
//				getAuthorizationHeaderForAccessToken(oauthDetails
//						.getAccessToken()));
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpResponse response = null;
//		int code = -1;
//		try {
//			response = client.execute(get);
//			code = response.getStatusLine().getStatusCode();
//			if (code >= 400) {
//				// Access token is invalid or expired.Regenerate the access
//				// token
//				System.out
//						.println("Access token is invalid or expired. Regenerating access token....");
//				String accessToken = getAccessToken(oauthDetails);
//				System.out.println("accessToken fffff " + accessToken);
//				if (isValid(accessToken)) {
//					// update the access token
//					 System.out.println("New access token: " + accessToken);
//					oauthDetails.setAccessToken(accessToken);
//					get.removeHeaders(OAuthConstants.AUTHORIZATION);
//					get.addHeader(OAuthConstants.AUTHORIZATION,
//							getAuthorizationHeaderForAccessToken(oauthDetails
//									.getAccessToken()));
//					get.releaseConnection();
//					response = client.execute(get);
//					code = response.getStatusLine().getStatusCode();
//					if (code >= 400) {
//						throw new RuntimeException(
//								"Could not access protected resource. Server returned http code: "
//										+ code);
//
//					}
//
//				} else {
//					throw new RuntimeException(
//							"Could not regenerate access token");
//				}
//
//			}
//
//			JSONObject obj = handleResponse(response);
//			System.out.println(obj.toString());
//
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			get.releaseConnection();
//		}
//
//	}
//
//	public static String getAccessToken(OAuth2Details oauthDetails) {
//		HttpPost post = new HttpPost(oauthDetails.getAuthenticationServerUrl()+"");
////		HttpGet post = new HttpGet("http://5.104.236.197:22999/services?grant_type=password&username=911020350941&password=zxc@123");
//		String clientId = oauthDetails.getClientId();
//		String clientSecret = oauthDetails.getClientSecret();
//		String scope = oauthDetails.getScope();
//
//		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
//		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,
//				oauthDetails.getGrantType()));
//		parametersBody.add(new BasicNameValuePair(OAuthConstants.USERNAME,
//				oauthDetails.getUsername()));
//		parametersBody.add(new BasicNameValuePair(OAuthConstants.PASSWORD,
//				oauthDetails.getPassword()));
//
//		if (isValid(clientId)) {
//			parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,
//					clientId));
//		}
//		if (isValid(clientSecret)) {
//			parametersBody.add(new BasicNameValuePair(
//					OAuthConstants.CLIENT_SECRET, clientSecret));
//		}
//		if (isValid(scope)) {
//			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE,
//					scope));
//		}
//
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpResponse response = null;
//		String accessToken = null;
//		try {
////			post.set
//			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
//			post.addHeader("Authorization", "Basic YWRhMGI3NTlmNTJiNDY0N2I0MjNjM2EwNWQ0MjM2ZTg6QU1hS0ltUEZJaUFSR3dGMmJ3NjZZVi9Ec05YZTd0ZkE5a1F3dU9VQzNoND0=");
//
//			response = client.execute(post);
//			int code = response.getStatusLine().getStatusCode();
//			if (code >= 400) {
//				System.out
//						.println("Authorization server expects Basic authentication");
//				// Add Basic Authorization header
////				post.addHeader(
////						OAuthConstants.AUTHORIZATION,
////						getBasicAuthorizationHeader(oauthDetails.getUsername(),
////								oauthDetails.getPassword()));
//				post.addHeader("Authorization", "Basic YWRhMGI3NTlmNTJiNDY0N2I0MjNjM2EwNWQ0MjM2ZTg6QU1hS0ltUEZJaUFSR3dGMmJ3NjZZVi9Ec05YZTd0ZkE5a1F3dU9VQzNoND0=");
//				System.out.println("Retry with login credentials");
//				post.releaseConnection();
//				response = client.execute(post);
//				code = response.getStatusLine().getStatusCode();
//				if (code >= 400) {
//					System.out.println("Retry with client credentials");
//					post.removeHeaders(OAuthConstants.AUTHORIZATION);
//
//					post.addHeader("Authorization", "Basic YWRhMGI3NTlmNTJiNDY0N2I0MjNjM2EwNWQ0MjM2ZTg6QU1hS0ltUEZJaUFSR3dGMmJ3NjZZVi9Ec05YZTd0ZkE5a1F3dU9VQzNoND0=");
//
////					post.addHeader(
////							OAuthConstants.AUTHORIZATION,
////							getBasicAuthorizationHeader(
////									oauthDetails.getClientId(),
////									oauthDetails.getClientSecret()));
//					post.releaseConnection();
//					response = client.execute(post);
//					code = response.getStatusLine().getStatusCode();
//					if (code >= 400) {
//						throw new RuntimeException(
//								"Could not retrieve access token for user: "
//										+ oauthDetails.getUsername());
//					}
//				}
//
//			}
////			Map<String, String> map = handleResponse(response);
////			accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
//
//			JSONObject obj = handleResponse(response);
//			if (obj.has(OAuthConstants.ACCESS_TOKEN)) {
//				accessToken = (String) obj.get(OAuthConstants.ACCESS_TOKEN);
//				System.out.println("accessToken ALPA   " + accessToken);
//				oauthDetails.setAccessToken(accessToken);
//			}
//			System.out.println(obj.toString());
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		return accessToken;
//	}
//
//	public static JSONObject handleResponse(HttpResponse response) { // Map
//		String contentType = OAuthConstants.JSON_CONTENT;
//		if (response.getEntity().getContentType() != null) {
//			contentType = response.getEntity().getContentType().getValue();
//		}
//
//		try {
//			String str = EntityUtils.toString(response.getEntity());
//			if (str.charAt(0) == '{') {
//				return new JSONObject(str);
//			}
//			else if (str.charAt(0) == '[') {
//				JSONArray arr = new JSONArray(str);
//
//				JSONObject obj = new JSONObject();
//				obj.put("array", arr);
//				return obj;
//			}
//		} catch (JSONException | IOException e) {
//			e.printStackTrace();
//		}
//
//		return new JSONObject();
//
//		/*if (contentType.contains(OAuthConstants.JSON_CONTENT)) {
//			return handleJsonResponse(response);
//		} else if (contentType.contains(OAuthConstants.URL_ENCODED_CONTENT)) {
//			return handleURLEncodedResponse(response);
//		} else if (contentType.contains(OAuthConstants.XML_CONTENT)) {
//			return handleXMLResponse(response);
//		} else {
//			// Unsupported Content type
//			throw new RuntimeException(
//					"Cannot handle "
//							+ contentType
//							+ " content type. Supported content types include JSON, XML and URLEncoded");
//		}*/
//
//	}
//
//	public static Map handleJsonResponse(HttpResponse response) {
//		Map<String, String> oauthLoginResponse = null;
//		String contentType = response.getEntity().getContentType().getValue();
//		try {
//			oauthLoginResponse = (Map<String, String>) new JSONParser()
//					.parse(EntityUtils.toString(response.getEntity()));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException();
//		} catch (org.json.simple.parser.ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException();
//		} catch (RuntimeException e) {
//			System.out.println("Could not parse JSON response");
//			throw e;
//		}
//		System.out.println();
//		System.out.println("********** Response Received **********");
//		for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet()) {
//			System.out.println(String.format("  %s = %s", entry.getKey(),
//					entry.getValue()));
//		}
//		return oauthLoginResponse;
//	}
//
//	public static Map handleURLEncodedResponse(HttpResponse response) {
//		Map<String, Charset> map = Charset.availableCharsets();
//		Map<String, String> oauthResponse = new HashMap<String, String>();
//		Set<Map.Entry<String, Charset>> set = map.entrySet();
//		Charset charset = null;
//		HttpEntity entity = response.getEntity();
//
//		System.out.println();
//		System.out.println("********** Response Received **********");
//
//		for (Map.Entry<String, Charset> entry : set) {
//			System.out.println(String.format("  %s = %s", entry.getKey(),
//					entry.getValue()));
//			if (entry.getKey().equalsIgnoreCase(HTTP.UTF_8)) {
//				charset = entry.getValue();
//			}
//		}
//
//		try {
//			List<NameValuePair> list = URLEncodedUtils.parse(
//					EntityUtils.toString(entity), Charset.forName(HTTP.UTF_8));
//			for (NameValuePair pair : list) {
//				System.out.println(String.format("  %s = %s", pair.getName(),
//						pair.getValue()));
//				oauthResponse.put(pair.getName(), pair.getValue());
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException("Could not parse URLEncoded Response");
//		}
//
//		return oauthResponse;
//	}
//
//	public static Map handleXMLResponse(HttpResponse response) {
//		Map<String, String> oauthResponse = new HashMap<String, String>();
//		try {
//
//			String xmlString = EntityUtils.toString(response.getEntity());
//			DocumentBuilderFactory factory = DocumentBuilderFactory
//					.newInstance();
//			DocumentBuilder db = factory.newDocumentBuilder();
//			InputSource inStream = new InputSource();
//			inStream.setCharacterStream(new StringReader(xmlString));
//			Document doc = db.parse(inStream);
//
//			System.out.println("********** Response Receieved **********");
//			parseXMLDoc(null, doc, oauthResponse);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(
//					"Exception occurred while parsing XML response");
//		}
//		return oauthResponse;
//	}
//
//	public static void parseXMLDoc(Element element, Document doc,
//			Map<String, String> oauthResponse) {
//		NodeList child = null;
//		if (element == null) {
//			child = doc.getChildNodes();
//
//		} else {
//			child = element.getChildNodes();
//		}
//		for (int j = 0; j < child.getLength(); j++) {
//			if (child.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
//				Element childElement = (Element) child
//						.item(j);
//				if (childElement.hasChildNodes()) {
//					System.out.println(childElement.getTagName() + " : "
//							+ childElement.getTextContent());
//					oauthResponse.put(childElement.getTagName(),
//							childElement.getTextContent());
//					parseXMLDoc(childElement, null, oauthResponse);
//				}
//
//			}
//		}
//	}
//
//	public static String getAuthorizationHeaderForAccessToken(String accessToken) {
//		return OAuthConstants.BEARER + " " + accessToken;
//	}
//
//	public static String getBasicAuthorizationHeader(String username,
//			String password) {
//		return OAuthConstants.BASIC + " "
//				+ encodeCredentials(username, password);
//	}
//
//	public static String encodeCredentials(String username, String password) {
//		String cred = username + ":" + password;
//		String encodedValue = null;
//		byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
//		encodedValue = new String(encodedBytes);
//		System.out.println("encodedBytes " + new String(encodedBytes));
//
//		byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
//		System.out.println("decodedBytes " + new String(decodedBytes));
//
//		return encodedValue;
//
//	}
//
//	public static boolean isValid(String str) {
//		return (str != null && str.trim().length() > 0);
//	}
//
//}
