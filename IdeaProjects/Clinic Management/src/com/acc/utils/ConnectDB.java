package com.acc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

interface ConnectionData{
       String url = "jdbc:mysql://127.0.0.1:3306/clinic";
       String username = "root";
       String password = "123456";
}
public class ConnectDB {

    public static Connection getDBConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
           System.out.println(e.getMessage());
        }
        try{
            return DriverManager.getConnection(ConnectionData.url, ConnectionData.username, ConnectionData.password);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
