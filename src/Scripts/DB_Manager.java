/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author zmoe7
 */
public class DB_Manager {
    PreparedStatement newStatement,insertStatement,updateStatement;
    Connection connection;
    
    String dbUrl = "jdbc:mysql://localhost:3306/NB?";
    String user = "root";
    String password = "hailtoyou";
    String table = "NewHS";
   
    public DB_Manager() throws SQLException {
        connection = DriverManager.getConnection(dbUrl + "serverTimezone=UTC&user=" + user + "&password=" + password);
        String createQuery = "CREATE TABLE "+table+" (NAME varchar(200) not null, SCORE int(20))";
        newStatement = connection.prepareStatement(createQuery);
        String insertQuery = "INSERT INTO "+ table +" ( NAME, SCORE) VALUES (?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        String updatequery = "UPDATE " + table + " SET SCORE=(?) WHERE NAME=(?)";
        updateStatement = connection.prepareStatement(updatequery);
    }


    public ArrayList<PlayerData> fetchData() throws SQLException {
        String query = "SELECT * FROM "+table;
        ArrayList<PlayerData> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results;
        try{
            results = stmt.executeQuery(query);
        }
        catch(SQLException e){
            newStatement.executeUpdate();
            results = stmt.executeQuery(query);
        }
        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            highScores.add(new PlayerData(name, score));
        }
        sort(highScores);
        return highScores;
    }

    public void add(String name) throws SQLException {
        insertStatement.setString(1, name);
        insertStatement.setInt(2, 1);
        try{
            insertStatement.executeUpdate();
        }
        catch(SQLException sq){
            newStatement.executeUpdate();
            insertStatement.executeUpdate();
        }
    }

    public void increase(String user) throws SQLException {
        String query = "SELECT * FROM "+table;
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            if(user.equals(name)){
                update(name,score);
                break;
            }
        }
    }
    
    void update(String name,int score) throws SQLException {
        updateStatement.setInt(1, score+1);
        updateStatement.setString(2, name);
        updateStatement.executeUpdate();
    }
    
    void sort(ArrayList<PlayerData> highScores) {
        Collections.sort(highScores, new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData p1, PlayerData p2) {
                return p2.getScore() - p1.getScore();
            }
        });
    }
}
