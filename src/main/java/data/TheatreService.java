package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import config.MomoConfiguration;
import constants.FormatConstants;
import model.CinemaMM;
import model.CityMM;
import model.MasterData;
import model.MovieMM;
import remote.MomoServiceImpl;
import utils.Json;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TheatreService {
    private static TheatreService instance;
    static MomoServiceImpl momoService;

    public static TheatreService getInstance(){
        if (instance == null) {
            instance = new TheatreService();
            momoService = new MomoServiceImpl();
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
        Date queryTime = new Date();
        String param = buildScheduleParamsV2(apiFilmId, time, cityId);

        LinkedHashMap<String, LinkedHashMap<String, ArrayList<LinkedHashMap<String, Object>>>> dataMap = DataService
                .getShowTimesByMovie(param, queryTime);

        ArrayList<LinkedHashMap<String, Object>> listObject = dataMap.get("data").get("pCinemas");
        List<CinemaMM> cities = listObject.stream()
                .map(map -> (ArrayList<LinkedHashMap<String, Object>>) map.get("cinemas"))
                .flatMap(Collection::stream).map(CinemaMM::fromMapZL).collect(Collectors.toList());
        return Json.stringifyPretty(Json.toJson(cities));
    }



    private String buildScheduleParams(String apiFilmId, String time, String cityId, Date queryTime) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(apiFilmId);
            sb.append("?t="+queryTime.getTime());
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

    private String buildScheduleParamsV2(String apiFilmId, String time, String cityId) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("?filmId=".concat(apiFilmId));
            sb.append("&locationId=".concat(cityId));
             sb.append("&date="+ new SimpleDateFormat(FormatConstants.FORMAT_DATE_MOMO).format(
                    new Date(Long.valueOf(time))));

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing parameters !");
        }
    }
}
