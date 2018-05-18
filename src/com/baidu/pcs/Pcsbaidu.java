package com.baidu.pcs;

public class Pcsbaidu {

	private String expires_in;
	private String accessToken;
	private String refresh_token;
	private String session_secret;
	private String session_key;
	private String scope;
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getSession_secret() {
		return session_secret;
	}
	public void setSession_secret(String session_secret) {
		this.session_secret = session_secret;
	}
	public String getSession_key() {
		return session_key;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Pcsbaidu(String expires_in, String accessToken, String refresh_token,
			String session_secret, String session_key, String scope) {
		super();
		this.expires_in = expires_in;
		this.accessToken = accessToken;
		this.refresh_token = refresh_token;
		this.session_secret = session_secret;
		this.session_key = session_key;
		this.scope = scope;
	}
	public Pcsbaidu(){
		super();
	}
	
}
