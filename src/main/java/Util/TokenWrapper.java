package util;

import java.io.Serializable;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;

public class TokenWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8983613985747209603L;
	
	private String accessToken, refreshToken;
	
	public TokenWrapper(AccessTokenResponse token) {
		this.accessToken = token.accessToken;
		this.refreshToken = token.refreshToken;
	}
	
	public AccessTokenResponse getToken() {
		AccessTokenResponse token = new AccessTokenResponse();
		token.accessToken = this.accessToken;
		token.refreshToken = this.refreshToken;
		return token;
	}
}
