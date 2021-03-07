package br.com.b2w.b2wchallenge.data.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SwApiResponse {

    private Integer count;
    private String next;
    private String previous;
    private List<Map<String, Object>> results;

}
