package config;


public class Configuration {
    private int port;
    private String webRoot;

    public Configuration() {};

    public int getPort() {
        return port;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
