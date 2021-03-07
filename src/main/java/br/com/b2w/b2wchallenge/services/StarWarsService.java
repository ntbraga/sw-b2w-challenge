package br.com.b2w.b2wchallenge.services;

import br.com.b2w.b2wchallenge.data.dto.SwApiResponse;
import org.springframework.web.client.RestClientException;

public interface StarWarsService {
    SwApiResponse findPlanet(String name) throws RestClientException;
    Integer getMovieAppearancesCount(String name) throws RestClientException;
}
