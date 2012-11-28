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
			
			EntityManager em = EMF.get().createEntityManager();
			Query q = em.createQuery("Select p FROM User p where p.userName='"+userName+"'");
			if(q.getResultList().size() > 0){
				resp.getOutputStream().write("{\"err\":\"User already exists\"}".getBytes());
				resp.setStatus(400);
				return;
			}
			em.persist(u);
			em.close();
			resp.setStatus(201);
		}catch(Exception e){
			
		}
	}
}
