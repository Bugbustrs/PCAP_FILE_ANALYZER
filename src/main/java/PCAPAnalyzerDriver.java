import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PCAPAnalyzerDriver {
    private FileMonitor fileMonitor;
    private final ScheduledExecutorService scheduler;

    public PCAPAnalyzerDriver() {
        fileMonitor = new FileMonitor(Config.HOST_NAME);
        scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * This methods starts the file monitor and also creates the Executor services that will be running running the analyzer after a certain time.
     */
    public void initiate() {

        if (fileMonitor.start()) {
            /*we want to create the scheduled executor service*/
            scheduler.scheduleAtFixedRate(new Analyzer(fileMonitor), Config.MINUTES_INIT_DELAY, Config.MINUTES_TILL_ANALYZE_PCAPFILES, TimeUnit.MINUTES);
        } else {//if we cant connect
            System.err.println("Please check the host in the config file and try to correct that and then restart this service.");
        }
    }
}
