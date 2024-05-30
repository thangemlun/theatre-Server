package utils;

import exceptions.HttpConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
    public static String readStringFromFile(String filePath) {
        FileReader reader;
        try {
            reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder("");
            int i ;
            try {
                while ((i = reader.read()) != -1) {
                    sb.append((char) i);
                }
            } catch (IOException e) {
                throw new HttpConfigurationException("Error while read from file. ", e);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException("File not found. ", e);
        }
    }
}
