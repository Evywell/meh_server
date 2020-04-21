package fr.evywell.common.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    public static final int FILTER_NOFILTER = 0;

    private static Log _instance;

    private Log(){}

    public static void info(String s, int filter)
    {
        getInstance().logInfo(s, filter);
    }
    public static void error(String s, int filter)
    {
        getInstance().logError(s, filter);
    }

    public static void info(String s)
    {
        getInstance().logInfo(s, Filter.FILTER_NOFILTER);
    }
    public static void error(String s)
    {
        getInstance().logError(s, Filter.FILTER_NOFILTER);
    }
    public static void debug(String s) {
        getInstance().logDebug(s, Filter.FILTER_NOFILTER);
    }

    private void logInfo(String s, int filter)
    {
        System.out.println(formatLog("INFO", s));
    }
    private void logError(String s, int filter)
    {
        System.out.println(formatLog("ERROR", s));
    }
    private void logDebug(String s, int filter) {
        if (true) { // Remplacer ça par une condition pour savoir si le debug est activé
            System.out.println(formatLog("DEBUG", s));
        }
    }

    private String formatLog(String type, String s) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return String.format("[%s] %s %s", type, format.format(date), s);
    }

    private static Log getInstance()
    {
        if (_instance == null) {
            _instance = new Log();
        }

        return _instance;
    }
}
