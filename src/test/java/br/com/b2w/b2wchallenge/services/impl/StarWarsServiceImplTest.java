package br.com.b2w.b2wchallenge.services.impl;

import br.com.b2w.b2wchallenge.config.RestConfig;
import br.com.b2w.b2wchallenge.data.dto.SwApiResponse;
import br.com.b2w.b2wchallenge.services.StarWarsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@Import({RestConfig.class, StarWarsServiceImpl.class})
class StarWarsServiceImplTest {

    @Autowired
    @Qualifier("swapi")
    RestTemplate swapiTemplate;

    @Autowired
    StarWarsService starWarsService;

    @Test
    @DisplayName("Find planet")
    void findPlanet() {
        SwApiResponse swApiResponse = starWarsService.findPlanet("tatoo");
        assertNotNull(swApiResponse);
        assertNull(swApiResponse.getNext());
        assertNull(swApiResponse.getPrevious());
        assertNotNull(swApiResponse.getResults());
        assertEquals(swApiResponse.getCount(), 1);
    }

    @Test
    @DisplayName("Find planet movie appearances")
    void findPlanetMovieAppearances() {
        Integer movieApearances = starWarsService.getMovieAppearancesCount("tatoo");
        assertEquals(movieApearances, 5);
    }

    @Test
    @DisplayName("Find planet movie appearances not found")
    void findPlanetMovieAppearancesNotFound() {
        Integer movieApearances = starWarsService.getMovieAppearancesCount("not existent");
        assertEquals(movieApearances, 0);
    }

}