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
public class RoomDatabase {

    private static final Properties dbprops = new Properties();

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Room ("
            + "id INT PRIMARY KEY,"
            + "name VARCHAR(1024),"
            + "description VARCHAR(1024),"
            + "look VARCHAR(1024),"
            + "imgNameRoom VARCHAR(1024),"
            + "visible BOOLEAN );";

    public void init() {
        try {
            connectToDatabase();
            try (Connection conn = DriverManager.getConnection("jdbc:h2:./resources/db/Room", dbprops)) {
                Statement stm = conn.createStatement();
                stm.executeUpdate(CREATE_TABLE);
                stm.close();
                stm = conn.createStatement();
                stm.executeUpdate("TRUNCATE TABLE Room");
                stm.close();

                PreparedStatement pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 1);
                pstm.setString(2, "camera");
                pstm.setString(3, "Ti trovi nella tua camera spaziale "
                        + "dopo un lungo viaggio dalla terra! La vista è magnifica, ti sembra tutto surreale.");
                pstm.setString(4, "Nulla di interessante... a parte una vista mozzafiato. ");
                pstm.setString(5, "pBedroom.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 2);
                pstm.setString(2, "bagno");
                pstm.setString(3, "Ti trovi nel bagno della tua camera. E' uno dei bagni più belli "
                        + "che tu abbia mai visto, super elegante e rilassante per un perfetto relax! La vista "
                        + "della terra è magnifica.");
                pstm.setString(4, "Oltre alla immensa bellenza del bagno, ti chiedi come e dove vadano a finire tutte quelle cose.... ");
                pstm.setString(5, "bathroom.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 3);
                pstm.setString(2, "corridoio");
                pstm.setString(3, "Ti trovi nel corridoio A54. Non ci sono finestre. Non si vede quasi nulla...");
                pstm.setString(4, "Vedi del sangue per terra! Non ci sono oggetti nei dintorni. Fossi in te non andrei in "
                        + "quella direzione. Ma se sei un tipo coraggioso, puoi sempre recuperare un'arma "
                        + "da qualche parte. ");
                pstm.setString(5, "corridoio.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 4);
                pstm.setString(2, "corridoio2");
                pstm.setString(3, "Ti trovi nel corridoio A55. Anche qui nulla di interessante");
                pstm.setString(4, "Non c'è nulla di interessante a parte guarda caso una armeria alla tua sinistra. Magari potresti trovare "
                        + "ciò che ti serve...");
                pstm.setString(5, "corridoio2.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 5);
                pstm.setString(2, "armeria");
                pstm.setString(3, "Ti trovi nell'armeria della stazione. Questa armeria è stata appositamente progettata "
                        + "per avvenimenti di questo tipo, invasioni alieni non sono da tenere sotto gamba...");
                pstm.setString(4, "Vedi uno zaino di armi, appositamente progettate per la difesa da "
                        + "forme di vita estranee. ");
                pstm.setString(5, "armeria.jpg");
                pstm.setBoolean(6, false);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 6);
                pstm.setString(2, "camera2");
                pstm.setString(3, "Ti trovi nella camera del tuo vicino di stanza. Sembra più carina di quella che ti hanno dato...");
                pstm.setString(4, "Niente di interessante, solo una splendida camera singola vista terra, ma "
                        + "ora non è il momento di godersi lo spettacolo! ");
                pstm.setString(5, "bedroom2.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 7);
                pstm.setString(2, "corridoio56");
                pstm.setString(3, "Ti trovi nel corridoio A56. Non ci sono finestre. ");
                pstm.setString(4, "Nulla di interessante. C'è solo una stanza alla tua destra. ");
                pstm.setString(5, "corridoio3.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 8);
                pstm.setString(2, "computerRoom");
                pstm.setString(3, "Ti trovi in una stanza super futuristica con un computer quantistico mai visto!");
                pstm.setString(4, "Dando una occhiata al computer, scopri che c'è un programma per scoprire "
                        + "i codici d'accesso di ogni stanza! Sulla scrivania c'è un foglio con scritto 346871 "
                        + "sarà la password di accesso al computer? ");
                pstm.setString(5, "ComputerRoom.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 9);
                pstm.setString(2, "corridoioAlieno");
                pstm.setString(3, "Entri in un corridoio e davanti a te c'è un alieno mastodontico!! La paura ti fa rimane immbolizzato!");
                pstm.setString(4, "Rimanendo fermo l'alieno non può vederti! Potrà attaccarti solo se fai rumore. Se vuoi trovare la via di fuga "
                        + "dovrai trovare un'arma per ammazzarlo! La stazione è provvista di una armeria, dovresti farci un salto! Assicurati "
                        + "di non fare troppo rumore! ");
                pstm.setString(5, "corridoioAlieno.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 10);
                pstm.setString(2, "corridoio4");
                pstm.setString(3, "Entri nel corridoio A57, quasi non si vede nulla, e davanti a te una è presente una porta verso l'area 2.");
                pstm.setString(4, "Area2? Cosa ci sarà li dentro? Guardi attentamente e trovi scritto infermieria..."
                        + "La porta richiede un codice accesso! A sinistra vedi delle macchie "
                        + "di sangue...");
                pstm.setString(5, "corridoioA57.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 11);
                pstm.setString(2, "relaxRoom");
                pstm.setString(3, "Entri nel stanza relax, anche se in questo momento, non è proprio così!");
                pstm.setString(4, "Davanti a te un'astronauta in tuta spaziale? Perché mai? L'astronauta sembra in fine di vita...");
                pstm.setString(5, "relaxRoom.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 12);
                pstm.setString(2, "infermieria");
                pstm.setString(3, "Ti trovi nella 'infermieria della stazione! Degli strumenti e computer mai visti prima, e tecnologie a te sconsciute"
                        + "pullulano nell stanza");
                pstm.setString(4, "Qualcuno ha lasciato un'archivio cartaceo, cosa ci sarà scritto all'interno? ");
                pstm.setString(5, "infermieria.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 13);
                pstm.setString(2, "commandRoom");
                pstm.setString(3, "Ti trovi nella sala comandi, computer futuristici, poltone super costose, ma ovviamente non sai come utilizzarli.");
                pstm.setString(4, "Alla tua destra noti delle tracce di sangue, ti chiedi a questo punto di chi siano...");
                pstm.setString(5, "commandRoom.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 14);
                pstm.setString(2, "captainRoom");
                pstm.setString(3, "Ti trovi nella la stanza del capitano della stazione.");
                pstm.setString(4, "Luci soffuse, quasi completamente al buio...difronte trovi una persona quasi morente...sarà meglio chiedere cosa è successo ");
                pstm.setString(5, "captainRoom.jpg");
                pstm.setBoolean(6, false);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 15);
                pstm.setString(2, "corridoio5");
                pstm.setString(3, "Ti trovi nel corridoio A58");
                pstm.setString(4, "Nulla di interessante. Alla tua destra c'è una camera con scritto 'Captain Room'. ");
                pstm.setString(5, "corridoioA58.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 16);
                pstm.setString(2, "warehouse");
                pstm.setString(3, "Ti trovi nel magazzino della stazione!");
                pstm.setString(4, "Davanti a te ancora un'altro mastodontico alieno mutaforma con affianco una tuta spaziale! Hai ancora un'arma con te? ");
                pstm.setString(5, "warehouse.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 17);
                pstm.setString(2, "corridoioA59");
                pstm.setString(3, "Ti trovi nel corridoioA59.");
                pstm.setString(4, "Davanti a te c'è solo una porta, attentamente osservi una fessura? Non ci entrerà mica una chiave elettronica? ");
                pstm.setString(5, "corridoioA59.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 19);
                pstm.setString(2, "vault");
                pstm.setString(3, "Ti trovi nella stanza di accesso alla navicella di emegernza.");
                pstm.setString(4, "Cosa c'è da guardare? Sbrigati a tornare sulla terra! ");
                pstm.setString(5, "vault.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();

                pstm = conn.prepareStatement("INSERT INTO Room VALUES (?,?,?,?,?,?)");
                pstm.setInt(1, 20);
                pstm.setString(2, "spaceship");
                pstm.setString(3, "Ti trovi nella spaceship di emergenza.");
                pstm.setString(4, "I comandi automatici sono disattivati, dovrai attivarli manualmente! ");
                pstm.setString(5, "finalScene.jpg");
                pstm.setBoolean(6, true);
                pstm.executeUpdate();
                pstm.close();
            }
        } catch (SQLException ex) {
            System.err.println(ex.getSQLState() + ": " + ex.getMessage());
        }
    }

    public static Room getRoom(String query) throws SQLException {
        Room room = null;
        connectToDatabase();
        Connection conn = DriverManager.getConnection("jdbc:h2:./resources/db/Room", dbprops);

        try (Statement stm = conn.createStatement(); ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                room = new Room(rs.getInt(1));
                room.setName(rs.getString(2));
                room.setDescription(rs.getString(3));
                room.setLook(rs.getString(4));
                room.setImgNameRoom(rs.getString(5));
                room.setVisible(rs.getBoolean(6));
            }
        }
        return room;
    }

    private static void connectToDatabase() {
        dbprops.setProperty("robmaf08", "robmaf08");
        dbprops.setProperty("password", "map2021");
    }

}
