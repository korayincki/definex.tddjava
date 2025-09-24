package capstone;

public class Main {
    public static void main(String[] args) {
        Order o = new Order().firstTimeBuyer(true).fragile(true);
        o.addLine(new OrderLine("A", 2, 3500));
        o.addLine(new OrderLine("B", 9, 800));

        PricingService svc = new PricingService(order -> {
            if (order.getLines().isEmpty()) throw new IllegalArgumentException("empty order");
        });

        int price = svc.price(o);
        System.out.println("Price (cents) = " + price);
    }
}
