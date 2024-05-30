package http;

public enum HttpMethod {
    GET,POST;
    public static final int MAX_LENGTH;

    static {
        int tempMax = -1;
        for (HttpMethod method : HttpMethod.values()) {
            if (method.name().length() > tempMax) {
                tempMax = method.name().length();
            }
        }
        MAX_LENGTH = tempMax;
    }
}
