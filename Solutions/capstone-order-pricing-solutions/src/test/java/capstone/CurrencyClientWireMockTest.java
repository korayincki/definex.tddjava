package capstone;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CurrencyClient WireMock tests (solutions)")
class CurrencyClientWireMockTest {

    static WireMockServer wm;
    CurrencyClient client;

    @BeforeAll
    static void beforeAll() {
        wm = new WireMockServer(0);
        wm.start();
        configureFor("localhost", wm.port());
    }

    @AfterAll static void afterAll() { if (wm != null) wm.stop(); }

    @BeforeEach
    void setUp() {
        wm.resetAll();
        client = new CurrencyClient(wm.baseUrl());
    }

    @Test
    @DisplayName("200 -> returns rate")
    void ok200() {
        stubFor(get(urlPathEqualTo("/rate"))
                .withQueryParam("from", equalTo("USD"))
                .withQueryParam("to", equalTo("TRY"))
                .willReturn(aResponse().withStatus(200).withBody("34.567")));
        double r = client.rate("USD", "TRY");
        assertEquals(34.567, r, 1e-6);
        verify(getRequestedFor(urlPathEqualTo("/rate"))
                .withQueryParam("from", equalTo("USD"))
                .withQueryParam("to", equalTo("TRY")));
    }

    @Test
    @DisplayName("404 -> RuntimeException wrapping IllegalArgumentException")
    void notFound404() {
        stubFor(get(urlPathEqualTo("/rate"))
                .withQueryParam("from", equalTo("FOO"))
                .withQueryParam("to", equalTo("BAR"))
                .willReturn(aResponse().withStatus(404)));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> client.rate("FOO", "BAR"));
        assertNotNull(ex.getCause());
        assertEquals(IllegalArgumentException.class, ex.getCause().getClass());
        assertTrue(ex.getCause().getMessage().contains("unsupported currency"));
    }

    @Test
    @DisplayName("500 -> RuntimeException wrapping IllegalStateException")
    void server500() {
        stubFor(get(urlPathEqualTo("/rate"))
                .withQueryParam("from", equalTo("USD"))
                .withQueryParam("to", equalTo("EUR"))
                .willReturn(aResponse().withStatus(500)));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> client.rate("USD", "EUR"));
        assertNotNull(ex.getCause());
        assertEquals(IllegalStateException.class, ex.getCause().getClass());
        assertTrue(ex.getCause().getMessage().contains("remote error: 500"));
    }

    @Test
    @DisplayName("fault -> RuntimeException cause present")
    void faultConnectionReset() {
        stubFor(get(urlPathEqualTo("/rate"))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> client.rate("USD", "TRY"));
        assertNotNull(ex.getCause());
    }

    @Test
    @DisplayName("malformed body -> RuntimeException wrapping NumberFormatException")
    void malformedBody() {
        stubFor(get(urlPathEqualTo("/rate"))
                .willReturn(aResponse().withStatus(200).withBody("oops")));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> client.rate("USD", "TRY"));
        assertNotNull(ex.getCause());
        assertEquals(NumberFormatException.class, ex.getCause().getClass());
    }
}
