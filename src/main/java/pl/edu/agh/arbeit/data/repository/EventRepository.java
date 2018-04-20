package pl.edu.agh.arbeit.data.repository;

import pl.edu.agh.arbeit.data.DatabaseInitializer;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.Event;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EventRepository {
    public void insertEvent(Event event) {
//        int x = new Random().nextInt(1500);
        String sql = "INSERT INTO Event (appName, eventType, eventDate) " +
            "VALUES (" +
                "'" + event.getTopic() +  "'" + ", " +
                "'" + event.getType() + "'" +
                ", " + "'" +  event.getDate() + "'" + ")"
                ;
        System.out.println("SQL:   " + sql);

        try (Connection conn = DriverManager.getConnection(DatabaseInitializer.url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllEvents() {

        List<String> result = new LinkedList<>();
        String sql = "SELECT rowid, appName, eventType, eventDate\n" +
                "FROM Event";

        System.out.println("SQL:   " + sql);
        try (Connection conn = DriverManager.getConnection(DatabaseInitializer.url);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            SimpleDateFormat parser=new SimpleDateFormat("EEE MMM d HH:mm:ss zzzz yyyy");
//            System.out.println(rs.getInt("rowid"));
            while (rs.next()) {
//                System.out.println(rs.getInt("id"));
//                System.out.println(rs.getString("appName"));
                result.add("[");
                result.add(rs.getString("appName"));
                result.add(rs.getString("eventType"));
//                FIXME add some wise date parsing so that we filter dates in SQL
                result.add(rs.getString("eventDate"));
                result.add("]\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}
