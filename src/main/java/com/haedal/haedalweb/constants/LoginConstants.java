package com.haedal.haedalweb.constants;

public final class LoginConstants {
    public static final String REFRESH_TOKEN_NULL = "refresh token null";
    public static final String REFRESH_TOKEN_EXPIRED = "refresh token expired";
    public static final String INVALID_REFRESH_TOKEN = "invalid refresh token";

    public static final String ACCESS_TOKEN_EXPIRED = "access token null";
    public static final String INVALID_ACCESS_TOKEN = "invalid access token";

    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "Authorization";

    public static final String USER_ID_CLAIM = "userId";
    public static final String ROLE_CLAIM = "role";
    public static final String CATEGORY_CLAIM = "category";

    public static final long ACCESS_TOKEN_EXPIRATION_TIME_MS = 3600*1000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME_MS = 86400*1000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME_S = 86400; // 1 day
}
