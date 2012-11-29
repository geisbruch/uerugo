package com.uerugo.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.mortbay.util.ajax.JSON;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@Entity
public class Event {
	
	Integer id;
	
	String publisherUserName;
	
	Location location;
	
	String name;
	
	String description;
	
	Float price = null;
	
	List<Type> types;
	
	List<Dates> dates;
	
	public Event(String name, String publisher,Location location, String description,
			Float price, List<Type> types, List<Dates> dates) {
		super();
		this.name = name;
		this.location = location;
		this.publisherUserName = publisher;
		this.description = description;
		this.price = price;
		this.types = types;
		this.dates = dates;
	}

	
	public String getPublisherUserName() {
		return publisherUserName;
	}


	public void setPublisherUserName(String publisherUserName) {
		this.publisherUserName = publisherUserName;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPublisher() {
		return publisherUserName;
	}

	public void setPublisher(String publisher) {
		this.publisherUserName = publisher;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public List<Dates> getDates() {
		return dates;
	}

	public void setDates(List<Dates> dates) {
		this.dates = dates;
	}

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	
	public JSONObject toJSON() throws Exception{
		JSONObject o = new JSONObject();
		try {
			if(this.id != null)
				o.put("id", this.id);
			o.put("description", this.getDescription());
			o.put("name",this.getName());
			o.put("publisher",this.getPublisherUserName());
			if(this.getPrice() != null & this.getPrice()>=0)
				o.put("price", this.getPrice());
			JSONObject location = new JSONObject();
			if(this.location.getId() != null)
				location.put("id", this.location.getId());
			if(this.location.getPlace() != null)
				location.put("place", this.location.getPlace());
			location.put("latitude", this.location.getLatitude());
			location.put("longitude",this.location.getLongitude());
			o.put("location",location);
			JSONArray dates = new JSONArray();
			if(this.dates != null){
				for(Dates date : this.dates){
					JSONObject dateJSON = new JSONObject();
					dateJSON.put("from", date.getFrom());
					dateJSON.put("to", date.getTo());
					dates.put(dateJSON);
				}
			}
			o.put("dates", dates);
		}catch(Exception ex){
			throw ex;
		}
		return o;
	}
	
	
	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	@Override
	public String toString() {
		try {
			return this.toJSON().toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
