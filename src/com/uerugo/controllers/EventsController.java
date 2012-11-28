package com.uerugo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.datanucleus.api.jpa.criteria.CriteriaBuilderImpl;
import org.mortbay.util.ajax.JSONObjectConvertor;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.labs.repackaged.org.json.JSONStringer;
import com.uerugo.EMF;
import com.uerugo.model.Dates;
import com.uerugo.model.Event;
import com.uerugo.model.Location;
import com.uerugo.model.Type;

public class EventsController extends Controller{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("application/json");
		try{
			String json = Utils.getBodyAsString(req.getReader());
		
			JSONObject o = new JSONObject(json);
		
			if(!o.has("publisher") || !o.has("location") 
					|| !o.has("name") || !o.has("description")
					|| !o.has("dates")){
				returnError("Should contains all paramters (publisher, location, name, description, dates)", resp);
				return;
			}
			String publisher = o.getString("publisher");
			String name = o.getString("name");
			String description = o.getString("description");
			
			JSONObject loc = o.getJSONObject("location");
			if(!loc.has("latitude") || !loc.has("longitude")){
				returnError("Location should contain longitude and latiude ", resp);
				return;
			}
			
			JSONArray dateArray = o.getJSONArray("dates");
			List<Dates> dates = new ArrayList<Dates>();
			
			for(Integer i = 0; i< dateArray.length(); i++){
				JSONObject da = dateArray.getJSONObject(i);
				if(!da.has("from") || !da.has("to")){
					returnError("Error date should contan from and to fields",  resp);
					return;
				}
				dates.add(new Dates(new Date(Date.parse(da.getString("from"))),new Date(Date.parse(da.getString("to")))));
			}
			
			if(dates.size()<=0){
				returnError("The event should contains at leat one date", resp);
				return;
			}
			
			Location location = new Location(Float.parseFloat(loc.get("latitude").toString()), Float.parseFloat(loc.get("longitude").toString()));
			
			Float price = 0f;
			
			if(o.has("price"))
				price = Float.parseFloat(o.get("price").toString());
			
			Set<Type> types = new HashSet<Type>();
			if(o.has("types")){
				JSONArray tArr = o.getJSONArray("types");
				for(Integer i = 0; i < tArr.length(); i++){
					String t = tArr.get(i).toString();
					types.add(new Type(t, null));
				}
			}
			
			Event e = new Event(name, publisher, location, description, price, types, dates);
			
			EntityManager em = EMF.get().createEntityManager();
			em.persist(e);
			em.close();
			resp.setStatus(201);

		}catch(Exception e){
			returnError("Error: "+e.getMessage(), resp);
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(req.getParameter("northeastLongitude") != null && 
				req.getParameter("northeastLatitude") != null &&
				req.getParameter("southwestLongitude") != null &&
				req.getParameter("southwestLatitude") != null){
			List<Event> e = getEventsByLocation(Float.parseFloat(req.getParameter("northeastLongitude")),
											Float.parseFloat(req.getParameter("northeastLatitude")),
											Float.parseFloat(req.getParameter("southwestLongitude")),
											Float.parseFloat(req.getParameter("southwestLatitude")));
			JSONArray a = new JSONArray();
			for(Event event : e){
				try {
					a.put(event.toJSON());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			resp.getOutputStream().write(a.toString().getBytes());
			resp.setStatus(200);
		}
	}

	private List<Event> getEventsByLocation(Float northeastLongitude,
			Float northeastLatitude, Float southwestLongitude, Float southwestLatitude) {
		EntityManager em = EMF.get().createEntityManager();
		Query q = em.createQuery("SELECT e FROM Event e");
		Iterator res = q.getResultList().iterator();
		List<Event> events = new  ArrayList<Event>();
		while(res.hasNext()){
			Event e = (Event)res.next();
			Location l = e.getLocation();
			try{
			if(l.getLatitude() >= southwestLatitude && 
					l.getLatitude()<=northeastLatitude &&
					l.getLongitude() <= northeastLongitude &&
					l.getLongitude() >= southwestLongitude)
				events.add(e);
			}catch(NullPointerException ex){
			
			}
		}
		return events;
	}
}
