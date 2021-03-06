package com.example.lab8;


import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class MySQLDataBase {

    /**
     * JDBC URL для базы данных на Localhost
     */
    private final String url;

    /**
     * Имя БД
     */
    private final String DBname;

    private PreparedStatement st;
    public Connection connection;
    //Phrases phrases;

    public MySQLDataBase(String DBname) {
        this.DBname = DBname;
        url = "jdbc:mysql://localhost:3306/" + DBname + "?serverTimezone=UTC";
        connection = null;
    }

    public String getDBname() {
        return DBname;
    }

    public boolean Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            Properties properties = new Properties();
            properties.setProperty("user", "test");
            properties.setProperty("password", "test");
            properties.setProperty("useSSL", "false");
            properties.setProperty("autoReconnect", "true");
            connection = getConnection(url, properties); // здесь осуществляется соединение c login и password
            this.executeQuery("use " + DBname + ";");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Выполнение запроса к БД без результирующего набора данных
     *
     * @param query запрос
     *
     * @throws SQLException Стандартный exception для работы с SQL
     */
    public boolean executeQuery(String query) throws SQLException {
        Statement st = connection.createStatement();
        return st.execute(query);
    }
    public int executeUpdate(String query, int count, Phrases phrases) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, phrases.getPhrases().get(count).getQuoteId());
        pst.setString(2, phrases.getPhrases().get(count).getQuote());
        pst.setString(3, phrases.getPhrases().get(count).getAuthor());
        pst.setString(4, phrases.getPhrases().get(count).getSeries());

        return pst.executeUpdate();
    }

    public boolean insert(String query, int count, Phrases phrases) throws SQLException {
        executeUpdate(query, count, phrases);

        return true;
    }
    public boolean delete(String query) throws SQLException {
        executeQuery(query);

        return true;
    }

    public boolean update(String query) throws SQLException {
        executeQuery(query);

        return true;
    }

    /**
     * Выполнение запроса к БД с результирующим набором данных
     *
     * @param query запрос
     *
     * @return ArrayList с результатами в виде массива строк
     */
    public ArrayList executeQueryWithResult(String query) {
        ArrayList<String[]> rows = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                String[] columns = new String[rsmd.getColumnCount()];
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    columns[i] = rs.getString(i + 1);
                }
                rows.add(columns);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    /**
     * Пример построения параметризованного запроса
     * @param text
     * @return
     * @throws SQLException
     */
    public ArrayList search(String text) throws SQLException {
        String query = "SELECT * FROM `users` WHERE name LIKE ? AND id > ?;";
        PreparedStatement st = this.connection.prepareStatement(query);

        st.setString(1, "%" + text + "%");
        st.setString(2, "0");

        ArrayList<String[]> rows = new ArrayList<>();
        ResultSet rs = st.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            String[] columns = new String[rsmd.getColumnCount()];
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                columns[i] = rs.getString(i + 1);
            }
            rows.add(columns);
        }
        return rows;
    }
}