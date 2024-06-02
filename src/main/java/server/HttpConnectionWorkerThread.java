package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import constants.ClientConstants;
import data.DataService;
import exceptions.HttpParsingException;
import http.HttpParser;
import http.HttpRequest;
import model.ShowTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.rsa.PSSParameters;
import utils.FileUtil;
import utils.Json;
import utils.ResponseUtil;

import java.io.*;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static constants.ClientConstants.*;

public class HttpConnectionWorkerThread extends Thread {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private final HttpParser parser;

    private final String RESPONSE_TEMPLATE;

    private Socket socket;

    private final Map<String, Function<Object, String>> serviceMap = Stream.of(
                    new AbstractMap.SimpleEntry<String, Function<Object, String>>(GET_ALL_CINEMAS, t -> {
                        try {
                            DataService.getAllCinemas();
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    }),
                    new AbstractMap.SimpleEntry<String, Function<Object, String>>(SHOWTIMES_BY_CINEMA, t -> {
                        try {
                            DataService.getShowTimesByCinemaSlug((String) t);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    }))
            .collect(Collectors.toMap(map -> map.getKey(), map1 -> map1.getValue()));
    public HttpConnectionWorkerThread(Socket socket) throws JsonProcessingException {
        this.socket = socket;
        this.parser = new HttpParser();
        RESPONSE_TEMPLATE = FileUtil.readStringFromFile("src/main/resources/response-template");
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = new BufferedOutputStream(socket.getOutputStream());

            HttpRequest request = null;

            try{
               request = parser.parseHttpRequest(inputStream);

            } catch (HttpParsingException e) {
                LOGGER.error("Error While Parsing The Request");
            }

            String data = processRequestAndGetData(request);

            String response = ResponseUtil.responseJSON(RESPONSE_TEMPLATE, data);

            LOGGER.info("JSON Output Length: {}", data.length());

            outputStream.write(response.getBytes(StandardCharsets.UTF_8), 0, response.getBytes().length);

        } catch (IOException e) {
            LOGGER.error("Error while having communication.", e);
        }  finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
        LOGGER.info("Connection finished.");
    }

    private String processRequestAndGetData(HttpRequest request) {
        Objects.requireNonNull(request);
        return null;
    }
}
