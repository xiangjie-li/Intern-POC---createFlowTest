import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

public class TokenGetter {
	
	
	
	  /** Directory to store user credentials. */
	  private static final File DATA_STORE_DIR =
	      new File(System.getProperty("user.home"), ".store/poctest");

	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	  private static FileDataStoreFactory DATA_STORE_FACTORY;

//	  /** OAuth 2 scope. */
//	  private static final String SCOPE = "https://www.googleapis.com/auth/content";
//
//	  /** Global instance of the HTTP transport. */
//	  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//
//	  /** Global instance of the JSON factory. */
//	  static final JsonFactory JSON_FACTORY = new JacksonFactory();
//	  
//
//	  private static final String TOKEN_SERVER_URL = "https://graph.facebook.com/v7.0/oauth/access_token";
//	  private static final String AUTHORIZATION_SERVER_URL =
//	      "https://www.facebook.com/v2.12/dialog/oauth";

	  /** Authorizes the installed application to access user's protected data. */
	  private static Credential authorize(String userid, String scope, String tokenUrl, String authorizeUrl, String clientId, String clientSecret) throws Exception {
		  
		  String SCOPE = scope;
		  HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		  JsonFactory JSON_FACTORY = new JacksonFactory();
		  String TOKEN_SERVER_URL = tokenUrl;
		  String AUTHORIZATION_SERVER_URL = authorizeUrl;
		  String CLIENT_ID = clientId;
		  String CLIENT_SECRET = clientSecret;
		  
//	    OAuth2ClientCredentials.errorIfNotSpecified();
	    // set up authorization code flow
		  
		  
	    AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
	    	BearerToken.authorizationHeaderAccessMethod(),
	        HTTP_TRANSPORT,
	        JSON_FACTORY,
	        new GenericUrl(TOKEN_SERVER_URL),
	        new ClientParametersAuthentication(
	        		CLIENT_ID, CLIENT_SECRET),
	        CLIENT_ID,
	        AUTHORIZATION_SERVER_URL)
	    	.setScopes(Arrays.asList(SCOPE))
	        .setDataStoreFactory(DATA_STORE_FACTORY).build();
	    
	    
	    // authorize
	    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(
	        "localhost").setPort(8081).build();
	    return new AuthorizationCodeInstalledApp(flow, receiver).authorize(userid);
	  }

//	  private static void run(HttpRequestFactory requestFactory) throws IOException {
//	    DailyMotionUrl url = new DailyMotionUrl("https://api.dailymotion.com/videos/favorites");
//	    url.setFields("id,tags,title,url");
//
//	    HttpRequest request = requestFactory.buildGetRequest(url);
//	    VideoFeed videoFeed = request.execute().parseAs(VideoFeed.class);
//	    if (videoFeed.list.isEmpty()) {
//	      System.out.println("No favorite videos found.");
//	    } else {
//	      if (videoFeed.hasMore) {
//	        System.out.print("First ");
//	      }
//	      System.out.println(videoFeed.list.size() + " favorite videos found:");
//	      for (Video video : videoFeed.list) {
//	        System.out.println();
//	        System.out.println("-----------------------------------------------");
//	        System.out.println("ID: " + video.id);
//	        System.out.println("Title: " + video.title);
//	        System.out.println("Tags: " + video.tags);
//	        System.out.println("URL: " + video.url);
//	      }
//	    }
//	  }

	  public static void main(String[] args) {
	    try {
	      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//	      final Credential credential = authorize();
//	      
//	      System.out.println(credential.getAccessToken());
//	      HttpRequestFactory requestFactory =
//	          HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
//	            @Override
//	            public void initialize(HttpRequest request) throws IOException {
//	              credential.initialize(request);
//	              request.setParser(new JsonObjectParser(JSON_FACTORY));
//	            }
//	          });
//	      run(requestFactory);
	      // Success!
	      return;
	    } catch (IOException e) {
	      System.err.println(e.getMessage());
	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
	    System.exit(1);
	  }
	  
	  
	  public static String TokenLoader(String name, String userid, String scope, String tokenUrl, String authorizeUrl, String clientId, String clientSecret) throws Exception {
//		  DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//		  AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(null, null, null, null, null, null, null).setDataStoreFactory(DATA_STORE_FACTORY).build();
//		  Credential credential = flow.loadCredential(userid);
//		  return credential.getAccessToken();
		  
		  
	      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
	      Credential credential = authorize(userid, scope, tokenUrl, authorizeUrl, clientId, clientSecret);

//            String accessToken = credential.getAccessToken();
	      
	          
	          String refreshToken = name.equals("facebook")? credential.getAccessToken() : credential.getRefreshToken();
	      
//	          String refreshToken = credential.getAccessToken();
//	          Long expire = credential.getExpiresInSeconds();
	          
	          
//	          String accessToken = getRefreshToken(refreshToken, tokenUrl, clientId, clientSecret);
	              
	          
//			  System.out.println("Access Token: " + accessToken);
//			  System.out.println("Refresh Token: " + refreshToken);
//			  System.out.println("Expire in Seconds: " + expire);
	         
	        
			  return refreshToken;
	  }
	  
	  public static String getRefreshToken(String refreshToken, String tokenUrl, String clientId, String clientSecret) throws IOException {
		  
		  String TOKEN_SERVER_URL = tokenUrl;
		  String CLIENT_ID = clientId;
		  String CLIENT_SECRET = clientSecret;
		    try {

		        TokenResponse response =
		            new RefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(), new GenericUrl(
		            		TOKEN_SERVER_URL), refreshToken)
//		                .setGrantType("fb_exchange_token").set("fb_exchange_token", refreshToken)
		                .setClientAuthentication(
		                		new ClientParametersAuthentication(
		                	            CLIENT_ID, CLIENT_SECRET)).execute();
				    System.out.println("Access Token: " + response.getAccessToken());
				    System.out.println("Refresh Token: " + response.getRefreshToken());
				    System.out.println(response.get("expires_in"));
		        return response.getAccessToken();
		      } catch (TokenResponseException e) {
		        if (e.getDetails() != null) {
		          System.err.println("Error: " + e.getDetails().getError());
		          if (e.getDetails().getErrorDescription() != null) {
		            System.err.println(e.getDetails().getErrorDescription());
		          }
		          if (e.getDetails().getErrorUri() != null) {
		            System.err.println(e.getDetails().getErrorUri());
		          }
		        } else {
		          System.err.println(e.getMessage());
		        }
		      }
		    return null;
	  }
}
