package com.haedal.haedalweb.constants;

import java.util.concurrent.TimeUnit;

public final class LoginConstants {
    public static final String REFRESH_TOKEN_NULL = "refresh token null";
    public static final String REFRESH_TOKEN_EXPIRED = "refresh token expired";
    public static final String INVALID_REFRESH_TOKEN = "invalid refresh token";

    public static final String ACCESS_TOKEN_EXPIRED = "access token null";
    public static final String INVALID_ACCESS_TOKEN = "invalid access token";

    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "Authorization";

    public static final String USERNAME_CLAIM = "username";
    public static final String ROLE_CLAIM = "role";
    public static final String CATEGORY_CLAIM = "category";

    public static final long ACCESS_TOKEN_EXPIRATION_TIME_MS = TimeUnit.HOURS.toMillis(1);
    public static final long REFRESH_TOKEN_EXPIRATION_TIME_MS = TimeUnit.DAYS.toMillis(1);
    public static final int REFRESH_TOKEN_COOKIE_EXPIRATION_TIME = (int) TimeUnit.DAYS.toSeconds(1);
}
