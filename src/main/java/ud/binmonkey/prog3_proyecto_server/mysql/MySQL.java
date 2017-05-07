package ud.binmonkey.prog3_proyecto_server.mysql;


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;

import java.sql.*;

public class MySQL {

    public Object close;
    String username;
    String password;
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

    /**
     * Deletes all data from the DB
     *
     * @throws SQLException If the database is not properly created
     */
    public void clearDB() throws SQLException {
        statement.executeUpdate("DELETE FROM choques");
        statement.executeUpdate("DELETE FROM contador");
    }

    /* Utility Methods */
    private void readConfig() {

        NodeList nList = DocumentReader.getDoc("conf/MySQLServer.xml").getElementsByTagName("mysql-server");
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;


        username = eElement.getElementsByTagName("username").item(0).getTextContent();
        password = eElement.getElementsByTagName("password").item(0).getTextContent();
    }

    public void startSession() {
        // Loads MySQL driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Starts connection
        try {
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/dwh?"
                            + "user=" + username + "&password=" + password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
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
        } catch (Exception ignored) {
        }
    }
}
