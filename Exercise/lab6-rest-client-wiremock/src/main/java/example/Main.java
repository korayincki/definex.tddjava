package example;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java -cp target/classes example.Main <baseUrl> <from> <to> <amount>");
            return;
        }
        String baseUrl = args[0];
        String from = args[1];
        String to = args[2];
        double amount = Double.parseDouble(args[3]);
        HttpCurrencyClient client = new HttpCurrencyClient(baseUrl);
        try {
            double result = client.convert(from, to, amount);
            System.out.printf("Converted: %s -> %s : %f%n", from, to, result);
        } catch (RuntimeException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
