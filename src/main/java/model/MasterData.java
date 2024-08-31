package model;

import com.fasterxml.jackson.core.type.TypeReference;
import constants.ClientConstants;
import data.DataService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import utils.Json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Slf4j
public class MasterData {
    private static MasterData instance;
    private List<MovieMM> dataMoviesNow;
    private List<MovieMM> dataMoviesSoon;
    private List<CityMM> cities;

    public static MasterData getInstance() {
        if (instance == null) {
            instance = new MasterData();
        }
        return instance;
    }

    public static void fetchData() {
        MasterData instance = MasterData.getInstance();
        String endPoint = ClientConstants.MOMO_ENDPOINT;
        String zaloEndpoint = ClientConstants.ZALO_SCREENING_ENDPOINT;
//        Map<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>> momoMasterData =
//                DataService.clientCall(endPoint, LinkedHashMap.class, false);

        Map<String, Object> zaloScreeningData = DataService.clientCall(zaloEndpoint,
                LinkedHashMap.class, false);

//        LinkedHashMap<String, LinkedHashMap<String, Object>> mapDataMoviesNow = pageProps.get("dataMoviesNow");
        // set data
        instance.setMovieNowList((ArrayList<LinkedHashMap<String, Object>>) zaloScreeningData.get("data"));
        instance.setMovieSoonList((ArrayList<LinkedHashMap<String, Object>>) zaloScreeningData.get("data"));
        instance.setDataCities();

        log.info("up coming movies size : {}", instance.dataMoviesSoon.size());
        log.info("screening movies size : {}", instance.dataMoviesNow.size());
        log.info("cities size : {}", instance.cities.size());
    }

    public void setMovieNowList(ArrayList<LinkedHashMap<String, Object>> listData) {
        MasterData instance = MasterData.getInstance();
        List<MovieMM> movies = new ArrayList<>();
        for (LinkedHashMap<String, Object> dataMap : listData) {
//            movies.add(MovieMM.fromMap(dataMap));
            movies.add(MovieMM.fromMapZL(dataMap));
        }
        instance.setDataMoviesNow(movies);
    }

    public void setMovieSoonList(ArrayList<LinkedHashMap<String, Object>> listData) {
        MasterData instance = MasterData.getInstance();
        List<MovieMM> movies = new ArrayList<>();
        for (LinkedHashMap<String, Object> dataMap : listData) {
            movies.add(MovieMM.fromMapZL(dataMap));
        }
        instance.setDataMoviesSoon(movies);
    }

    public void setDataCities() {
        MasterData instance = MasterData.getInstance();
        instance.setCities(CityMM.initCities());
    }
}
