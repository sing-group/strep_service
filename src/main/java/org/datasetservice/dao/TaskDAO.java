package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.datasetservice.domain.Task;

public class TaskDAO {

    private final String URL = "jdbc:mysql://localhost:3306/onlinepreprocessor";

    private final String USER = "springuser";

    private final String PASSWORD = "springpassword";

    public Task getTask(Long id)
    {
        Task task = null;

        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM task where id=?")
        )
        {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next())
            {
                task = new Task(rs.getLong(1), null, rs.getString(3), rs.getString(2));
            }

            
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.toString());
        }
        
        return task;
    }

    public void changeState(String message, String state, Long id)
    {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE task SET message = ?, state = ? where id = ?"))
        {
            preparedStatement.setString(1, message);
            preparedStatement.setString(2, state);
            preparedStatement.setLong(3, id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.toString());
        }
    } 
    
}