package pl.edu.agh.arbeit.data.repository;

import com.google.common.collect.Lists;
import pl.edu.agh.arbeit.tracker.events.Event;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatabaseEventRepository implements EventRepository{
    public void put(Event event) {
//        int x = new Random().nextInt(1500);
//        SimpleDateFormat parserToInsert=new SimpleDateFormat("YYYY-MM-DD HH:MM:SS:sss");
//        SimpleDateFormat parserFromEvent=new SimpleDateFormat("EEE MMM dd HH:MM:SS Z yyyy");
//        Date dt = null;
//        try {
//            dt = convertUtilToSql(parserFromEvent.parse(event.getDate().toString()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
/*        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:MM:SS ZZZZ yyyy");
        Calendar calendar = new GregorianCalendar(
                event.getDate().getYear(),
                event.getDate().getMonth(),
                event.getDate().getDay(),
                event.getDate().getHours(),
                event.getDate().getMinutes(),
                event.getDate().getSeconds());
        System.out.println("HAHAHA "+ event.getDate().getYear() + "(((" + sdf.format(calendar.getTime()));*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getDate());
        String dateToInsert = sdf.format(calendar.getTime());
        String sql = "INSERT INTO Event (appName, eventType, eventDate) " +
            "VALUES (" +
                "'" + event.getTopic() +  "'" + ", " +
                "'" + event.getType() + "'" +
                ", " + "'" +  dateToInsert + "'" + ")"
                ;
        System.out.println("SQL:   " + sql);

        try (Connection conn = DriverManager.getConnection(DatabaseInitializer.getUrl());
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Event> getEvents() {

        List<String> result = new LinkedList<>();
        String sql = "SELECT rowid, appName, eventType, eventDate\n" +
                "FROM Event\n" +
                "WHERE eventDate\n" +
                "BETWEEN '2018-04-21' AND '2018-04-22'";

        System.out.println("SQL:   " + sql);
        try (Connection conn = DriverManager.getConnection(DatabaseInitializer.getUrl());
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
        return Collections.emptyList();

//        return result;
    }
    private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

}
