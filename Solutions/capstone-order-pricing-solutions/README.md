# Capstone — Order Pricing Suite — Solutions

This project contains **passing tests** and a complete implementation of `PricingService.price(Order)` plus interaction and WireMock tests.

## How to run
```bash
mvn -q test
mvn -q verify   # prints the Surefire console table
```

Open coverage:
```
target/site/jacoco/index.html
```

### What’s covered
- First-time + bulk discounts with **25% cap**
- Shipping threshold at **≥ ₺100** (10_000 cents), fragile surcharge +500
- Data-driven thresholds for items (1,2,10,11) and totals (9_999/10_000/10_001)
- Mockito interaction tests for `OrderValidator`
- WireMock HTTP stubs for `CurrencyClient` (200/404/500/fault/malformed)

