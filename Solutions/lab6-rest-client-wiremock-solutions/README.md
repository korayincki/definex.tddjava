# Lab 6 — REST Client Testing with WireMock — Solutions (Fixed)

**Note:** `HttpCurrencyClient` wraps all non-success flows in a `RuntimeException` (see `catch (Exception e) { throw new RuntimeException(e); }`).  
Therefore, tests assert `RuntimeException` and then check the **cause type/message**.

## Run
```bash
mvn -q test
mvn -q verify
```
Coverage:
```
target/site/jacoco/index.html
```
