package example;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Student skeleton – fill in stubs & assertions for 200/404/500, timeout, malformed body.
 */
@DisplayName("HttpCurrencyClient + WireMock (student skeleton)")
class HttpCurrencyClientTest {

    static WireMockServer wm;

    @BeforeAll
    static void beforeAll() {
        wm = new WireMockServer(0); // dynamic port
        wm.start();
        configureFor("localhost", wm.port());
    }

    @AfterAll
    static void afterAll() {
        if (wm != null) wm.stop();
    }

    @BeforeEach
    void reset() {
        wm.resetAll();
    }

    @Test
    @DisplayName("200 OK -> returns parsed double")
    void ok200_returnsDouble() {
        // TODO: stub GET /convert to return body "123.45" with status 200
        // TODO: create client with baseUrl pointing to wm.baseUrl()
        // TODO: assert that convert returns 123.45 within a small delta
        // TODO: verify request was made with expected query params using WireMock verify()
        throw new UnsupportedOperationException("implement");
    }

    @Test
    @DisplayName("404 -> IllegalArgumentException('unsupported currency')")
    void notFound404_throwsIllegalArgument() {
        // TODO: stub 404 and assert exception
        throw new UnsupportedOperationException("implement");
    }

    @Test
    @DisplayName("500 -> IllegalStateException('remote error: 500')")
    void server500_throwsIllegalState() {
        // TODO: stub 500 and assert exception
        throw new UnsupportedOperationException("implement");
    }

    @Test
    @DisplayName("timeout -> RuntimeException")
    void timeout_runtimeException() {
        // TODO: use a fixed delay greater than HttpClient default? Or simulate network failure with Fault.CONNECTION_RESET_BY_PEER
        // e.g., stubFor(get(urlPathEqualTo("/convert")).willReturn(aResponse().withFixedDelay(3000)));
        // Expect RuntimeException wrapped by client
        throw new UnsupportedOperationException("implement");
    }

    @Test
    @DisplayName("malformed body -> RuntimeException wrapping NumberFormatException")
    void malformedBody_runtimeException() {
        // TODO: return body 'oops' with 200 and assert RuntimeException cause is NumberFormatException
        throw new UnsupportedOperationException("implement");
    }
}
