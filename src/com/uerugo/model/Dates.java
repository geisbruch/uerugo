package com.uerugo.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Dates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Key key;
	
	@Basic
	Date from;
	
	@Basic
	Date to;

	public Dates(Date from, Date to) {
		super();
		this.from = from;
		this.to = to;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}
	
	
}
