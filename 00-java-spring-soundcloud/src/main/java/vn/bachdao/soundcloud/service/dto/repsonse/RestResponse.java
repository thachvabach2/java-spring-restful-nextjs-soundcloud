package vn.bachdao.soundcloud.service.dto.repsonse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private Object message;
    private String error;
    private T data;
}