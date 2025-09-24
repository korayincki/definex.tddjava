package example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Student skeleton – fill in tests to achieve high coverage.
 * Consider boundary tests at 500g/2000g and 20km/100km,
 * fragile true/false, and member NONE/SILVER/GOLD.
 */
@DisplayName("ShippingFeeCalculator coverage tests (student skeleton)")
class ShippingFeeCalculatorTest {

    ShippingFeeCalculator calc;

    @BeforeAll static void beforeAll() { }
    @BeforeEach void setUp() { calc = new ShippingFeeCalculator(); }
    @AfterEach void tearDown() { }
    @AfterAll static void afterAll() { }

    // ---- Valid scenarios ----

    @Test
    @DisplayName("small weight + short distance + non-fragile + NONE")
    void smallShortNonFragileNone() {
        // TODO: compute expected and assert
        throw new UnsupportedOperationException("implement test");
    }

    @Test
    @DisplayName("medium weight + mid distance + fragile + SILVER")
    void mediumMidFragileSilver() {
        // TODO: compute expected and assert
        throw new UnsupportedOperationException("implement test");
    }

    @Test
    @DisplayName("large weight + long distance + non-fragile + GOLD")
    void largeLongNonFragileGold() {
        // TODO: compute expected and assert
        throw new UnsupportedOperationException("implement test");
    }

    // ---- Boundaries ----

    @Test
    @DisplayName("weight boundary 500g")
    void weightBoundary500() {
        // TODO: add assertions to hit boundary
        throw new UnsupportedOperationException("implement test");
    }

    @Test
    @DisplayName("weight boundary 2000g")
    void weightBoundary2000() {
        // TODO: add assertions to hit boundary
        throw new UnsupportedOperationException("implement test");
    }

    @Test
    @DisplayName("distance boundary 20km")
    void distanceBoundary20() {
        // TODO: add assertions to hit boundary
        throw new UnsupportedOperationException("implement test");
    }

    @Test
    @DisplayName("distance boundary 100km")
    void distanceBoundary100() {
        // TODO: add assertions to hit boundary
        throw new UnsupportedOperationException("implement test");
    }

    // ---- Errors ----

    @Test
    @DisplayName("invalid: non-positive inputs")
    void invalidNonPositive() {
        // TODO: assert throws
        throw new UnsupportedOperationException("implement test");
    }

    @Test
    @DisplayName("invalid: fragile over 100km")
    void invalidFragileOver100() {
        // TODO: assert throws
        throw new UnsupportedOperationException("implement test");
    }
}
