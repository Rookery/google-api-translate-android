/**
 * 
 */
package com.rookery.web_api_translate;

import retrofit.RetrofitError;
import retrofit.client.Response;

import com.rookery.web_api_translate.GoogleTranslateClient.TransResult;
import com.rookery.web_api_translate.GoogleTranslateClient.TranslateOp;
import com.rookery.web_api_translate.type.Language;
import com.rookery.web_api_translate.type.TranslateError;

/**
 * @author simsun
 *
 */
public class GoogleTranslator {
	
	private static GoogleTranslator translator;
	private TranslateOp translate_client;
	private static boolean debug_flag = true;
	private GoogleTranslator() {
		if (translate_client == null) {
			translate_client = GoogleTranslateClient.build_v2_client(debug_flag);
		}
	}
	/**
	 * default is disbale blog.
	 * if u want to enable log , u should invoke this function before getInstance().
	 */
	public static void enableLog() {
		debug_flag = true;
	}
	
	public static GoogleTranslator getInstance() {
		synchronized (GoogleTranslator.class) {
			if (translator == null) {
				translator = new GoogleTranslator();
			}	
		}
		return translator;		
	}
	
	public abstract interface Callback {
		public abstract void onSuccess(Language detected_lang, String translated_text);
		public abstract void onFailed(TranslateError e);
	}
	
	/**
	 * execute translation
	 * @param text the text u wonder to translated.
	 * @param dest_lang destination language.
	 * @param api_key api_key
	 * @param cb callback for get execute result
	 */
	public void execute(String text, Language dest_lang, String api_key, final Callback cb) {
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
		translate_client.getTranslation(api_key, dest_lang_str, text, new retrofit.Callback<GoogleTranslateClient.TransResult>() {
			
			@Override
			public void success(TransResult result, Response response) {
				for (GoogleTranslateClient.TransEntity entity : result.get_entities()) {
					cb.onSuccess(Language.fromString(entity.get_source_language()), entity.get_translated_text());
				}						
			}
			
			@Override
			public void failure(RetrofitError e) {
				cb.onFailed(new TranslateError(e));
			}					
		});
	} 
}
