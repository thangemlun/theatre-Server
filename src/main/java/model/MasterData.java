package model;

import com.fasterxml.jackson.core.type.TypeReference;
import constants.ClientConstants;
import data.DataService;
import lombok.Getter;
import lombok.Setter;
import utils.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class MasterData {
    private static MasterData instance;
    private List<MovieMM> dataMoviesNow;
    private List<MovieMM> dataMoviesSoon;

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
        // set data
        instance.setMovieNowList((ArrayList<LinkedHashMap<String, Object>>) mapDataMoviesNow.get("Data").get("Items"));
        instance.setMovieSoonList((ArrayList<LinkedHashMap<String, Object>>) mapDataMoviesSoon.get("Data").get("Items"));
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
}
