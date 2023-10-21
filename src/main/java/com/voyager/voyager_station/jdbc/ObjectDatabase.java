/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.voyager.voyager_station.jdbc;

import com.voyager.voyager_station.game.Parameters.InterfaceObjectParameters;
import com.voyager.voyager_station.type.AdvObject;
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
public class ObjectDatabase {

    private static final Properties dbprops = new Properties();

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Object ("
            + "id INT PRIMARY KEY,"
            + "name VARCHAR(1024),"
            + "description VARCHAR(1024),"
            + "imgObjRoom VARCHAR(1024)"
            + ");";
   
    public void init(InterfaceObjectParameters obj) {

        try {
            dbprops.setProperty("robmaf08", "robmaf08");
            dbprops.setProperty("password", "map2021");
            try (Connection conn = DriverManager.getConnection("jdbc:h2:./resources/db/Object", dbprops)) {
                Statement stm = conn.createStatement();
                stm.executeUpdate(CREATE_TABLE);
                stm.close();
                stm = conn.createStatement();
                stm.executeUpdate("TRUNCATE TABLE Object");
                stm.close();
                PreparedStatement pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 1);
                pstm.setString(2, obj.journalName());
                pstm.setString(3, "NASA: 27/07/2027. Partono le sperimentazioni del patogeno NewAriston al di fuori della terra. "
                        + "E' stato chiarito che tale patogeno ha scarse probabiltà infettive. Tuttativa, vista la loro conformazione "
                        + "e struttura ancora non nota, la sperimentazione deve essere effettuata al di fuori della terra, "
                        + "al fine di evitare possibili pandemie. Un troupe di medici è stata spedita nelle diversi stazioni "
                        + "spaziali per studiarlo a fondo.");

                pstm.setString(4, "giornale.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 2);
                pstm.setString(2, obj.torchName());
                pstm.setString(3, "Una delle torce più potenti del 2029. "
                        + "Presenta una visione di oltre 2 km, anche se è abbastanza pesante! Può sempre "
                        + "tornare utile");
                pstm.setString(4, "torcia.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 3);
                pstm.setString(2, obj.knifeName());
                pstm.setString(3, "Un semplice bisturi.");
                pstm.setString(4, "bisturi.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 4);
                pstm.setString(2, obj.computerName());
                pstm.setString(3, "Un computer futurisco mai visto prima? Chissa cosa contiene.");
                pstm.setString(4, "");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 5);
                pstm.setString(2, obj.dossierName());
                pstm.setString(3, "Un dossier cartaceo contenente i dati dei pazienti");
                pstm.setString(4, "dossier.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 6);
                pstm.setString(2, obj.commandManualName());
                pstm.setString(3, "Un manuale cartaceo contenente i comandi della spaceship!.");
                pstm.setString(4, "commandManual.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 7);
                pstm.setString(2, obj.suiteName());
                pstm.setString(3, "Una tuta spaziale da utilizzare all'interno delle navicelle.");
                pstm.setString(4, "spacesuit.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 8);
                pstm.setString(2, obj.gunName());
                pstm.setString(3, "Una leggendaria ray-gun con circa 200 colpi!");
                pstm.setString(4, "raygun.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 9);
                pstm.setString(2, obj.keyName());
                pstm.setString(3, "Una chiave elettronica, cosa aprirà?");
                pstm.setString(4, "key.png");
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Object VALUES (?,?,?,?)");
                pstm.setInt(1, 10);
                pstm.setString(2, obj.serverName());
                pstm.setString(3, "Un computer per comunicare con la terra e attivare i comandi manuali. Riuscirai ad utilizzarlo?");
                pstm.setString(4, "");
                pstm.executeUpdate();
                pstm.close();
            }
        } catch (SQLException ex) {
            System.err.println(ex.getSQLState() + ": " + ex.getMessage());
        }
    }

    public AdvObject getObject(String query) throws SQLException {
        AdvObject obj = null;
        connectToDatabase();
        Connection conn = DriverManager.getConnection("jdbc:h2:./resources/db/Object", dbprops);

        try (Statement stm = conn.createStatement(); ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                obj = new AdvObject(rs.getInt(1), rs.getString(2), rs.getString(3));
                obj.setName(rs.getString(2));
                obj.setDescription(rs.getString(3));
                obj.setImgObjRoom(rs.getString(4));
            }
        }
        return obj;
    }
   
    private void connectToDatabase() {
        dbprops.setProperty("robmaf08", "robmaf08");
        dbprops.setProperty("password", "map2021");
    }
}
