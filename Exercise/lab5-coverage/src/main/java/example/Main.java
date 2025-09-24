package example;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java -cp target/classes example.Main <weightGrams> <distanceKm> <fragile:true|false> <member:NONE|SILVER|GOLD>");
            return;
        }
        try {
            int weight = Integer.parseInt(args[0]);
            int distance = Integer.parseInt(args[1]);
            boolean fragile = Boolean.parseBoolean(args[2]);
            ShippingFeeCalculator.MemberType member = ShippingFeeCalculator.MemberType.valueOf(args[3].toUpperCase());
            ShippingFeeCalculator calc = new ShippingFeeCalculator();
            int fee = calc.calculate(weight, distance, fragile, member);
            System.out.printf("Fee=%d (weight=%d, distance=%d, fragile=%s, member=%s)%n", fee, weight, distance, fragile, member);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
