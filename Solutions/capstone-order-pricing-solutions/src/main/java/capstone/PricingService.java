package capstone;

public class PricingService {

    private final OrderValidator validator;

    public PricingService(OrderValidator validator) {
        this.validator = validator;
    }

    public int price(Order order) {
        validator.validate(order);

        int subtotal = order.subtotal();
        int items = order.totalItems();

        long discount = 0;
        if (order.isFirstTimeBuyer() && items >= 2) {
            discount += Math.round(subtotal * 0.10);
        }
        if (items > 10) {
            discount += 700;
        }
        int discountCap = (int) Math.round(subtotal * 0.25);
        if (discount > discountCap) discount = discountCap;

        int shipping = (subtotal >= 10_000 ? 0 : 1200) + (order.isFragile() ? 500 : 0);

        long result = (long) subtotal - discount + shipping;
        if (result < 0 || result > Integer.MAX_VALUE) throw new ArithmeticException("price overflow");
        return (int) result;
    }
}
