package com.github.gun2.restmodel.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 유효성 검증 관련 error
 */
@Getter
@Setter
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse<List<ValidationErrorResponse.ClientFieldError>> {


    public ValidationErrorResponse(List<ClientFieldError> error){
        super(error);
    }

    public static ValidationErrorResponse of(List<FieldError> errors){
        return new ValidationErrorResponse(errors.stream().map(ClientFieldError::new).collect(Collectors.toList()));
    }



    @Setter
    @Getter
    @NoArgsConstructor
    public static class ClientFieldError implements Serializable {
        String field;
        String defaultMessage;
        ClientFieldError(FieldError fieldError){
            this.field = fieldError.getField();
            this.defaultMessage = fieldError.getDefaultMessage();
        }

        protected ClientFieldError(String field, String defaultMessage) {
            this.field = field;
            this.defaultMessage = defaultMessage;
        }

        public static ClientFieldError of(String field, String defaultMessage){
            return new ClientFieldError(field, defaultMessage);
        }
    }
}
