/**
 * 
 */
package com.rookery.google_api_translate;

import retrofit.RetrofitError;
import retrofit.client.Response;

import com.rookery.google_api_translate.TranslateClient.TransResult;
import com.rookery.google_api_translate.TranslateClient.TranslateOp;
import com.rookery.google_api_translate.type.Language;
import com.rookery.google_api_translate.type.TranslateError;

/**
 * @author simsun
 *
 */
public class Translator {
	
	private static Translator translator;
	private TranslateOp translate_client;
	private static boolean debug_flag = false;
	private Translator() {
		if (translate_client == null) {
			translate_client = TranslateClient.build_v2_client(true);
		}
	}
	/**
	 * default is disbale blog.
	 * if u want to enable log , u should invoke this function before getInstance().
	 */
	public static void enableLog() {
		debug_flag = true;
	}
	
	public static Translator getInstance() {
		synchronized (Translator.class) {
			if (translator == null) {
				translator = new Translator();
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
		translate_client.getTranslation(api_key, dest_lang.toString(), text, new retrofit.Callback<TranslateClient.TransResult>() {
			
			@Override
			public void success(TransResult result, Response response) {
				for (TranslateClient.TransEntity entity : result.get_entities()) {
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
