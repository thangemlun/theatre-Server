package config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.HttpConfigurationException;
import utils.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MomoConfiguration {

    private final static String filePath = "src/main/resources/momo_token.json";

    private static MomoConfiguration instance;

    private LinkedHashMap<String, String> keyMap;

    public static MomoConfiguration getInstance() {
        if (instance == null) {
            instance = new MomoConfiguration();
        }
        return instance;
    }

    public void loadMOMOToken() {
        FileReader fileReader;
        MomoConfiguration instance = MomoConfiguration.getInstance();
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

        try {
            JsonNode node = Json.parse(sb.toString());
            LinkedHashMap<String, String> toMap = Json.fromJson(node, LinkedHashMap.class);
            instance.setKeyMap(toMap);
        } catch (Exception e) {
            throw new HttpConfigurationException("Error parsing string to object. ", e);
        }

    }

    public LinkedHashMap getKeyMap() {
        return this.keyMap;
    }

    public void setKeyMap(LinkedHashMap map) {
        this.keyMap = map;
    }

}
