package com.uerugo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uerugo.daos.EventDao;
import com.uerugo.daos.UserDao;
import com.uerugo.model.Dates;
import com.uerugo.model.Event;
import com.uerugo.model.Location;
import com.uerugo.model.Type;
import com.uerugo.model.User;

@SuppressWarnings("serial")
public class UerugoServletControler extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	
			
			try {
				new UserDao().createUser(new User("test","test","test","test","test"));
				List<Type> types = new ArrayList<Type>();
				types.add(new Type("test1", "dsa"));
				types.add(new Type("test2", "dsa"));
				List<Dates> dates = new ArrayList<Dates>();
				dates.add(new Dates(Calendar.getInstance().getTime(),Calendar.getInstance().getTime()));
				Event e = new Event("testEvent","test",new Location(null, 0f, 0f, "ala"),"un evento loco",0f,types,dates);
				new EventDao().createEvent(e);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
