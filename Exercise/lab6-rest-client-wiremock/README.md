# Lab 6 — REST Client Testing with WireMock

**Goal**: Verify HTTP client behavior via **stubs** (no real network).  
**Required**: Cover **200 / 404 / 500** mappings. Add **timeout** and **malformed body** as extensions.

## Application under test
`example.HttpCurrencyClient` calls `GET /convert?from=...&to=...&amount=...` on a remote service and:
- returns the parsed **double** amount on **200 OK**,
- throws `IllegalArgumentException("unsupported currency")` on **404**,
- throws `IllegalStateException("remote error: <code>")` on other non-2xx,
- wraps I/O errors as `RuntimeException`.

You will **not** call a real server; WireMock simulates the server responses.

## Tasks
1. Implement `HttpCurrencyClientTest` using WireMock:
   - Stub **200** to return a numeric string (e.g., `"123.45"`).
   - Stub **404** to simulate unsupported currency.
   - Stub **500** to simulate server error.
   - (Extension) Simulate **timeout** using delayed responses.
   - (Extension) Return a **malformed body** (e.g., `"NaN?"`) and assert behavior.
2. Use JUnit 5 lifecycle: `@BeforeAll/@AfterAll` to start/stop `WireMockServer`.
3. Verify call path using WireMock’s `verify(getRequestedFor(...))` if desired.
4. Check coverage after implementing tests.

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
Build and run a quick call against a base URL:
```bash
mvn -q -DskipTests compile
java -cp target/classes example.Main http://localhost:8080 USD TRY 10
```
*(Use WireMock or any HTTP server to provide responses on `/convert`.)*

---

## Expected results
- Clear mapping from HTTP status/body to return/exception.
