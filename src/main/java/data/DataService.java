package data;

import com.fasterxml.jackson.databind.JsonNode;
import constants.ClientConstants;
import utils.Json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Supplier;

public class DataService {

    public static <T> T clientCall(String api, Class<T> clazz) {
        T data = null;
        try {
            URL url = new URL(api);
            URLConnection connection = url.openConnection();
            JsonNode node = Json.parse(parseStreamToString(connection.getInputStream()));
            data = Json.fromJson(node, clazz);
        }catch (Exception e) {

        }

        return data;
    }

    static String parseStreamToString(InputStream stream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder("");
            int i ;

            while ((i = br.read()) >= 0) {
                sb.append((char) i);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
