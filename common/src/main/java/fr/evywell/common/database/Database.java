package fr.evywell.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final String host;
    private final Long port;
    private final String dbname;
    private final String user;
    private final String password;

    private boolean connected;
    private Connection connection;
    private PreparedStatementPool pool;

    public Database(String host, Long port, String dbname, String user, String password) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.user = user;
        this.password = password;
        this.pool = new PreparedStatementPool(this);
        this.connected = false;
    }

    public Database(String dbname, String user, String password) {
        this("localhost", (long) 3306, dbname, user, password);
    }

    public Statement executeStatement(String sql) {
        this.connect();
        try {
            Statement st = this.connection.createStatement();
            if (st.execute(sql)) {
                return st;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement createPreparedStatement(String sql) {
        this.connect();
        try {
            return new PreparedStatement(sql, this.connection.prepareStatement(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        return this.pool.getPreparedStatement(sql);
    }

    public boolean connect() {
        if (this.connected)
            return true;

        try {
            String dsn = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", this.host, this.port, this.dbname, this.user, this.password);
            this.connection = DriverManager.getConnection(
                dsn
            );
            this.connected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        this.pool.close();
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
