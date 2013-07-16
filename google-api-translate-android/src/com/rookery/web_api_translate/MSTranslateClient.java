package com.rookery.web_api_translate;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

import com.rookery.web_api_translate.util.SimpleXmlConverter;

/*package*/ class MSTranslateClient {
	@SuppressWarnings("unused")
	private static final String TAG = "MSTranslateClient";
	
	private static final String API_URL = "http://api.microsofttranslator.com/V2/Http.svc";
	
	public class TransResult {
		public String text;
	}
	/**
	 * v2 request
	 * @author simsun
	 *
	 */
	public interface TranslateOp {
		@GET("/Translate")
		void getTranslation(@Header("Authorization") String access_token,
							@Query("appid") String appid,
							@Query("text") String text,
							@Query("to") String tolang,
							@Query("contentType") String content_type,
							Callback<String> callback);
	}
	
	private static RestAdapter initClient(boolean debug) {

		RestAdapter restAdapter;
		restAdapter = new RestAdapter.Builder().setServer(API_URL)
						.setDebug(debug)
						.setClient(new OkClient())
						.setConverter(new SimpleXmlConverter())
						.build();

		return restAdapter;
	}
	
	/**
	 * factory method for build v2 translate client
	 * @param debug log toggle
	 * @return 
	 */
	public static TranslateOp build_v2_client(boolean debug) {
		RestAdapter restAdapter = initClient(debug);
		return restAdapter.create(TranslateOp.class);
	}
}
