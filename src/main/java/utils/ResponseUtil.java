package utils;

import constants.ContentType;

import java.util.Date;

public class ResponseUtil {
    public static String responseJSON(String template, String data) {
        String tranform = template.replace("{date}", new Date().toGMTString())
                .replace("{modified-date}", new Date().toGMTString())
                .replace("{content-length}", String.valueOf(data.length()))
                .replace("{content-type}", ContentType.APPLICATION_JSON.getValue())
                .replace("{data}", data);

        return tranform;
    }
}
