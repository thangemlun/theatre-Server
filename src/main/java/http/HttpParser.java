package http;

import com.sun.xml.internal.ws.util.StringUtils;
import exceptions.BadHttpVersionException;
import exceptions.HttpParsingException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13 (Carriage Return)
    private static final int LF = 0x0A; // 10 (Line Feed)

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        parseRequestLine(reader, request);
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }
    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int _byte;
        try {
            while ( (_byte = reader.read()) >= 0 ) {
                if (_byte == CR) {
                    _byte = reader.read();
                    if (_byte == LF) {
                        LOGGER.debug("Request line VERSION to process : {}", processingDataBuffer.toString());
                        if (!methodParsed || !requestTargetParsed) {
                            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                        }

                        try {
                            request.setHttpVersion(processingDataBuffer.toString());
                        } catch (BadHttpVersionException e) {
                            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                        }

                        return;
                    }
                }

                if (_byte == SP) {
                    if (!methodParsed) {
                        LOGGER.debug("Request line METHOD to process : {}", processingDataBuffer.toString());
                        methodParsed = true;
                        request.setMethod(processingDataBuffer.toString());
                    } else if (!requestTargetParsed) {
                        LOGGER.debug("Request line REQ TARGET to process : {}", processingDataBuffer.toString());
                        requestTargetParsed = true;
                        request.setRequestTarget(parseRequestTarget(processingDataBuffer.toString()));
                    } else {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    processingDataBuffer.delete(0, processingDataBuffer.length());
                } else {
                    processingDataBuffer.append((char) _byte);
                    if (!methodParsed) {
                        if (processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) {
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {

    }

    private HttpRequestTarget parseRequestTarget(String requestTarget) {
        HttpRequestTarget target = new HttpRequestTarget();
        String path = "";
        Map<String, Object> params = new HashMap<>();
        getPathAndParams(requestTarget, path, params);
        target.setPath(path);
        target.setParams(params);
        return target;
    }

    private void getPathAndParams(String requestTarget, String path, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder("");
        int index = -1;
        while ((index ++) < requestTarget.length()) {
            if (requestTarget.charAt(index) == '?') {
                path = builder.toString();
                builder.delete(0, builder.length());
                continue;
            }
            builder.append(requestTarget.charAt(index));
        }

        // start get params
        List<String> paramList = Arrays.asList(builder.toString().split("&"));
        for (String param : paramList) {
            List<String> item = Arrays.asList(param.split("="));
            if (!item.isEmpty() && item.size() > 1 &&  ObjectUtils.allNotNull(item.get(0), item.get(1))) {
                params.put(item.get(0), item.get(1));
            }
        }
    }
}
