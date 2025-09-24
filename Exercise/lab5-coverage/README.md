# Lab 5 — Coverage

**Goal:** Practice writing tests that achieve high **statement/branch coverage** using JaCoCo, while keeping tests readable and meaningful.

## Application
`example.ShippingFeeCalculator` computes shipping fees based on weight, distance, fragility, and membership status.

Key rules (see source):
- Weight & distance must be `> 0`.
- Fragile items **not allowed** if distance `> 100` km.
- Tiered fees by weight and distance.
- Extra fee for `fragile`.
- Membership discount (%) applied **excluding** the fragile surcharge portion.

## Tasks
1. Open `ShippingFeeCalculatorTest.java` and complete tests for:
   - Valid combinations that hit **all weight tiers** and **distance tiers**.
   - `fragile` true/false branches and GOLD/SILVER/NONE discounts.
   - Error cases: non-positive inputs; fragile over 100 km.
2. Measure coverage with JaCoCo and aim for **> 90% line** and **> 90% branch** coverage.
3. (Optional) Add a few **boundary tests** (exact thresholds: 500g, 2000g, 20km, 100km).

## How to run tests
```bash
mvn -q test
```

Open coverage after a run:
```
target/site/jacoco/index.html
```

### Console Result Table (Surefire)
To print a summary table of test results in the console after tests:
```bash
mvn -q verify
```
This runs a helper (`tools.SurefireTablePrinter`) bound to the `verify` phase via the `exec-maven-plugin` and prints a neat ASCII table by reading `target/surefire-reports`.

---

## CLI Demo
A tiny CLI is included to try scenarios from the shell (not part of coverage).

Build and run:
```bash
mvn -q -DskipTests compile
java -cp target/classes example.Main 450 10 false NONE
java -cp target/classes example.Main 1200 50 true SILVER
java -cp target/classes example.Main 2500 150 false GOLD
```

Usage:
```
java -cp target/classes example.Main <weightGrams> <distanceKm> <fragile:true|false> <member:NONE|SILVER|GOLD>
```
