package org.strep.service.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.strep.service.domain.Dataset;
import org.strep.service.domain.TaskCreateSdataset;

/**
 * Data access object for system tasks
 */
public class TaskCreateSdatasetDAO {

    /**
     * The connection url to the database
     */
    private static final Logger logger = LogManager.getLogger(TaskCreateSdatasetDAO.class);

    /**
     * A constructor for create instances of DatasetDAO
     */
    public TaskCreateSdatasetDAO() {
    }

    /**
     * Return a list of the waiting system tasks
     *
     * @return a list of the waiting system tasks
     * @throws Exception
     */
    public ArrayList<TaskCreateSdataset> getWaitingSystemTasks() throws Exception {
        ArrayList<TaskCreateSdataset> waitingSystemTasks = new ArrayList<>();

        String query = "SELECT t.id, t.message, t.state FROM task_create_sdataset sd INNER JOIN task t ON t.id = sd.id WHERE t.state='waiting'";
        try (Connection connection = ConnectionPool.getDataSourceConnection();
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                DatasetDAO datasetDAO = new DatasetDAO();
                Dataset dataset = datasetDAO.getDatasetByTaskId(rs.getLong(1));

                if (dataset != null) {
                    TaskCreateSdataset taskCreateSdataset = new TaskCreateSdataset(rs.getLong(1), dataset, rs.getString(2), rs.getString(3));
                    waitingSystemTasks.add(taskCreateSdataset);
                }
            }
        } catch(Exception sqlException){
            logger.warn("[ERROR getWaitingSystemTasks]: " + sqlException.getMessage());
        }
        return waitingSystemTasks;
    }
}
