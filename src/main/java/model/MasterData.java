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
        Map<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>> momoMasterData =
                DataService.clientCall(endPoint, LinkedHashMap.class);

        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> pageProps =
                momoMasterData.get("pageProps");

        LinkedHashMap<String, LinkedHashMap<String, Object>> mapDataMoviesNow = pageProps.get("dataMoviesNow");
        LinkedHashMap<String, LinkedHashMap<String, Object>> mapDataMoviesSoon = pageProps.get("dataMoviesSoon");
        LinkedHashMap<String, LinkedHashMap<String, Object>> mapCinemaMaster = pageProps.get("dataCinemaMaster");
        // set data
        instance.setMovieNowList((ArrayList<LinkedHashMap<String, Object>>) mapDataMoviesNow.get("Data").get("Items"));
        instance.setMovieSoonList((ArrayList<LinkedHashMap<String, Object>>) mapDataMoviesSoon.get("Data").get("Items"));
        instance.setDataCities((ArrayList<LinkedHashMap<String, Object>>) mapCinemaMaster.get("Data").get("Cities"));
        log.info("up coming movies size : {}", instance.dataMoviesSoon.size());
        log.info("screening movies size : {}", instance.dataMoviesNow.size());
        log.info("cities size : {}", instance.cities.size());
    }

    public void setMovieNowList(ArrayList<LinkedHashMap<String, Object>> listData) {
        MasterData instance = MasterData.getInstance();
        List<MovieMM> movies = new ArrayList<>();
        for (LinkedHashMap<String, Object> dataMap : listData) {
            movies.add(MovieMM.fromMap(dataMap));
        }
        instance.setDataMoviesNow(movies);
    }

    public void setMovieSoonList(ArrayList<LinkedHashMap<String, Object>> listData) {
        MasterData instance = MasterData.getInstance();
        List<MovieMM> movies = new ArrayList<>();
        for (LinkedHashMap<String, Object> dataMap : listData) {
            movies.add(MovieMM.fromMap(dataMap));
        }
        instance.setDataMoviesSoon(movies);
    }

    public void setDataCities(ArrayList<LinkedHashMap<String, Object>> objects) {
        MasterData instance = MasterData.getInstance();
        instance.setCities(objects.stream().map(CityMM::fromMap).collect(Collectors.toList()));
    }
}
