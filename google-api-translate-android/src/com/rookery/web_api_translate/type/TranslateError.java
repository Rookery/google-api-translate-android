/**
 * 
 */
package com.rookery.web_api_translate.type;

/**
 * @author simsun
 *
 */
public class TranslateError extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 221843528590808183L;

	public TranslateError(final String msg) {
		super(msg);
	}
	
	public TranslateError(final Exception e) {
		super(e);
	}
}
