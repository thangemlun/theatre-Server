package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import constants.ServerConstants;
import model.Movie;
import utils.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TheatreService {
    private static TheatreService instance;

    public static TheatreService getInstance (){
        if (instance == null) {
            instance = new TheatreService();
        }
        return instance;
    }

    public String getTrendingMovies() throws JsonProcessingException {
        return DataService.getAllMovies(true);
    }

}
