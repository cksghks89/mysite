package com.poscodx.mysite.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebUtil {
	public static String encodeURL(String keyword, String charset) {
		try {
			return URLEncoder.encode(keyword, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
