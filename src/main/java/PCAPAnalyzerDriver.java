import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PCAPAnalyzerDriver {
    private FileMonitor fileMonitor;
    private final ScheduledExecutorService scheduler;
    static JSONObject CONFIG;
    public PCAPAnalyzerDriver(JSONObject config) {
        CONFIG =config;
        fileMonitor = new FileMonitor(config.getString("FILE_SERVER_HOSTNAME"));
        scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * This methods starts the file monitor and also creates the Executor services that will be running running the analyzer after a certain time.
     */
    public boolean initiate() {

        if (fileMonitor.start()) {
            /*we want to create the scheduled executor service*/
            int init_delay = CONFIG.getInt("MINUTES_INIT_DELAY");
            int minutes_till_analyze = CONFIG.getInt("MINUTES_TILL_ANALYZE_PCAPFILES");
            scheduler.scheduleAtFixedRate(new Analyzer(fileMonitor), init_delay, minutes_till_analyze, TimeUnit.MINUTES);
            return true;
        } else {//if we cant connect
            System.err.println("Please check the host in the config file and try to correct that and then restart this service.");
            return false;
        }
    }
}
