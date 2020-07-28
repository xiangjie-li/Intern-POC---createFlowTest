import java.io.IOException;

import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class TokenRefresher {
	
	  public static String getRefreshToken(String systemName, String refreshToken, String tokenUrl, String clientId, String clientSecret) throws IOException {
		  
		  String TOKEN_SERVER_URL = tokenUrl;
		  String CLIENT_ID = clientId;
		  String CLIENT_SECRET = clientSecret;
		    try {
                if (systemName.equals("facebook")) {
		        TokenResponse response =
		            new RefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(), new GenericUrl(
		            		TOKEN_SERVER_URL), refreshToken)
		                .setGrantType("fb_exchange_token").set("fb_exchange_token", refreshToken)
		                .setClientAuthentication(
		                		new ClientParametersAuthentication(
		                	            CLIENT_ID, CLIENT_SECRET)).execute();
				    System.out.println("Access Token: " + response.getAccessToken());
				    System.out.println("Refresh Token: " + response.getRefreshToken());
				    System.out.println(response.get("expires_in"));
		        return response.getAccessToken();
                }
                
                else {
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
                }
		        
		        
		        
		        
		        
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
