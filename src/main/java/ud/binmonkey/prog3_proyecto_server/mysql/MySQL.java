package ud.binmonkey.prog3_proyecto_server.mysql;


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;

import java.sql.*;

public class MySQL {

    private String username;
    private String password;

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    /**
     * Constructor for the class MySQL
     */
    public MySQL() {
        readConfig();
        startSession();
    }

    /* Server Utility Methods */

    /**
     * Deletes all data from the DB
     *
     * @throws SQLException If the database is not properly created
     */
    public void clearDB() throws SQLException {
        statement.executeUpdate("DELETE FROM neo4j_titles");
        statement.executeUpdate("DELETE FROM neo4j_genres");
    }

    private void readConfig() {

        NodeList nList = DocumentReader.getDoc("conf/MySQLServer.xml").getElementsByTagName("mysql-server");
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;


        username = eElement.getElementsByTagName("username").item(0).getTextContent();
        password = eElement.getElementsByTagName("password").item(0).getTextContent();
    }

    public void startSession() {
        try {
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/dwh?"
                            + "user=" + username + "&password=" + password);

            statement = connect.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeSession() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* Server Utility Methods */

    /* MySQL Methods */

    public Statement getStatement() {
        return statement;
    }

    /**
     * Adds 1 to a counter in counter table
     *
     * @param table - Table where the counter is located
     * @param name  - Name of the counter
     * @throws SQLException - If MySQL DB is not created properly
     */
    public void updateCounter(String table, String name) throws SQLException {
        int contador;

        try {
            resultSet = statement.executeQuery("SELECT COUNT FROM " + table + " WHERE NAME='" + name + "'");

            resultSet.next();
            contador = resultSet.getInt("COUNT") + 1;

            statement.executeUpdate("UPDATE " + table + " SET COUNT=" + contador +
                    " WHERE NAME='" + name + "';");

        } catch (SQLException e) {
            statement.executeUpdate("INSERT INTO " + table + " VALUES ('" + name +
                    "'," + 1 + ");");
        }
    }
    /* END MySQL Methods */
}
