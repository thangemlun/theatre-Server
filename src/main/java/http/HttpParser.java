package http;

import exceptions.BadHttpVersionException;
import exceptions.HttpParsingException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13 (Carriage Return)
    private static final int LF = 0x0A; // 10 (Line Feed)

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        parseRequestLine(reader, request);
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
        StringBuilder sb = new StringBuilder();
//        BufferedReader bufReader = new BufferedReader(reader);
        try {
            int _byte;
            while ((_byte = reader.read()) >= 0) {
                sb.append((char) _byte);
                System.out.print((char) _byte);
            }
            System.out.println("REQUEST : \n" + sb.toString());
        } catch (Exception e) {

        }
        // read request

    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {

    }

    private HttpRequestTarget parseRequestTarget(String requestTarget) {
        HttpRequestTarget target = new HttpRequestTarget();
        getPathAndParams(requestTarget, target);
        return target;
    }

    private void getPathAndParams(String requestTarget, HttpRequestTarget request) {
        String path = "";
        String paramString = "";
        int paramIndex = 0;
        boolean hasParam = false;
        while (paramIndex < requestTarget.length()) {
            if (requestTarget.charAt(paramIndex) == '?') {
                hasParam = true;
                break;
            }
            paramIndex ++;
        }
        path = requestTarget.substring(0, paramIndex);
        if (hasParam) {
            paramString = requestTarget.substring(paramIndex + 1, requestTarget.length());
        }

        // start get params
        Map<String, Object> mapParams = new HashMap<>();
        List<String> paramList = Arrays.asList(paramString.toString().split("&"));
        for (String param : paramList) {
            List<String> item = Arrays.asList(param.split("="));
            if (!item.isEmpty() && item.size() > 1 &&  ObjectUtils.allNotNull(item.get(0), item.get(1))) {
                mapParams.put(item.get(0), item.get(1));
            }
        }
        request.setPath(path);
        request.setParams(mapParams);
    }
}
