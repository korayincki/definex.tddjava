package capstone;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PricingService interaction tests (solutions)")
class PricingServiceInteractionTest {

    @Mock OrderValidator validator;
    PricingService svc;

    @BeforeEach
    void setUp() {
        svc = new PricingService(validator);
    }

    @Test
    @DisplayName("validator is called once on happy path")
    void validatorCalledOnce() {
        Order o = new Order().firstTimeBuyer(false).fragile(false)
                .addLine(new OrderLine("X", 1, 1000));
        int out = svc.price(o);
        assertEquals(2200, out);
        verify(validator, times(1)).validate(o);
        verifyNoMoreInteractions(validator);
    }

    @Test
    @DisplayName("validator throws -> pricing does not proceed")
    void validatorThrows_haltingCalculation() {
        doThrow(new IllegalArgumentException("bad order")).when(validator).validate(any(Order.class));
        Order o = new Order().firstTimeBuyer(false).fragile(false)
                .addLine(new OrderLine("X", 1, 1000));
        assertThrows(IllegalArgumentException.class, () -> svc.price(o));
        verify(validator, times(1)).validate(o);
        verifyNoMoreInteractions(validator);
    }
}
