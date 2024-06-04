package utils;

import Enum.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import http.HttpStatusCode;

import java.util.Date;

public class ResponseUtil {
    private final static String template = FileUtil.readStringFromFile("src/main/resources/response-template");
    public static String responseJSON(String data) {
        String tranform = template
                .replace("{statusCode}", HttpStatusCode.SERVER_RESPONSE_200_SUCCESS.getCode())
                .replace("{statusMessage}", HttpStatusCode.SERVER_RESPONSE_200_SUCCESS.getMessage())
                .replace("{date}", new Date().toGMTString())
                .replace("{modified-date}", new Date().toGMTString())
                .replace("{content-length}", String.valueOf(data.getBytes().length))
                .replace("{content-type}", ContentType.APPLICATION_JSON.getValue())
                .replace("{data}", data);

        return tranform;
    }

    public static String responseInternalServer() throws JsonProcessingException {
        String tranform = template
                .replace("{statusCode}", HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR.getCode())
                .replace("{statusMessage}", HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR.getMessage())
                .replace("{date}", new Date().toGMTString())
                .replace("{modified-date}", new Date().toGMTString())
                .replace("{content-type}", ContentType.APPLICATION_JSON.getValue())
                .replace("{data}", Json.stringifyPretty(Json.toJson(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR)));

        return tranform;
    }

    public static String responseBadRequest() throws JsonProcessingException {
        String tranform = template
                .replace("{statusCode}", HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST.getCode())
                .replace("{statusMessage}", HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST.getMessage())
                .replace("{date}", new Date().toGMTString())
                .replace("{modified-date}", new Date().toGMTString())
                .replace("{content-type}", ContentType.APPLICATION_JSON.getValue())
                .replace("{data}", Json.stringifyPretty(Json.toJson(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST)));

        return tranform;
    }
}
