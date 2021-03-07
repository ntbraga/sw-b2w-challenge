package br.com.b2w.b2wchallenge.services.impl;

import br.com.b2w.b2wchallenge.data.dto.SwApiResponse;
import br.com.b2w.b2wchallenge.services.StarWarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class StarWarsServiceImpl implements StarWarsService {

    private final RestTemplate swapiTemplate;

    @Autowired
    public StarWarsServiceImpl(@Qualifier("swapi") RestTemplate swapiTemplate) {
        this.swapiTemplate = swapiTemplate;
    }

    public SwApiResponse findPlanet(String name) throws RestClientException {
        UriComponents builder = UriComponentsBuilder.fromPath("/planets/")
                .queryParam("search",name)
                .build();
        return this.swapiTemplate.getForObject(builder.toUriString(), SwApiResponse.class);
    }

    public Integer getMovieAppearancesCount(String name) throws RestClientException {
        SwApiResponse planetResponse = this.findPlanet(name);

        Optional<Map<String, Object>> optionalMap = planetResponse.getResults().stream().findFirst();

        if(optionalMap.isPresent()) {
            Map<String, Object> planetData = optionalMap.get();
            Object films = planetData.get("films");
            if(films instanceof List) {
                return ((List<?>) films).size();
            }
        }

        return 0;
    }

}
