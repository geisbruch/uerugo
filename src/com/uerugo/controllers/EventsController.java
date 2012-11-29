package com.uerugo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.uerugo.daos.EventDao;
import com.uerugo.model.Dates;
import com.uerugo.model.Event;
import com.uerugo.model.Location;
import com.uerugo.model.Type;

public class EventsController extends Controller{
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				dates.add(new Dates(sf.parse(da.getString("from")),sf.parse(da.getString("to"))));
			}
			
			if(dates.size()<=0){
				returnError("The event should contains at leat one date", resp);
				return;
			}
			
			Float price = 0f;
			
			if(o.has("price"))
				price = Float.parseFloat(o.get("price").toString());
			
			List<Type> types = new ArrayList<Type>();
			if(o.has("types")){
				JSONArray tArr = o.getJSONArray("types");
				for(Integer i = 0; i < tArr.length(); i++){
					String t = tArr.get(i).toString();
					types.add(new Type(t, null));
				}
			}
			
			Integer locId = null;
			if(loc.has("id"))
				locId = loc.getInt("id");
			Location location = new Location(locId,
					Float.parseFloat(loc.get("latitude").toString()),
					Float.parseFloat(loc.get("longitude").toString()), null);
			
			Event e = new Event(name, 
					publisher, 
					location,
					description, 
					price, 
					types, 
					dates);
			new EventDao().createEvent(e);
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
			List<Event> events = null;
			try {
				events = new EventDao().getEvents(Float.parseFloat(req.getParameter("northeastLatitude")), 
						Float.parseFloat(req.getParameter("northeastLongitude")), 
						Float.parseFloat(req.getParameter("southwestLatitude")), 
						Float.parseFloat(req.getParameter("southwestLongitude")), 
						null, null);
			} catch (Exception e) {
				e.printStackTrace();
				returnError("Error getting events ["+e.getMessage()+"]", resp);
				return;
			}

			JSONArray a = new JSONArray();
			for(Event event : events){
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


}
