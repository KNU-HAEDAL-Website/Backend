package com.haedal.haedalweb.constants;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
    public HttpStatus getHttpStatus();
    public String getMessage();
    public String name();
}
