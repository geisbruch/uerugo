package com.uerugo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.appengine.api.rdbms.AppEngineDriver;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static Connection con;

	static {
    	try {
			DriverManager.registerDriver(new AppEngineDriver());
			con = DriverManager.getConnection("jdbc:google:rdbms://uerugo-mysql:uerugo-main/uerugo");
			con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
	public static Connection getConnection(){
		return con;
	}
	
	public static Statement getStatement() throws SQLException{
		return getConnection().createStatement();
	}
	
	public static PreparedStatement getStatement(String query) throws SQLException{
		return getConnection().prepareStatement(query);
	}
}