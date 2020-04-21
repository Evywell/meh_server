package fr.evywell.common.database;

import fr.evywell.common.logger.Log;
import fr.evywell.common.timer.Stopwatch;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PreparedStatementPool extends HashMap<String, PreparedStatement> {
    private Database database;

    public PreparedStatementPool(Database database) {
        this.database = database;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        if (!this.containsKey(sql)) {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();
            PreparedStatement stmt = this.database.createPreparedStatement(sql);
            stopwatch.stop();
            Log.debug("Création de la requête préparée " + sql + " en " + stopwatch.getDiffTime() + " ms");
            this.put(sql, stmt);
            return stmt;
        }

        return this.get(sql);
    }

    public void close() {
        try {
            for (Map.Entry<String, PreparedStatement> entry : this.entrySet()) {
                entry.getValue().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
