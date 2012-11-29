package com.uerugo.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

public class Type {

	Integer id;
	
	String hashtag;
	
	String description;
	
	public Type(String hashtag, String description) {
		super();
		this.hashtag = hashtag;
		this.description = description;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
