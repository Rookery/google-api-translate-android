google-api-translate-android
============================

Provides a simple, unofficial, Android client API for using Google Translate. 

****
# How to use#

```
Translator.getInstance().execute("hello,every one.my name is simsun", Language.CHINESE_SIMPLIFIED, API_KEY, new Translator.Callback() {
					
					@Override
					public void onSuccess(Language detected_lang, String translated_text) {
						Log.d(TAG, "onSuccess: language:" + detected_lang.toString() + "\ttext:" + translated_text);
					}
					
					@Override
					public void onFailed(TranslateError e) {
						e.printStackTrace();
					}
				});

```


****

# TODO#

1. import okhttp & restrofit with maven
2. feature interface
3. change access tonken for ms module.
