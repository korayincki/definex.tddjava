package example;

public class ShippingFeeCalculator {
    public enum MemberType {
        NONE, SILVER, GOLD
    }

    public int calculate(int weightGrams, int distanceKm, boolean fragile, MemberType member) {
        if (weightGrams <= 0 || distanceKm <= 0)
            throw new IllegalArgumentException("weight and distance must be > 0");
        if (fragile && distanceKm > 100)
            throw new IllegalArgumentException("fragile items >100km not allowed");
        int base = 500;
        if (weightGrams <= 500)
            base += 200;
        else if (weightGrams <= 2000)
            base += 500;
        else
            base += 1200;
        if (distanceKm <= 20)
            base += 300;
        else if (distanceKm <= 100)
            base += 800;
        else
            base += 1600;
        if (fragile)
            base += 700;
        int discountable = base - (fragile ? 700 : 0);
        int discount = switch (member) {
            case GOLD -> (int) Math.round(discountable * 0.15);
            case SILVER -> (int) Math.round(discountable * 0.05);
            default -> 0;
        };
        return base - discount;
    }
}
