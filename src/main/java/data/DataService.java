package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import constants.ClientConstants;
import constants.FormatConstants;
import constants.ServerConstants;
import model.Cinema;
import model.Movie;
import model.ScheduleShowTimes;
import model.ShowTime;
import utils.Json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        String endpoint = ClientConstants.API.concat(ClientConstants.CINEMAS);
        List dataList = DataService.clientCall(endpoint, ArrayList.class);
        List<Cinema> cinemas = Json.defaultObjectMapper().convertValue(dataList, new TypeReference<List<Cinema>>() { });

        cinemas.forEach(cinema -> {
            if (Objects.nonNull(cinema.getImage())) {
                cinema.setImage(ClientConstants.HOST.concat(cinema.getImage()));
            }

        });

        String data = Json.stringifyPretty(Json.toJson(cinemas));
        return data;
    }

    public static String getAllMovies(boolean newest) throws JsonProcessingException {
        String endpoint = ClientConstants.API.concat(ClientConstants.MOVIES);
        List dataList = DataService.clientCall(endpoint, ArrayList.class);
        List<Movie> movies = Json.defaultObjectMapper().convertValue(dataList, new TypeReference<List<Movie>>() { });

        movies.stream()
                .filter(mov -> Objects.nonNull(mov) && Objects.nonNull(mov.getEn_name()))
                .forEach(movie -> {
            if (Objects.nonNull(movie.getPoster())) {
                movie.setPoster(ClientConstants.HOST.concat(movie.getPoster()));
            }

            if (Objects.nonNull(movie.getRelease())) {
                SimpleDateFormat fm = new SimpleDateFormat(FormatConstants.FORMAT_DATE_STRING);
                try {
                    Date dateRelease = fm.parse(movie.getRelease());
                    movie.setReleaseDate(dateRelease);
                } catch (ParseException e) {
                    System.out.println("Error parsing date "+ e.getMessage());
                }
            }
        });

        if (newest) {
            movies = movies.stream().filter(mov -> Objects.nonNull(mov) && Objects.nonNull(mov.getEn_name()))
                    .filter(item -> {
                                return Objects.nonNull(item.getReleaseDate()) &&
                                        item.getReleaseDate().after(ServerConstants.trendingDate);
                            }).collect(Collectors.toList());
        }

        String data = Json.stringifyPretty(Json.toJson(movies));
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
            item.setShowTimes(Json.defaultObjectMapper().convertValue(v, new TypeReference<List<ShowTime>>() { }));

            item.getShowTimes().forEach(showTime -> {
                String src = showTime.getPoster();
                if (Objects.nonNull(src)) {
                    showTime.setPoster(ClientConstants.HOST.concat(src));
                }
            });

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
