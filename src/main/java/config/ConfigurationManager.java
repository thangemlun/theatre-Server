package config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.HttpConfigurationException;
import utils.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myConfiguration;
    public ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (myConfigurationManager == null) {
            myConfigurationManager = new ConfigurationManager();
        }

        return myConfigurationManager;
    }

    //Load the config file
    public void loadMyConfigurationFile(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException("File not found. ", e);
        }

        StringBuffer sb =  new StringBuffer();
        int i ;
        try {
            while ( (i = fileReader.read()) != -1 ) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException("Error while read from file. ", e);
        }

        JsonNode conf = null;
        try {
            conf = Json.parse(sb.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            myConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration getCurrentConfiguration() throws HttpConfigurationException {
        if (myConfiguration == null) {
            throw new HttpConfigurationException("No current configuration set. ");
        }
        return myConfiguration;
    }
}
