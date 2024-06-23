package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import constants.FormatConstants;
import model.CinemaMM;
import model.CityMM;
import model.MasterData;
import model.MovieMM;
import utils.Json;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    public String getCities() throws JsonProcessingException {
        return Json.stringifyPretty(Json.toJson(MasterData.getInstance().getCities()));
    }

    public String getScheduleForMovie(String apiFilmId, String time, String cityId) throws JsonProcessingException {
        String param = buildScheduleParams(apiFilmId, time, cityId);
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> dataMap = DataService.getShowTimesByMovie(param);
        ArrayList<LinkedHashMap<String, Object>> listObject = (ArrayList<LinkedHashMap<String, Object>>) dataMap.get("Data").get("Cinemas").get("Items");
        List<CinemaMM> cities = listObject.stream().map(CinemaMM::fromMap).collect(Collectors.toList());
        return Json.stringifyPretty(Json.toJson(cities));
    }

    private String buildScheduleParams(String apiFilmId, String time, String cityId) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(apiFilmId);
            sb.append("?t=" + time);
            sb.append("&sortType=1");
            sb.append("&sortDir=1");
            sb.append("&apiCityId=" + cityId);
            sb.append("&date="+ new SimpleDateFormat(FormatConstants.FORMAT_DATE_MOMO).format(
                    new Date(Long.valueOf(time))));

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing parameters !");
        }
    }
}
