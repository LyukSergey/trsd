package com.edu.gz22_1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProblemDetail {

    public ProblemDetail(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    private String title;

    private String detail;

    private String type;

    private String instance;

    private Integer status;

    private List<ProblemDetail> errors;

}
