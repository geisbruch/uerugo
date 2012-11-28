package com.uerugo.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public abstract class Controller extends HttpServlet {

	protected void returnError(String error,HttpServletResponse response) throws IOException{
		response.getOutputStream().write(("{\"err\":\""+error+"\"}").getBytes());
		response.setStatus(400);
	}
	
}
