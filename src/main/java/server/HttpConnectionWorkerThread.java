package server;

import exceptions.HttpParsingException;
import http.HttpParser;
import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtil;
import utils.ResponseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
            outputStream = socket.getOutputStream();

            String html = "<html><head><title>Theatre HTTP Server</title></head><body> Welcome to HTTP Server </body></html>";

            final String CRLF = "\n\r";

            HttpRequest request = null;

            try{
               request = parser.parseHttpRequest(inputStream);

            } catch (HttpParsingException e) {
                LOGGER.error("Error While Parsing The Request");
            }

//            String response =
//                    "HTTP/1.1 200 OK" + CRLF + // STATUS LINE
//                            "Content-length: " + html.getBytes().length + //HEADER
//                            CRLF + CRLF +
//                            html +
//                            CRLF + CRLF;
            String response = ResponseUtil.responseJSON(RESPONSE_TEMPLATE, html);

            outputStream.write(response.getBytes());

        } catch (IOException e) {
            LOGGER.error("Error while having communication.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
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
