package example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ShippingFeeCalculator coverage tests (solutions)")
class ShippingFeeCalculatorTest {

    ShippingFeeCalculator calc;

    @BeforeAll static void beforeAll() { }
    @BeforeEach void setUp() { calc = new ShippingFeeCalculator(); }
    @AfterEach void tearDown() { }
    @AfterAll static void afterAll() { }

    // ---- Happy-path combinations ----

    @Test
    @DisplayName("small weight + short distance + non-fragile + NONE -> 1000")
    void smallShortNonFragileNone() {
        int fee = calc.calculate(400, 10, false, ShippingFeeCalculator.MemberType.NONE);
        assertEquals(1000, fee);
    }

    @Test
    @DisplayName("medium weight + mid distance + fragile + SILVER -> 2410")
    void mediumMidFragileSilver() {
        int fee = calc.calculate(1200, 50, true, ShippingFeeCalculator.MemberType.SILVER);
        assertEquals(2410, fee); // base 500+500+800+700=2500; discount on 1800 @5% = 90; 2500-90=2410
    }

    @Test
    @DisplayName("large weight + long distance + non-fragile + GOLD -> 2805")
    void largeLongNonFragileGold() {
        int fee = calc.calculate(2500, 150, false, ShippingFeeCalculator.MemberType.GOLD);
        assertEquals(2805, fee); // base 500+1200+1600=3300; 15% of 3300=495; 3300-495=2805
    }

    // ---- Boundaries ----

    @Test
    @DisplayName("weight boundary 500g (<=500 tier)")
    void weightBoundary500() {
        int fee = calc.calculate(500, 20, false, ShippingFeeCalculator.MemberType.NONE);
        assertEquals(1000, fee); // 500+200+300
    }

    @Test
    @DisplayName("weight boundary 2000g (<=2000 tier)")
    void weightBoundary2000() {
        int fee = calc.calculate(2000, 20, false, ShippingFeeCalculator.MemberType.NONE);
        assertEquals(1300, fee); // 500+500+300
    }

    @Test
    @DisplayName("distance boundary 20km (<=20 tier)")
    void distanceBoundary20() {
        int fee = calc.calculate(600, 20, false, ShippingFeeCalculator.MemberType.NONE);
        assertEquals(1300, fee); // 500+500+300
    }

    @Test
    @DisplayName("distance boundary 100km (<=100 tier)")
    void distanceBoundary100() {
        int fee = calc.calculate(600, 100, false, ShippingFeeCalculator.MemberType.NONE);
        assertEquals(1800, fee); // 500+500+800
    }

    // ---- Discount rule around fragile surcharge ----

    @Test
    @DisplayName("discount excludes fragile surcharge")
    void discountExcludesFragileSurcharge() {
        // base = 500 + 500(weight) + 300(distance) + 700(fragile) = 2000
        // discountable = 1300; GOLD 15% -> 195; expected = 2000 - 195 = 1805
        int fee = calc.calculate(600, 20, true, ShippingFeeCalculator.MemberType.GOLD);
        assertEquals(1805, fee);
    }

    // ---- Errors ----

    @Test
    @DisplayName("invalid: non-positive inputs")
    void invalidNonPositive() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculate(0, 10, false, ShippingFeeCalculator.MemberType.NONE));
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculate(10, 0, false, ShippingFeeCalculator.MemberType.NONE));
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculate(-1, 10, false, ShippingFeeCalculator.MemberType.NONE));
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculate(10, -1, false, ShippingFeeCalculator.MemberType.NONE));
    }

    @Test
    @DisplayName("invalid: fragile over 100km")
    void invalidFragileOver100() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculate(1000, 101, true, ShippingFeeCalculator.MemberType.SILVER));
    }
}
