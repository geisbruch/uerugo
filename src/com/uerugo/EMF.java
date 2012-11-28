package com.uerugo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.rdbms.AppEngineDriver;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
    static {
    	try {
			DriverManager.registerDriver(new AppEngineDriver());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	public static Connection getConnection(){
		try {
			return DriverManager.getConnection("jdbc:google:rdbms://uerugo-mysql:uerugo-main/uerugo");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}