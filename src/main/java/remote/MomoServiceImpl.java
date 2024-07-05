package remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utils.Json;

import java.util.LinkedHashMap;

@Slf4j
public class MomoServiceImpl {
    private static final String BASE_URL = "https://webmomoapi.momo.vn/api/ci-film/session/";

    private final MomoService momoService;

    public MomoServiceImpl() {
        ObjectMapper mapper = Json.defaultObjectMapper();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();
        this.momoService = retrofit.create(MomoService.class);
    }

    public LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> getMovieByCinema(
            String clientToken, String clientId, String clientDevice, String timeStamp, String time,int sortType, int sortDir,
            String apiCityId, String date, String apiFilmId) {
        try {
            Call<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>> call = momoService
                    .getMovieByCinema(apiFilmId, clientToken, clientId, clientDevice, timeStamp, time, sortType,
                            sortDir, apiCityId, date);

            Response<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (Exception e) {
            log.error("Momo call failed : {}", e.getMessage());
        }
        return new LinkedHashMap<>();
    }
}
