package remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.LinkedHashMap;

public interface MomoService {
    @GET("/{apiFilmId}")
    public Call<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>> getMovieByCinema(
            @Path("apiFilmId") String apiFilmId,
            @Header("X-Client-Token") String clientToken, @Header("X-Client-Id") String clientId,
            @Header("X-Client-Device") String clientDevice,
            @Header("X-Timestamp") String timeStamp,
            @Query("t") String time,
            @Query("sortType") int sortType, @Query("sortDir") int sortDir, @Query("apiCityId") String apiCityId,
            @Query("date") String date);
}
