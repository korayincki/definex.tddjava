package example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HttpCurrencyClient + WireMock (solutions, cause-asserting)")
class HttpCurrencyClientTest {

    static WireMockServer wm;
    HttpCurrencyClient client;

    @BeforeAll
    static void beforeAll() {
        wm = new WireMockServer(0);
        wm.start();
        configureFor("localhost", wm.port());
    }

    @AfterAll
    static void afterAll() {
        if (wm != null) wm.stop();
    }

    @BeforeEach
    void setUp() {
        wm.resetAll();
        client = new HttpCurrencyClient(wm.baseUrl());
    }

    @Test
    @DisplayName("200 OK -> returns parsed double")
    void ok200_returnsDouble() {
        stubFor(get(urlPathEqualTo("/convert"))
                .withQueryParam("from", equalTo("USD"))
                .withQueryParam("to", equalTo("TRY"))
                .withQueryParam("amount", equalTo("10.0"))
                .willReturn(aResponse().withStatus(200).withBody("123.45")));

        double out = client.convert("USD", "TRY", 10.0);
        assertEquals(123.45, out, 1e-6);

        verify(getRequestedFor(urlPathEqualTo("/convert"))
                .withQueryParam("from", equalTo("USD"))
                .withQueryParam("to", equalTo("TRY"))
                .withQueryParam("amount", equalTo("10.0")));
    }

    @Test
    @DisplayName("404 -> RuntimeException whose cause is IllegalArgumentException('unsupported currency')")
    void notFound404_wrapsIllegalArgument() {
        stubFor(get(urlPathEqualTo("/convert"))
                .withQueryParam("from", equalTo("FOO"))
                .withQueryParam("to", equalTo("BAR"))
                .withQueryParam("amount", equalTo("1.0"))
                .willReturn(aResponse().withStatus(404)));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> client.convert("FOO", "BAR", 1.0));
        assertNotNull(ex.getCause());
        assertEquals(IllegalArgumentException.class, ex.getCause().getClass());
        assertTrue(ex.getCause().getMessage().contains("unsupported currency"));
    }

    @Test
    @DisplayName("500 -> RuntimeException whose cause is IllegalStateException('remote error: 500')")
    void server500_wrapsIllegalState() {
        stubFor(get(urlPathEqualTo("/convert"))
                .withQueryParam("from", equalTo("USD"))
                .withQueryParam("to", equalTo("EUR"))
                .withQueryParam("amount", equalTo("2.5"))
                .willReturn(aResponse().withStatus(500)));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> client.convert("USD", "EUR", 2.5));
        assertNotNull(ex.getCause());
        assertEquals(IllegalStateException.class, ex.getCause().getClass());
        assertTrue(ex.getCause().getMessage().contains("remote error: 500"));
    }

    @Test
    @DisplayName("timeout/connection fault -> RuntimeException wrapping I/O")
    void timeout_runtimeException() {
        stubFor(get(urlPathEqualTo("/convert"))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> client.convert("USD", "TRY", 10.0));
        assertNotNull(ex.getCause(), "should wrap the underlying I/O exception");
    }

    @Test
    @DisplayName("malformed body -> RuntimeException wrapping NumberFormatException")
    void malformedBody_runtimeException() {
        stubFor(get(urlPathEqualTo("/convert"))
                .willReturn(aResponse().withStatus(200).withBody("oops")));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> client.convert("USD", "TRY", 10.0));
        assertNotNull(ex.getCause());
        assertEquals(NumberFormatException.class, ex.getCause().getClass());
    }
}
