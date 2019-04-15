Study Spring Retry and Spring Data with multiple datasources (2 DataSource)

- Setup 2 datasource with Hikari Connection Pool, separate JPA configurations
- Setup HttpConnectionPool
- Call API Async
 
# Load Configurations Manually
### Reference
 - `JpaBaseConfiguration.class`
 - `EntityManagerFactoryBuilder.class`
 - `JpaProperties.class` (Spring will load automatically these configurations that have prefix **_spring.data_**)
 
# Call API Async
### Combine Spring Async Method + CompletableFuture
 - Config ThreadPoolTaskExecutor for async method
 - Enable Async
 - The async method have to return CompletableFuture
 - Apply `CompletableFuture.allOf(completableTasks).join();` to join/wait all completed workers
 - Reference:
    - `GatewayStatusImporterImpl.class`
    - `GatewayStatusScheduler.class`