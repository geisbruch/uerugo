package com.uerugo.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.uerugo.EMF;
import com.uerugo.model.Location;

public class LocationsDao {
	
	private PreparedStatement query;

	public Location createLocation(Location location) throws SQLException{
		StringBuilder builder = new StringBuilder();
		try {
			builder.append("INSERT INTO Location (latitude, longitude, place) values (?,?,?)");
			query = EMF.getStatement(builder.toString());
			int i = 0;
			query.setFloat(++i, location.getLatitude());
			query.setFloat(++i, location.getLongitude());
			query.setString(++i, location.getPlace());
			
			query.execute();
			ResultSet rs = query.getGeneratedKeys();
			if(!rs.next())
				throw new SQLException("Error creating location");
			location.setId(rs.getInt(1));
			return location;
		}catch(SQLException e){
			throw e;
		}
	}
}
