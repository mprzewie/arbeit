package pl.edu.agh.arbeit.data.repository;

import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseEventRepository implements EventRepository {

    public static DatabaseEventRepository initializeDBWithDrop(){
        DatabaseEventRepository databaseEventRepository = new DatabaseEventRepository();
        databaseEventRepository.initialize(true);
        return databaseEventRepository;
    }


    public static DatabaseEventRepository initializeDBOrConnectToExisting(){
        DatabaseEventRepository databaseEventRepository = new DatabaseEventRepository();
        databaseEventRepository.initialize(false);
        return databaseEventRepository;
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
        System.out.println("SQL:   " + sql);

        try (Connection connection = DriverManager.getConnection(url)) {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO change to stream maybe?
    public List<Event> getAllEvents(){
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

    public List<Event> getEventsForGivenApps(String[] applications) {
        String sql = "SELECT rowid, topic, eventType, eventDate\n" +
                "FROM Event where topic = ?";
        LinkedList<Event> result = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(url)
        ) {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (String name: applications) {
                statement.setString(1, name);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    fromResultSet(rs).ifPresent(result::addLast);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Optional<Event> getPreviousEventTypeForApp(List<Event> events) {

        Optional<Event> firstEvent = events
                .stream()
                .min(Comparator.comparing(Event::getDate));

        if (!firstEvent.isPresent()) {
            return Optional.empty();
        }

        Date firstEventDate = firstEvent.get().getDate();

        String dateToDb = dateFormat.format(firstEventDate);
        String firstEventTopic = firstEvent.get().getTopic();

        String sql = "SELECT * " +
                "FROM Event " +
                "WHERE (eventType='START' or eventType='STOP') and topic='" + firstEventTopic + "' and eventDate < datetime('" + dateToDb + "') " +
                "order by datetime(eventDate) DESC " +
                " LIMIT 1";

        Optional<Event> result = Optional.empty();
        try (Connection conn = DriverManager.getConnection(url)
        ) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result = fromResultSet(rs);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Event> getEventForGivenAppinRange(String application, Date startDate, Date endDate){
        String formatStartDate = dateFormat.format(startDate);
        String formatEndDate = dateFormat.format(endDate);

        String sql = "SELECT rowid, topic, eventType, eventDate\n" +
                "FROM Event where topic = ? AND eventDate > ? AND eventDate < ?";

        LinkedList<Event> result = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(url)
        ) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, application);
            statement.setString(2, formatStartDate);
            statement.setString(3, formatEndDate);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
               fromResultSet(rs).ifPresent(result::addLast);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Event> getBy(Date date, String topic) {
        // TODO implement this method
        return null;
    }

    @Override
    public List<Event> getBy(Date date) {
        // TODO implement this method
        return null;
    }

    @Override
    public List<Event> getBy(String topic) {
        // TODO implement this method
        return null;
    }

    @Override
    public List<Event> getAll() {
        // TODO implement this method
        return null;
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
