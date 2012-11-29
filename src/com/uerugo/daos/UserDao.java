package com.uerugo.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import com.uerugo.EMF;
import com.uerugo.model.User;

public class UserDao {

	private static final Logger log = Logger.getLogger(UserDao.class.getName());

	public void createUser(User user) throws SQLException{
		try{
		String query = "INSERT INTO User (email, firstname, username, password, lastName) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = EMF.getConnection().prepareStatement(query);
		int i = 1;
		preparedStatement.setString(i++, user.getEmail());
		preparedStatement.setString(i++, user.getName());
		preparedStatement.setString(i++, user.getUserName());
		preparedStatement.setString(i++, user.getPassword());
		preparedStatement.setString(i++, user.getLastName());
		preparedStatement.execute();
		EMF.getConnection().commit();
		preparedStatement.close();
		}catch(SQLException e){
			EMF.getConnection().rollback();
			log.severe(e.getMessage());
			throw e;
		}
	}

	public User getUserByUsername(String publisher) throws SQLException {
		
		PreparedStatement stm = EMF.getStatement("SELECT iduser, email,firstname,username,password,lastname FROM User WHERE username = ?");
		try{
			stm.setString(1, publisher);
			ResultSet rs = stm.executeQuery();
			if(!rs.next())
				throw new SQLException("User ["+publisher+"] not found");
			User user = new User(rs.getString("email"), 
					rs.getString("username"), 
					rs.getString("password"), 
					rs.getString("firstname"), 
					rs.getString("lastname"));
			user.setId(rs.getInt("iduser"));
			stm.close();
			return user;
		}catch(SQLException ex){
			throw ex;
		}finally{
			try {
				stm.close();
			} catch (SQLException ex) {
				throw ex;
			}
		}
	}
}
