package javaapplication1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Dao {

	// Code database URL
	static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false";
	// Database credentials
	static final String USER = "fp411", PASS = "411";

	public Connection connect() throws SQLException {

		return DriverManager.getConnection(DB_URL, USER, PASS);

	}
	
	static Connection connect = null;
	Statement statement = null;
	
	public void createTable() {
		 try {
		 
		statement = connect().createStatement();
	
			String sql = "CREATE TABLE YChen_ticket" + 
			             "(tic_num INTEGER not NULL AUTO_INCREMENT, " +
			  	       "userid VARCHAR(20), " +
					 " tic_desc VARCHAR(100), " + 
					 " tic_status VARCHAR(20), " + 
					 " startdate DATE, " + 
					 " enddate DATE, " +
					 " tic_reply VARCHAR(100), " + 
				 " PRIMARY KEY ( tic_num ))";
	
		statement.executeUpdate(sql);
		
		String sql2 = "CREATE TABLE YChen_users" + 
		             "(userid VARCHAR(20) not NULL, " +
		  	       " passw VARCHAR(20), " +
		           " admin BOOLEAN, " + 
			 " PRIMARY KEY ( userid ))";

		statement.executeUpdate(sql2);
		
		connect().close(); //close db connection 
		} catch (SQLException se) {
		// Handle errors for JDBC
		 se.printStackTrace();
		}
	}
	
	//CRUD
	
	//Insert a ticket
	public void insertTicket(String userid, String t_Desc) {
		try {
			statement = connect().createStatement();
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
			statement.executeUpdate("Insert into ychen_ticket" + 
					"(userid, tic_desc, startdate, tic_status) values(" +
					" '" + userid + "','" + t_Desc + "','" +
					timeStamp  + "','" + "OPEN" + "')");
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//View all ticket for JTable
	public ResultSet readTickets(String userid) {

		ResultSet results = null;
		try {
			statement = connect().createStatement();
			results = statement.executeQuery("SELECT * FROM ychen_ticket WHERE userid = " + "'" + userid + "'");
			connect().close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	
	//Update a ticket
	public void updateTicket(String uid, int tid, String nDesc) {
		try {
			statement = connect().createStatement();
			statement.executeUpdate("UPDATE ychen_ticket SET tic_desc = " + 
			"'" + nDesc + "'" + " WHERE tic_num = " + tid +
			" AND userid = " + "'" + uid + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Delete a ticket
	public void deleteTicket(String uid, int tid) {
		try {
			statement = connect().createStatement();
			statement.executeUpdate("DELETE FROM ychen_ticket WHERE userid = " + 
			"'" + uid + "'" + " AND tic_num = " + tid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Read a specific ticket.
	public String readTicket(String uid, int tid) {
		ResultSet resultSet = null;
		String tdesc = null;
		String sTime = null;
	    String eTime = null;
		String status= null;
		try {
			statement = connect().createStatement();
			resultSet = statement.executeQuery("SELECT * FROM ychen_ticket WHERE userid = " + 
			"'" + uid + "'" + " AND tic_num = " + tid);
			while(resultSet.next()) {
				tdesc = resultSet.getString("tic_desc");
				sTime = resultSet.getString("startdate");
			    eTime= resultSet.getString("enddate");
				status= resultSet.getString("tic_status");
			}
			
			//Construct output for dialog
			String result = "Ticket: " + tid + "\nDescription:" + tdesc + "\nStart Time: " + sTime + "End Time: " + eTime + "\nStatus: " + status;
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Could not read";
	}
}
