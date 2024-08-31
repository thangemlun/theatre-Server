package constants;

public class ClientConstants {

    //API
    public static String HOST = "https://rapchieuphim.com";

    public static String FPT_HOST = "https://api.fptplay.net/api/v7.1_w";

    public static String GET_ALL_CHANNELS = "/tv?st=7zJNYbKLcbnygTh4D6J5lw&e=1719512157&device=Chrome(version%253A126.0.0.0)&drm=1";

    public static String FPT_TOKEN = "?st=aICVwdLcAVNtIQFBgoc1Cg&e=1719511806";

    public static String GET_FPT_SCHEDULE_BY_CHANNEL_ID = "/tvschedule/";

    public static String API = HOST.concat("/api/v1");
    public static String SHOWTIMES = "/showtimes/cinema/";

    public static String MOVIES = "/movies";

    public static String CINEMAS = "/cinemas";

    //THEATRE SERVER ENDPOINT
    public static String GET_ALL_CINEMAS = "/get-all-cinema";

    public static String SHOWTIMES_BY_CINEMA = "/get-show-times-by-cinema";

    public static String GET_ALL_MOVIES = "/get-all-movies";

    public static String GET_UP_COMING_MOVIES = "/get-up-coming-movies";

    public static String GET_ON_SCREEN_MOVIES = "/get-on-screen-movies";

    public static String GET_TV_SHOW_CHANNELS = "/get-all-channels";

    public static String GET_ALL_CITIES = "/get-all-cities";

    public static String GET_SCHEDULE_BY_CHANNEL_ID = "/get-schedule-by-id";

    public static String GET_SCHEDULE_BY_MOVIE = "/get-schedule-by-movie";

    public static String ZALO_SCREENING_ENDPOINT = "https://zlp-movie-api.zalopay.vn/v2/movie/data/film/showing";

    public static String ZALO_MOVIE_SHOWTIMES = "https://zlp-movie-api.zalopay.vn/v2/movie/data/sessions";
}
