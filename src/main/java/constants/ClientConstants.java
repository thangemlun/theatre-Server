package constants;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientConstants {

    //API
    public static String HOST = "https://rapchieuphim.com";

    public static String API = HOST.concat("/api/v1");
    public static String SHOWTIMES = "/showtimes/cinema/";

    public static String MOVIES = "/movies";

    public static String CINEMAS = "/cinemas";

    //THEATRE SERVER ENDPOINT
    public static String GET_ALL_CINEMAS = "/get-all-cinema";

    public static String SHOWTIMES_BY_CINEMA = "/get-show-times-by-cinema";

    public static String GET_ALL_MOVIES = "/get-all-movies";

    public static String GET_TRENDING_MOVIES = "/get-trending-movies";

}
