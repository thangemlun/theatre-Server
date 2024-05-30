import config.Configuration;
import config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ServerListenerThread;

public class TheatreApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(TheatreApplication.class);

    public static void main(String[] args) {
        printOut("Server starting...");
        ConfigurationManager.getInstance().loadMyConfigurationFile("src/main/resources/server-config.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        printOut(String.format("Using port: %s", conf.getPort()));

        ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort());
        serverListenerThread.start();
    }

    static void printOut(String a) {
        LOGGER.info(a);
    }
}
