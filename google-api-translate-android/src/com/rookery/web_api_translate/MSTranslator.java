/**
 * 
 */
package com.rookery.web_api_translate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;

import com.rookery.web_api_translate.MSAccessTokenClient.AccessTokenResult;
import com.rookery.web_api_translate.type.Language;
import com.rookery.web_api_translate.type.TranslateError;

/**
 * @author simsun
 *
 */
public class MSTranslator {
	
	private static final String TAG = "MSTranslator";
	private static MSTranslator translator;
	private MSTranslateClient.TranslateOp translate_client;
	private MSAccessTokenClient.AccessTokenOp access_token_client;
	private static boolean debug_flag = true;
	private MSTranslator() {
		if (translate_client == null) {
			translate_client = MSTranslateClient.build_v2_client(debug_flag);
		}
		if (access_token_client == null) {
			access_token_client = MSAccessTokenClient.buildAccessTokenClient(debug_flag);
		}
	}
	/**
	 * default is disbale blog.
	 * if u want to enable log , u should invoke this function before getInstance().
	 */
	public static void enableLog() {
		debug_flag = true;
	}
	
	public static MSTranslator getInstance() {
		synchronized (MSTranslator.class) {
			if (translator == null) {
				translator = new MSTranslator();
			}	
		}
		return translator;		
	}
	
	public abstract interface TransCallback {
		public abstract void onSuccess(Language detected_lang, String translated_text);
		public abstract void onFailed(TranslateError e);
	}
	
	public abstract interface TokenCallback {
		public abstract void onSuccess(String access_token, String expired_time);
		public abstract void onFailed(TranslateError e);
	}
	
	/**
	 * execute translation
	 * @param text the text u wonder to translated.
	 * @param dest_lang destination language.
	 * @param api_key api_key
	 * @param cb callback for get execute result
	 */
	public void execute(String text, Language dest_lang, String access_token, final TransCallback cb) {
		String dest_lang_str;
		// catch null exception. set to English for default
		if (dest_lang == null) {
			dest_lang_str = "en";
		} else {
			dest_lang_str = dest_lang.toString();
			//catch the language cannot support by google, set to English for default.
			if (dest_lang_str == null || dest_lang_str.length() == 0) {
				dest_lang_str = "en";
			}
		}
		access_token = "Bearer " + access_token;
		translate_client.getTranslation(access_token, "", text, dest_lang_str, "text/plain",new retrofit.Callback<String>() {
			
			@Override
			public void failure(RetrofitError e) {
				cb.onFailed(new TranslateError(e));
			}

			@Override
			public void success(String arg0, Response arg1) {
				Log.d(TAG, "text:" + arg0);
			}
		});
	}
	
	public void get_access_token(String client_id, String client_secret, final TokenCallback cb) {
		try {
			final String params = "grant_type=client_credentials&scope=http://api.microsofttranslator.com"
			           + "&client_id=" + URLEncoder.encode(client_id, "UTF-8")
			           + "&client_secret=" + URLEncoder.encode(client_secret, "UTF-8") ;
			Log.e(TAG, params);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		access_token_client.getAccessToken(client_id, client_secret, "http://api.microsofttranslator.com", "client_credentials", new retrofit.Callback<MSAccessTokenClient.AccessTokenResult>(){

			@Override
			public void failure(RetrofitError arg0) {
				cb.onFailed(new TranslateError(arg0));
			}

			@Override
			public void success(AccessTokenResult arg0, Response arg1) {
				cb.onSuccess(arg0.access_token, arg0.expires_in);
			}
			
		});
	}
}
