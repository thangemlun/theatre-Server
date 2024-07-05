package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import config.HttpsTrustManager;
import config.MomoConfiguration;
import constants.ClientConstants;
import constants.FormatConstants;
import constants.ServerConstants;
import model.*;
import utils.Json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DataService {
    public static <T> T clientCall(String api, Class<T> clazz, boolean isAuthenticated) {
        T data = null;
        try {
            URL url = new URL(api);
            HttpsTrustManager.allowAllSSL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Sec-Ch-Ua-Platform", "\"Windows\"");
            connection.setRequestProperty("X-Did", "2EC82B90304527FC");

            JsonNode node = Json.parse(parseStreamToString(connection.getInputStream()));
            data = Json.fromJson(node, clazz);
            connection.disconnect();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static <T> T clientCallForShowTime(String api, Class<T> clazz, boolean isAuthenticated, Date queryTime,
                                              String token) {
        T data = null;
        try {
            URL url = new URL(api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
            connection.setRequestProperty("Accept", "*/*");
//            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Sec-Ch-Ua-Platform", "\"Windows\"");

            JsonNode node = Json.parse(parseStreamToString(connection.getInputStream()));
            data = Json.fromJson(node, clazz);
            connection.disconnect();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static String getAllCinemas() throws JsonProcessingException {
        String endpoint = ClientConstants.API.concat(ClientConstants.CINEMAS);
        List dataList = DataService.clientCall(endpoint, ArrayList.class, false);
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
        List dataList = DataService.clientCall(endpoint, ArrayList.class, false);
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

            if (Objects.nonNull(movie.getTrailer())) {
                movie.setTrailer(movie.getTrailer().replace("//",""));
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

    public static String getAllChannels() throws JsonProcessingException {
        String endpoint = ClientConstants.FPT_HOST.concat(ClientConstants.GET_ALL_CHANNELS);
        Map dataMap = DataService.clientCall(endpoint, LinkedHashMap.class, false);
        List<Channel> channels = Json.defaultObjectMapper().convertValue(dataMap.get("Channels"), new TypeReference<List<Channel>>() { });

        String data = Json.stringifyPretty(Json.toJson(channels));
        return data;
    }

    public static String getScheduleOfChannelById(String id) throws JsonProcessingException {
        String endpoint = ClientConstants.FPT_HOST.concat(ClientConstants.GET_FPT_SCHEDULE_BY_CHANNEL_ID);
        endpoint = endpoint.concat(id).concat("?st=zibDHOYijv7B_PPQcNBfGQ&e=1719512160").concat("&page=1&day=").concat(getTodayString());
        Map<String, LinkedHashMap<String, Object>> dataMap = DataService.clientCall(endpoint, LinkedHashMap.class, false);

        List<Schedule> schedules = Json.defaultObjectMapper().convertValue(dataMap.get("data").get("schedule_list"), new TypeReference<List<Schedule>>() { });

        String data = Json.stringifyPretty(Json.toJson(schedules));
        return data;
    }

    private static String getTodayString() {
        SimpleDateFormat format = new SimpleDateFormat(FormatConstants.FORMAT_DATE_PARAM);
        return format.format(new Date());
    }

    public static String getShowTimesByCinemaSlug(String slug) throws JsonProcessingException {
        String endpoint = ClientConstants.API.concat(ClientConstants.SHOWTIMES+slug);
        LinkedHashMap<String, List<ShowTime>> dataMap = DataService.clientCall(endpoint, LinkedHashMap.class, false);
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

    public static LinkedHashMap getShowTimesByMovie(String param, Date queryTime) throws JsonProcessingException {
        String endpoint = ClientConstants.ZALO_MOVIE_SHOWTIMES.concat(param);
        String clientToken = Base64.getEncoder().encodeToString(param.getBytes(StandardCharsets.UTF_8));
        LinkedHashMap dataMap = DataService.clientCallForShowTime(endpoint, LinkedHashMap.class, true ,
                queryTime, clientToken);
        return dataMap;
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
