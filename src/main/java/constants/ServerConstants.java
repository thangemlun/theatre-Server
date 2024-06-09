package constants;

import java.util.Date;

public class ServerConstants {

    private static Long ONE_MONTH_AS_MILIS = 2629800000L;

    public static Long RESPONSE_TIMEOUT = 60000L;
    public static final Date trendingDate = new Date(new Date().getTime() - ONE_MONTH_AS_MILIS);
}
