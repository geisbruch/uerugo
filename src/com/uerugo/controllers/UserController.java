package com.uerugo.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.uerugo.EMF;
import com.uerugo.daos.UserDao;
import com.uerugo.model.User;

public class UserController extends Controller {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try{
			
			resp.setContentType("application/json");
			String json = Utils.getBodyAsString(req.getReader());
			
			JSONObject o = new JSONObject(json);
			
			if(!o.has("email") || !o.has("userName") || !o.has("password") || !o.has("lastName")){
				returnError("Should contains all paramters", resp);
				return;
			}
			String email = o.getString("email");
			String name = o.getString("name");
			String userName = o.getString("userName");
			String password = o.getString("password");
			String lastName = o.getString("lastName");
			User u = new User(email,userName,password,name,lastName);
			new UserDao().createUser(u);
			resp.setStatus(201);
		}catch(Exception e){
			e.printStackTrace();
			returnError("Error creating user ["+e.getMessage()+"]", resp);
		}
	}
}
