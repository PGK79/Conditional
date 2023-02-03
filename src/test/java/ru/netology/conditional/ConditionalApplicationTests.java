package ru.netology.conditional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final GenericContainer<?> myAppDev = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);

    private static final GenericContainer<?> myAppProd = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        myAppDev.start();
        myAppProd.start();
    }

    @Test
    void testDevProfile() {
        // given:
        ResponseEntity<String> forEntityDev = restTemplate.getForEntity("http://localhost:"
                + myAppDev.getMappedPort(8080) + "/profile", String.class);
        String expected = "Current profile is dev";

        // when:
        String actual = forEntityDev.getBody();

        // then:
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testProductionProfile() {
        // given:
        ResponseEntity<String> forEntityProd = restTemplate.getForEntity("http://localhost:"
                + myAppProd.getMappedPort(8081) + "/profile", String.class);
        String expected = "Current profile is production";

        // when:
        String actual = forEntityProd.getBody();

        // then:
        Assertions.assertEquals(expected, actual);
    }
}
