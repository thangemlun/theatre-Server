package http;

public enum HttpStatusCode {
    // CLIENT_ERROR
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_NOT_FOUND(401, "Not Found"),
    CLIENT_ERROR_414_BAD_REQUEST(414, "Bad Request"),

    // SERVER ERROR
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supported"),
    ;

    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int statusCode, String message) {
        this.STATUS_CODE = statusCode;
        this.MESSAGE = message;
    }
}
