package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import constants.ClientConstants;
import model.Cinema;
import model.ScheduleShowTimes;
import model.ShowTime;
import utils.Json;
import utils.ResponseUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    public static String getAllCinemas() throws JsonProcessingException {
        String endpoint = ClientConstants.API.concat("cinemas");
        String data = Json.stringifyPretty(Json.toJson(
                DataService.clientCall(endpoint, Cinema.class)));
        return data;
    }

    public static String getShowTimesByCinemaSlug(String slug) throws JsonProcessingException {
        String endpoint = ClientConstants.API.concat(ClientConstants.SHOWTIMES+slug);
        LinkedHashMap<String, List<ShowTime>> dataMap = DataService.clientCall(endpoint, LinkedHashMap.class);
        List<ScheduleShowTimes> scheduleShowTimes = new ArrayList<>();
        // map data to schedule show time
        dataMap.forEach((e, v) -> {
            ScheduleShowTimes item = new ScheduleShowTimes();
            item.setDate(e);
            item.setShowTimes(v);
            scheduleShowTimes.add(item);
        });
        String data = Json.stringifyPretty(Json.toJson(scheduleShowTimes));
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