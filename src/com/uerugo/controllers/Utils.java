package com.uerugo.controllers;

import java.io.BufferedReader;

import javax.servlet.ServletInputStream;


public class Utils {

	public static String getBodyAsString(BufferedReader reader) throws Exception {
		if(reader == null)
			throw new Exception("Error reading inputStream");
		
		char[] buf = new char[4 * 1024]; // 4 KB char buffer
		int len;
		StringBuilder out = new StringBuilder();
		while ((len = reader.read(buf, 0, buf.length)) != -1) {
		 out.append(buf, 0, len);
		}
		return out.toString();
	}

}
