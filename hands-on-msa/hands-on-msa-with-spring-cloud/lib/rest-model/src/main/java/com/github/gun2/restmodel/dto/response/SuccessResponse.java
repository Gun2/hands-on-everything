package com.github.gun2.restmodel.dto.response;

import com.github.gun2.restmodel.status.SuccessCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuccessResponse<T> extends BaseResponse{
    private T data;

    protected SuccessResponse(T data){
        this.data = data;
    }

    protected SuccessResponse(T data, SuccessCode successCode){
        this.data = data;
        this.setMessage(successCode.getMessage());
        this.setCode(successCode.getCode());
    }

    public final static <T> SuccessResponse of(T data){
        return new SuccessResponse(data);
    }

    public final static <T> SuccessResponse of(T data, SuccessCode successCode){
        return new SuccessResponse(data, successCode);
    }
}
