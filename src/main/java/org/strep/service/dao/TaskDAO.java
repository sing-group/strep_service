package org.strep.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.strep.service.domain.Task;

/**
 * Data access object for task objects
 */
public class TaskDAO {
    /**
     * For logging purposes
     */
    private static final Logger logger = LogManager.getLogger(TaskDAO.class);

    /**
     * A constructor for create instances of TaskDAO
     */
    public TaskDAO() {
    }

    /**
     * Return the specified task
     *
     * @param id the id of the task
     * @return the specified task
     */
    public Task getTask(Long id) {
        Task task = null;

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, message, state FROM task where id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                task = new Task(rs.getLong(1), null, rs.getString(3), rs.getString(2));
            }

        } catch (SQLException sqlException) {
            logger.warn("[ERROR getTask]: " + sqlException.getMessage());
        }
        return task;
    }

    /**
     * Change the state of the task
     *
     * @param message a information message for failed tasks
     * @param state the state of the task: waiting, executing, failed, success
     * @param id the id of the task
     */
    public void changeState(String message, String state, Long id) {
        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE task SET message = ?, state = ? WHERE id = ?")) {
            preparedStatement.setString(1, message);
            preparedStatement.setString(2, state);
            preparedStatement.setLong(3, id);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.warn("[ERROR changeState]: " + sqlException.getMessage());
        }
    }
}
