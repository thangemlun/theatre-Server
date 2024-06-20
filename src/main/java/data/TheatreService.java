package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.MasterData;
import server.TheatreClient;
import utils.Json;

public class TheatreService {
    private static TheatreService instance;

    public static TheatreService getInstance(){
        if (instance == null) {
            instance = new TheatreService();
        }
        return instance;
    }

    public String getUpComingMovies() throws JsonProcessingException {
        return Json.stringifyPretty(Json.toJson(MasterData.getInstance().getDataMoviesSoon()));
    }

    public String getOnScreenMovies() throws JsonProcessingException {
        return Json.stringifyPretty(Json.toJson(MasterData.getInstance().getDataMoviesNow()));
    }

}
