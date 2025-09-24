package capstone;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PricingService unit tests (solutions)")
class PricingServiceTest {

    PricingService svc;

    @BeforeEach
    void setUp() {
        svc = new PricingService(order -> { /* accept */ });
    }

    private static Order orderWithItems(int items, int unitPrice, boolean firstTime, boolean fragile) {
        Order o = new Order().firstTimeBuyer(firstTime).fragile(fragile);
        o.addLine(new OrderLine("SKU", items, unitPrice));
        return o;
    }

    @Test
    @DisplayName("basic order with 1 item, non-fragile, not first-time => subtotal 1000 + ship 1200 = 2200")
    void basicNonFragileNoDiscount() {
        Order o = orderWithItems(1, 1000, false, false);
        assertEquals(2200, svc.price(o));
    }

    @ParameterizedTest(name = "[{index}] items={0}, firstTime={1} -> expected={2}")
    @CsvSource({
            "2,true,3000",   // 2*1000=2000, 10% discount=200, ship=1200 => 3000
            "10,true,9000",  // 10*1000=10000, 10% discount=1000, free ship => 9000
            "11,true,9200",  // 11000 - (1100+700) + 0 = 9200
            "11,false,10300" // 11000 - 700 + 0 = 10300
    })
    void thresholdsFirstTimeAndBulk(int items, boolean firstTime, int expected) {
        Order o = orderWithItems(items, 1000, firstTime, false);
        assertEquals(expected, svc.price(o));
    }

    @Test
    @DisplayName("shipping threshold 9_999 / 10_000 / 10_001")
    void shippingThresholds() {
        Order a = new Order().firstTimeBuyer(false).fragile(false).addLine(new OrderLine("A",1,9999));
        Order b = new Order().firstTimeBuyer(false).fragile(false).addLine(new OrderLine("B",1,10000));
        Order c = new Order().firstTimeBuyer(false).fragile(false).addLine(new OrderLine("C",1,10001));
        assertEquals(11199, svc.price(a)); // 9999 + 1200
        assertEquals(10000, svc.price(b)); // 10000 + 0
        assertEquals(10001, svc.price(c)); // 10001 + 0
    }

    @Test
    @DisplayName("discount cap at 25% of subtotal")
    void discountCap() {
        // subtotal 1100; first-time + bulk would be 110 + 700 = 810; cap is 275 -> expect 275 used
        Order o = orderWithItems(11, 100, true, false);
        int price = svc.price(o);
        assertEquals(1100 - 275 + 1200, price); // 2025
    }

    @Test
    @DisplayName("fragile surcharge applies even if shipping is free")
    void fragileSurcharge() {
        // subtotal 10000 -> free shipping; fragile=true adds +500
        Order o = new Order().firstTimeBuyer(false).fragile(true)
                .addLine(new OrderLine("X", 2, 5000));
        assertEquals(10500, svc.price(o));
    }
}
