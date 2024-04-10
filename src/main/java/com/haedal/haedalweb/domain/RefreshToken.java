package com.haedal.haedalweb.domain;

import com.haedal.haedalweb.constants.LoginConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = LoginConstants.REFRESH_TOKEN_EXPIRATION_TIME_S)
public class RefreshToken {
    @Id
    private String token;

    @Indexed
    private String userId;
}
