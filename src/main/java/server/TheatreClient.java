package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import constants.ServerConstants;
import data.DataService;
import data.TheatreService;
import exceptions.HttpParsingException;
import http.HttpParser;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ResponseUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static constants.ClientConstants.*;

public class TheatreClient extends Thread {
    private final Logger LOGGER = LoggerFactory.getLogger(TheatreClient.class);

    private final HttpParser parser;

    private TheatreService theatreService;

    private Socket socket;

    private final Map<String, Function<Map<String, Object>, Object>> serviceMap = Stream.of(
                    new AbstractMap.SimpleEntry<String, Function<Map<String, Object>, Object>>(GET_ALL_CINEMAS,
                            t -> {

                                try {
                                    return DataService.getAllCinemas();
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            }),
                    new AbstractMap.SimpleEntry<String, Function<Map<String, Object>, Object>>(SHOWTIMES_BY_CINEMA,
                            t -> {
                                try {
                                    String slug = (String) t.get("slug");
                                    return DataService.getShowTimesByCinemaSlug(slug);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            }),
                    new AbstractMap.SimpleEntry<String, Function<Map<String, Object>, Object>>(GET_ALL_MOVIES,
                            t -> {
                                try {
                                    return DataService.getAllMovies(false);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            }),
                    new AbstractMap.SimpleEntry<String, Function<Map<String, Object>, Object>>(GET_TRENDING_MOVIES,
                            t -> {
                                try {
                                    return theatreService.getTrendingMovies();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }))
            .collect(Collectors.toMap(map -> map.getKey(), map1 -> map1.getValue()));
    public TheatreClient(Socket socket) throws JsonProcessingException {
        this.socket = socket;
        this.parser = new HttpParser();
        this.theatreService = TheatreService.getInstance();
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

            String response = ResponseUtil.responseJSON(data);

            LOGGER.info("JSON Output Length: {}", response.length());
            try{
                outputStream.write(response.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }catch (Exception e) {
                e.printStackTrace();
            }



        } catch (Exception e) {
            LOGGER.error("Error while having communication.", e);
            try {
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                String internalResponse = ResponseUtil.responseInternalServer();
                outputStream.write(internalResponse.getBytes(StandardCharsets.UTF_8), 0,
                        internalResponse.getBytes().length);
            } catch (IOException ex) {}

        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            if (outputStream != null) {
                try {
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
        return (String) serviceMap.get(request.getRequestTarget().getPath()).apply(request.getRequestTarget().getParams());
    }
}
