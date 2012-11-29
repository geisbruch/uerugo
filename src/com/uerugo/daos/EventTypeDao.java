package com.uerugo.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uerugo.EMF;
import com.uerugo.model.Type;

public class EventTypeDao {

	public List<Type> createAndGetTypes(List<String> hashtags) throws SQLException{
		StringBuilder builder = new StringBuilder();
		try {
			builder.append("INSERT IGNORE INTO Type (hashtag) values ");
			StringBuilder vals = new StringBuilder();
			for(int i = 0; i<hashtags.size(); i++){
				vals.append(",(?)");
			}
			builder.append(vals.substring(1));
			
			PreparedStatement stm = EMF.getConnection().prepareStatement(builder.toString());
		
			for(int i=0; i< hashtags.size(); i++)
				stm.setString(i+1, hashtags.get(i));
		
			stm.execute();
			
			return getTypes(hashtags);
		} catch (SQLException e) {
			throw e;
		}
			
	}

	private List<Type> getTypes(List<String> hashtags) throws SQLException {
		StringBuilder in = new StringBuilder();
		for(int i = 0; i<hashtags.size(); i++){
			in.append(",?");
		}
		StringBuilder query = new StringBuilder("SELECT idtype,hashtag,description FROM Type WHERE hashtag IN (");
		query.append(in.substring(1));
		query.append(")");
		try{
			PreparedStatement stm = EMF.getConnection().prepareStatement(query.toString());
			for(int i=0; i< hashtags.size(); i++)
				stm.setString(i+1, hashtags.get(i));
			
			ResultSet rs = stm.executeQuery();
			EMF.getConnection().commit();
			List<Type> result = new ArrayList<Type>(); 
			while(rs.next()){
				Type t = new Type(rs.getString("hashtag"),rs.getString("description"));
				t.setId(rs.getInt("idtype"));
				result.add(t);
			}
			return result;
		}catch(SQLException e){
			EMF.getConnection().rollback();
			throw e;
		}
	}
}
