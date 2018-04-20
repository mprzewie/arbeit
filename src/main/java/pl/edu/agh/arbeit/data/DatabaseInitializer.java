package pl.edu.agh.arbeit.data;

import com.sun.corba.se.impl.orb.DataCollectorBase;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static Connection conn = null;
    public static String url = "jdbc:sqlite:test.db";
    public static void initializeDatabase(){


        // create a connection to the database
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
//            createApplicationTable();
            createEventTable();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }


    }

 /*   private static void createApplicationTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Application (\n"
                + "	appName text NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Created Application table.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    private static void createEventTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Event (\n"
                + " appName text NOT NULL,\n"
                + "	eventType text NOT NULL,\n"
                + " eventDate text NOT NULL\n"
                + ");";
        System.out.println(sql);

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Created Event table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
