package com.uerugo.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Controller extends HttpServlet {

	protected void returnError(String error,HttpServletResponse response) throws IOException{
		response.getOutputStream().write(("{\"err\":\""+error+"\"}").getBytes());
		response.setStatus(400);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		resp.setContentType("application/json");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		resp.setHeader("Access-Control-Allow-Credentials", "true");
	}
	
}
