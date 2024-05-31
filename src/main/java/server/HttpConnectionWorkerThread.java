package server;

import constants.ClientConstants;
import data.DataService;
import exceptions.HttpParsingException;
import http.HttpParser;
import http.HttpRequest;
import model.ShowTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtil;
import utils.Json;
import utils.ResponseUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class HttpConnectionWorkerThread extends Thread {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private final HttpParser parser;

    private final String RESPONSE_TEMPLATE;

    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
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

            String data = Json.stringifyPretty(Json.toJson(
                            DataService.clientCall(
                                    ClientConstants.API.concat(ClientConstants.SHOWTIMES), LinkedHashMap.class)));

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
}
