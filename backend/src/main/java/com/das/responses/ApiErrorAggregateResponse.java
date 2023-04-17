package com.das.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ApiErrorAggregateResponse extends ApiErrorResponse {
    private Map<String, String> errors;

    public ApiErrorAggregateResponse(Integer code, String message, Map<String, String> errors) {
        super(code, message);
        this.errors = errors;
    }
}
