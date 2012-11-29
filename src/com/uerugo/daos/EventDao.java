package com.uerugo.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.uerugo.EMF;
import com.uerugo.model.Dates;
import com.uerugo.model.Event;
import com.uerugo.model.Location;
import com.uerugo.model.Type;
import com.uerugo.model.User;

public class EventDao {
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void createEvent(Event e) throws SQLException{
		Connection con = EMF.getConnection();
		PreparedStatement typesStm = null;
		PreparedStatement datesStm = null;
		PreparedStatement eventsStm = null;
		try {
			
			UserDao userDao = new UserDao();
			EventTypeDao eventTypeDao = new EventTypeDao();
			User user = userDao.getUserByUsername(e.getPublisher());
			con.setAutoCommit(false);

			StringBuilder eventsQuery = 
					new StringBuilder("INSERT INTO Event (name, description, price," +
							"User_iduser, User_username, Location_idlocation) VALUES (?,?,?,?,?,?)");
			StringBuilder datesQuery = new StringBuilder("INSERT INTO Dates (Event_idevent, `from`, `to`) VALUES ");
			StringBuilder typesQuery = new StringBuilder("INSERT INTO Type_Event (Type_idtype, Event_idevent) VALUES ");
			
			StringBuilder helper = new StringBuilder();
			for(Integer i = 0; i < e.getDates().size(); i++)
				helper.append(", (?,?,?)");
			datesQuery.append(helper.substring(1));
			
			helper = new StringBuilder();
			
			List<String> hashtags = new ArrayList<String>();
			
			
			for(Integer i = 0; i < e.getTypes().size(); i++){
				helper.append(", (?,?)");
				hashtags.add(e.getTypes().get(i).getHashtag());
			}
			
			e.setTypes(eventTypeDao.createAndGetTypes(hashtags));
			
			typesQuery.append(helper.substring(1));

			if(e.getLocation().getId() == null){
				e.setLocation(new LocationsDao().createLocation(e.getLocation()));
			}

			int i = 1;
			eventsStm = EMF.getStatement(eventsQuery.toString());
			eventsStm.setString(i++, e.getName());
			eventsStm.setString(i++, e.getDescription());
			eventsStm.setFloat(i++, e.getPrice());
			eventsStm.setInt(i++, user.getId());
			eventsStm.setString(i++, user.getUserName());
			eventsStm.setInt(i++, e.getLocation().getId());
			
			eventsStm.execute();
			ResultSet rs = eventsStm.getGeneratedKeys();
			if(!rs.next())
				throw new SQLException("Error doing insert of event ["+e.toString()+"]");
			e.setId(rs.getInt(1));
			
			datesStm = EMF.getStatement(datesQuery.toString());
			int counter = 0;
			for(int j = 0; j < e.getDates().size(); j++){
				Dates d = e.getDates().get(j);
				datesStm.setInt(++counter, e.getId());
				datesStm.setString(++counter, sf.format(d.getFrom()));
				datesStm.setString(++counter, sf.format(d.getTo()));
				
			}
			datesStm.execute();
			
			typesStm = EMF.getStatement(typesQuery.toString());
			counter = 1;
			for(int j = 0; j < e.getTypes().size(); j++){
				Type t = e.getTypes().get(j);
				typesStm.setInt(counter++, t.getId());
				typesStm.setInt(counter++, e.getId());
			}
			typesStm.execute();
			
			con.commit();
		} catch (SQLException ex) {
			con.rollback();
			throw ex;
		}finally{
			if(typesStm!=null)
				typesStm.close();
			if(datesStm!=null)
				datesStm.close();
			if(eventsStm!=null)
				eventsStm.close();
			
		}
	}
	
	public List<Event> getEvents(Float northLatitude, 
			Float northLongitude, 
			Float southLatitude, 
			Float southLongitude,
			Date startDate,
			Date endDate) throws SQLException{
		
		if(startDate == null){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			startDate = cal.getTime(); 
		}
		
		if(endDate == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 3);
			endDate = cal.getTime();
		}
		
		StringBuilder query = new StringBuilder(
				"SELECT e.idevent as idevent, e.name as name, e.description as description, e.price as price, " +
				"e.User_username as username, " +
				"l.idlocation as idlocation,l.latitude as latitude, l.longitude as longitude, l.place as place " +
				"FROM Event e " +
				"INNER JOIN Location l ON e.Location_idlocation = l.idlocation " +
				"INNER JOIN Dates d ON d.Event_idevent = e.idevent " +
				"WHERE l.latitude BETWEEN ? AND ? and l.longitude BETWEEN ? AND ? " +
				"AND d.`from` >= ? AND d.`to` <= ? ");
		
		try{
			PreparedStatement stm = EMF.getStatement(query.toString());
			int i = 0;
			stm.setFloat(++i, southLatitude);
			stm.setFloat(++i, northLatitude);
			stm.setFloat(++i, southLongitude);
			stm.setFloat(++i, northLongitude);
			stm.setString(++i, sf.format(startDate));
			stm.setString(++i, sf.format(endDate.getTime()));
			ResultSet rs = stm.executeQuery();
			List<Event> events = new ArrayList<Event>();
			while(rs.next()){
				Location loc = new Location(rs.getInt("idlocation"),
						rs.getFloat("latitude"),
						rs.getFloat("longitude"),
						rs.getString("place"));
				Event e = new Event(rs.getString("name"),
						rs.getString("username"),
						loc,
						rs.getString("description"),
						rs.getFloat("price"),null,null);
				e.setId(rs.getInt("idevent"));
				//TODO: get types and get dates;
				events.add(e);
			}
			stm.close();
			return events;
		}catch(SQLException e){
			throw e; 
		}
		
	}
}
