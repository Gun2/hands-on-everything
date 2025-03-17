package com.github.gun2.restmodel.dto.response;

import com.github.gun2.restmodel.status.ResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
public class BaseResponse {
    private String code = "";
    private String message = "";

    public ResponseEntity toResponseEntity(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        return ResponseEntity.status(responseCode.getStatus()).body(this);
    }
}
