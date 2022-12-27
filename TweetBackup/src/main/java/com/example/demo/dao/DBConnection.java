package com.example.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.*;

public class DBConnection {
	Connection connection;
	String url,name, pass;
	
	public DBConnection() {
		url="jdbc:mysql://localhost:3306/Archive";
		name= "root";
		pass = "";
	}
	
	public Connection getConnection() throws Exception{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection=DriverManager.getConnection(url, name, pass);
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			
		}
		
		return connection;
	}
}
