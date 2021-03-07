package br.com.b2w.b2wchallenge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServletInitializerTest {
    @Mock
    private SpringApplicationBuilder springApplicationBuilder;


    @Test
    @DisplayName("Configure servlet.")
    void configure() {
        ServletInitializer servletInitializer = new ServletInitializer();
        when(springApplicationBuilder.sources(B2wChallengeApplication.class)).thenReturn(springApplicationBuilder);

        SpringApplicationBuilder result = servletInitializer.configure(springApplicationBuilder);

        verify(springApplicationBuilder).sources(B2wChallengeApplication.class);
        assertEquals(springApplicationBuilder,result);
    }

}