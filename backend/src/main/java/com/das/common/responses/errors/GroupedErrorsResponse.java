package com.das.common.responses.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GroupedErrorsResponse extends ErrorResponse {
    private Map<String, String> errors;

    public GroupedErrorsResponse(Integer code, String message, Map<String, String> errors) {
        super(code, message);
        this.errors = errors;
    }
}
