package pl.edu.agh.arbeit.data.repository;

import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DatabaseEventRepository implements EventRepository {

    public DatabaseEventRepository() {
        initialize(true);
    }

    private final String url = "jdbc:sqlite:test.db";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public void put(Event event){
        String dateToInsert = dateFormat.format(event.getDate());
        String sql = "INSERT INTO Event (topic, eventType, eventDate) " +
                "VALUES (" +
                "'" + event.getTopic() +  "'" + ", " +
                "'" + event.getType() + "'" +
                ", " + "'" +  dateToInsert + "'" + ")"
                ;
//        System.out.println("SQL:   " + sql);

        try (Connection connection = DriverManager.getConnection(url)) {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO change to stream maybe?
    public List<Event> getEvents(){
        String sql = "SELECT rowid, topic, eventType, eventDate\n" +
                "FROM Event";

//        System.out.println("SQL:   " + sql);
        LinkedList<Event> result = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fromResultSet(rs).ifPresent(result::addLast);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Optional<Event> fromResultSet(ResultSet set){
        try {
            String topic = set.getString("topic");
            Date parsedDate = dateFormat.parse(set.getString("eventDate"));
            EventType type = EventType.valueOf(EventType.class, set.getString("eventType"));

            Event result = new Event() {
                @Override
                public String getTopic() {
                    return topic;
                }
                @Override
                public EventType getType() {
                    return type;
                }

                @Override
                public Date getDate() {
                    return parsedDate;
                }
            };
            return Optional.of(result);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    private void initialize(){
        initialize(false);
    }
    private void initialize(boolean drop){
        try(Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
            if(drop){
                System.out.println("DROPPING DATABASE");
                setupTablesWithDrop(connection);
            }
            else setupTables(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTablesWithDrop(Connection connection) throws SQLException{
        String sql = "DROP TABLE IF EXISTS Event";
        Statement statement = connection.createStatement();
        statement.execute(sql);
        setupTables(connection);
    }

    private void setupTables(Connection connection) throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS Event (\n"
                + " topic text NOT NULL,\n"
                + "	eventType text NOT NULL,\n"
                + " eventDate datetime NOT NULL\n"
                + ");";
        System.out.println(sql);
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }
}