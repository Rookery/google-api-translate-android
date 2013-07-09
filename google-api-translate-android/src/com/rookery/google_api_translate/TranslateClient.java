package com.rookery.google_api_translate;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;

public class TranslateClient {
	@SuppressWarnings("unused")
	private static final String TAG = "TranslateClient";
	
	private static final String API_URL = "https://www.googleapis.com/language/translate";
	
	public class TransResult {
		private ResponseData data;
		
		public ArrayList<TransEntity> get_entities() {
			return data.translations;
		}
	}
	
	public class ResponseData {
		private ArrayList<TransEntity> translations;
	}
	
	//FIXME: enum language
	public class TransEntity {
		private String translatedText;
		private String detectedSourceLanguage;
		
		public String get_translated_text() {
			return translatedText;
		}
		
		public String get_source_language() {
			return detectedSourceLanguage;
		}
	}
	/**
	 * v2 request
	 * @author simsun
	 *
	 */
	public interface TranslateOp {
		@GET("/v2")
		void getTranslation(@Query("key") String api_key,
							@Query("target") String targetLang,
							@Query("q") String q,
							Callback<TransResult> callback);
	}
	
	private static RestAdapter initClient() {

		RestAdapter restAdapter;
		restAdapter = new RestAdapter.Builder().setServer(API_URL)
						.setDebug(true)
						.setClient(new OkClient())
						.build();

		return restAdapter;
	}
	
	public static void GET(final String api_key, final String targetLang, final String q,
							Callback<TransResult> callback) {
		RestAdapter restAdapter = initClient();

		// Create an instance API interface.
		TranslateOp Translator = restAdapter.create(TranslateOp.class);

		Translator.getTranslation(api_key, targetLang, q, callback);
	}
}
