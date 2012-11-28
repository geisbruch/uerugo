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
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Key key;
	
	@Basic 
	String publisherUserName;
	
	@Embedded  
	Location location;
	
	@Basic
	String name;
	
	@Basic
	String description;
	
	@Basic
	Float price = null;
	
	@OneToMany(cascade = CascadeType.ALL) 
	Set<Type> types;
	
	@OneToMany(cascade = CascadeType.ALL) 
	List<Dates> dates;
	
	public Event(String name, String publisher, Location location, String description,
			Float price, Set<Type> types, List<Dates> dates) {
		super();
		this.name = name;
		this.publisherUserName = publisher;
		this.location = location;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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

	public Set<Type> getTypes() {
		return types;
	}

	public void setTypes(Set<Type> types) {
		this.types = types;
	}

	public List<Dates> getDates() {
		return dates;
	}

	public void setDates(List<Dates> dates) {
		this.dates = dates;
	}

	public Key getKey() {
		return key;
	}
	
	public JSONObject toJSON() throws Exception{
		JSONObject o = new JSONObject();
		try {
			o.put("description", this.getDescription());
			o.put("name",this.getName());
			o.put("publisher",this.getPublisherUserName());
			if(this.getPrice() != null & this.getPrice()>=0)
				o.put("price", this.getPrice());
			JSONObject location = new JSONObject();
			location.put("latitude", this.getLocation().getLatitude());
			location.put("longitude",this.getLocation().getLongitude());
			o.put("location",location);
			JSONArray dates = new JSONArray();
			for(Dates date : this.dates){
				JSONObject dateJSON = new JSONObject();
				dateJSON.put("from", date.getFrom());
				dateJSON.put("to", date.getTo());
				dates.put(dateJSON);
			}
			o.put("dates", dates);
		}catch(Exception ex){
			throw ex;
		}
		return o;
	}
}
