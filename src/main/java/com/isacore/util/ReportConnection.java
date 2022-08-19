package com.isacore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ReportConnection {

	public static Connection getConectionISA() {
		try {
			return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=isa;user=sa;password=sqlserver");
		} catch (SQLException e) {
			return null;
		}
	}
	/*
	public static DriverConnectionProvider ConnectionPentaho() {
		
		final DriverConnectionProvider sampleDriverConnectionProvider = new DriverConnectionProvider();
        sampleDriverConnectionProvider.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //sampleDriverConnectionProvider.setUrl("jdbc:sqlserver://192.168.4.18:1433;databaseName=isa;user=sa;password=grupoempresarialsrs");
        sampleDriverConnectionProvider.setUrl("jdbc:sqlserver://localhost:1433;databaseName=isa;user=sa;password=sqlserver");
        
        return sampleDriverConnectionProvider;
	}*/
	
}
