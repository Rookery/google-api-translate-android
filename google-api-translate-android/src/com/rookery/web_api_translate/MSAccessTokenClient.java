package com.rookery.web_api_translate;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/*package*/ class MSAccessTokenClient {
	@SuppressWarnings("unused")
	private static final String TAG = "MSTranslateClient";
	protected static final String ENCODING = "UTF-8";
	private static final String API_URL ="https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
	
	public class AccessTokenResult {
		public String access_token;
		public String token_type;
		public String expires_in;
		public String scope;
	}
	/**
	 * v2 request
	 * @author simsun
	 *   uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=" + ENCODING);
       uc.setRequestProperty("Accept-Charset",ENCODING);
	 */
	public interface AccessTokenOp {
		@Headers({  "Content-Type: application/x-www-form-urlencoded; charset=" + ENCODING,
					"Accept-Charset: " + ENCODING})
		@FormUrlEncoded
		@POST("/")
		void getAccessToken(
							@Field("client_id") String client_id,
							@Field("client_secret") String client_secret,
							@Field("scope") String scope,
							@Field("grant_type") String grant_type,
							Callback<AccessTokenResult> callback);
	}
	
	private static RestAdapter initClient(boolean debug) {

		RestAdapter restAdapter;
		restAdapter = new RestAdapter.Builder().setServer(API_URL)
						.setDebug(debug)
						.setClient(new OkClient())
						.build();

		return restAdapter;
	}
	
	/**
	 * factory method for build v2 translate client
	 * @param debug log toggle
	 * @return 
	 */
	public static AccessTokenOp buildAccessTokenClient(boolean debug) {
		RestAdapter restAdapter = initClient(debug);
		return restAdapter.create(AccessTokenOp.class);
	}
	
	
}
