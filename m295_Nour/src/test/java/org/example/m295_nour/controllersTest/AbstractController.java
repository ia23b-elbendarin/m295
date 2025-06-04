package org.example.m295_nour.controllersTest;

import org.example.m295_nour.controllers.AbstractController;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractControllerTest {

    static class DummyController extends AbstractController {
        public ResponseEntity<String> testResponse() {
            return getRespond("Hallo Welt");
        }
    }

    @Test
    void testGetRespond() {
        DummyController dummy = new DummyController();
        ResponseEntity<String> response = dummy.testResponse();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hallo Welt");
    }
}
