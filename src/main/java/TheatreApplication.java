import config.Configuration;
import config.ConfigurationManager;
import model.MasterData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ServerListenerThread;
import server.TheatreAutoUpdateScheduler;
import server.TheatreServer;

public class TheatreApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(TheatreApplication.class);

    public static void main(String[] args) {
        printOut("Server starting...");
        ConfigurationManager.getInstance().loadMyConfigurationFile("src/main/resources/server-config.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        printOut(String.format("Using port: %s", conf.getPort()));

        try {
            MasterData.fetchData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fetch data failed : " + e.getMessage());
        }
        TheatreServer serverListener = new TheatreServer(conf.getPort());
        TheatreAutoUpdateScheduler scheduler = new TheatreAutoUpdateScheduler();
        serverListener.start();
        scheduler.start();
    }

    static void printOut(String a) {
        LOGGER.info(a);
    }
}
