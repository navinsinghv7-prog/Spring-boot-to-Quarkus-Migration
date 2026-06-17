# Quarkus Migration Notes

## Quarkus extensions used

- `io.quarkus:quarkus-rest`
- `io.quarkus:quarkus-arc`
- `io.quarkus:quarkus-hibernate-orm-panache`
- `io.quarkus:quarkus-jdbc-h2`
- `io.quarkus:quarkus-junit5`
- `io.quarkus:quarkus-junit5-mockito`

## Significant technical challenge

The most significant challenge was preserving exact transactional behavior while moving from Spring Data JPA and Spring transaction annotations to Quarkus Panache and Jakarta Transactions. In Spring, repository `save` calls and `Optional` retrieval patterns are common defaults, while Panache favors `persist` and direct entity lookups. The service layer had to be adapted to Panache APIs without changing the purchase flow semantics, especially ensuring that `InsufficientFundsException` still aborts the transaction and prevents partial writes.

Another key challenge was keeping endpoint behavior identical while replacing Spring MVC with JAX-RS. The controller was migrated to RESTEasy Reactive annotations and still returns the same status codes/messages for success, insufficient funds, and unexpected errors. Tests were also adjusted from Spring test utilities to Quarkus test tooling so response contracts and service interactions remain validated after the framework migration.

## Run in dev mode

```bash
mvn quarkus:dev
```
