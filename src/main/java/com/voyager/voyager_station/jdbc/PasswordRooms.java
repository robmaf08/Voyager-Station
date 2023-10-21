/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.voyager.voyager_station.jdbc;

import com.voyager.voyager_station.type.Room;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author Roberto Maffucci
 */
public class PasswordRooms {

    public static final String CREATE_TABLE_PASSWORD_ROOMS = "CREATE TABLE IF NOT EXISTS PasswordRooms ("
            + "Name VARCHAR(1024), "
            + "Password VARCHAR(1024)"
            + ")";

    private static PasswordRooms instance;

    private Connection connection;

    private PasswordRooms() {

    }

    public static PasswordRooms getInstance() {
        if (instance == null) {
            instance = new PasswordRooms();
        }
        return instance;
    }

    public void connect() throws SQLException {
        Properties dbprops = new Properties();
        dbprops.setProperty("robmaf08", "robmaf08");
        dbprops.setProperty("password", "map2021");
        connection = DriverManager.getConnection("jdbc:h2:./resources/db/PasswordRooms", dbprops);
        try (Statement stm = connection.createStatement()) {
            stm.executeUpdate(CREATE_TABLE_PASSWORD_ROOMS);
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public void insertRoomPassword(Room room, String password) throws SQLException {
        try (PreparedStatement pstm = connection.prepareStatement("INSERT INTO PasswordRooms VALUES (?,?)")) {
            pstm.setString(1, room.getName());
            pstm.setString(2, password);
            pstm.execute();
        }
    }

    public String searchRoomPassword(String query) throws SQLException {
        String password;
        try (Statement stm = connection.createStatement()) {
            password = null;
            try (ResultSet rs = stm.executeQuery("SELECT * FROM PasswordRooms WHERE Name LIKE '%" + query + "%'")) {
                while (rs.next()) {
                    password = rs.getString(2);
                }
            } catch (Exception ex) {
            }
        }
        return password;
    }

}
