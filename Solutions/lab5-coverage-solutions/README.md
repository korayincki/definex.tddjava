# Lab 5 — Coverage — Solutions

This project contains **passing tests** for `example.ShippingFeeCalculator` targeting high line and branch coverage (JaCoCo).

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
```bash
mvn -q -DskipTests compile
java -cp target/classes example.Main 450 10 false NONE
java -cp target/classes example.Main 1200 50 true SILVER
java -cp target/classes example.Main 2500 150 false GOLD
```
