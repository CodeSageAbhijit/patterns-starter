# Disease Statistics API

A Spring Boot application that exposes disease statistics through a small REST API. The project demonstrates Singleton, Adapter, Factory, Facade, and Chain of Responsibility patterns.

## Requirements

- Java 8
- Internet access for the first Maven dependency download
- Maven is not required globally; use the included Maven Wrapper
- Postman for endpoint verification

The Spring Boot version and Java version are defined in `pom.xml`.

## Run The Application

Open PowerShell in the project root:

```powershell
Set-Location "C:\Users\ADMIN\Downloads\patterns+starter\patterns starter"
```

Verify Java and Maven Wrapper:

```powershell
java -version
.\mvnw.cmd -v
```

Download dependencies and compile:

```powershell
.\mvnw.cmd clean compile
```

Run the tests:

```powershell
.\mvnw.cmd test
```

Start Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on `http://localhost:8080` unless another port is configured.

To build and run a packaged JAR instead:

```powershell
.\mvnw.cmd clean package
java -jar target\patterns-0.0.1-SNAPSHOT.jar
```

## Data Sources

The strategies use the fixture files supplied in `src/main/resources`:

- `DiseaseSh.json` contains the disease.io response for India.
- `JohnHopkins.json` contains John Hopkins statistics.

The source URLs are retained in `src/main/resources/application.yml` as configuration for the project specification. The current strategy implementations deserialize the supplied fixture files so endpoint tests remain deterministic and do not depend on external service availability.

## Design Patterns

### 1. Singleton: `RestServiceGenerator`

File: `src/main/java/com/upgrad/patterns/Config/RestServiceGenerator.java`

`GetInstance()` creates the configured `RestTemplate` only when the static instance is absent. Later calls return the same object. Both disease strategies obtain their `RestTemplate` from this generator.

### 2. Adapter: `IndianDiseaseStat`

Files:

- `src/main/java/com/upgrad/patterns/Interfaces/IndianDiseaseStat.java`
- `src/main/java/com/upgrad/patterns/Strategies/DiseaseShStrategy.java`
- `src/main/java/com/upgrad/patterns/Strategies/JohnHopkinsStrategy.java`

The interface gives both sources a common `GetActiveCount()` contract even though their JSON structures differ.

- `DiseaseShStrategy` maps the disease.io response to a rounded count.
- `JohnHopkinsStrategy` filters India records, reads each confirmed count, and sums the results.

### 3. Factory: `IndiaDiseaseStatFactory`

File: `src/main/java/com/upgrad/patterns/Service/IndiaDiseaseStatFactory.java`

`GetInstance(SourceType)` selects the appropriate `IndianDiseaseStat` implementation at runtime:

- `SourceType.DiseaseSh` selects `DiseaseShStrategy`.
- `SourceType.JohnHopkins` selects `JohnHopkinsStrategy`.
- An unsupported value raises `IllegalArgumentException`.

### 4. Facade: `DiseaseCountFacade`

File: `src/main/java/com/upgrad/patterns/Service/DiseaseCountFacade.java`

The facade hides factory and strategy selection from the controller. It exposes simple methods for the two count endpoints and for infected-ratio calculation.

### 5. Chain of Responsibility: Authentication

Files:

- `src/main/java/com/upgrad/patterns/Authentication/Authenticator.java`
- `src/main/java/com/upgrad/patterns/Middleware/AuthenticationProcessor.java`
- `src/main/java/com/upgrad/patterns/Middleware/JwtAuthProcessor.java`
- `src/main/java/com/upgrad/patterns/Middleware/BasicAuthProcessor.java`
- `src/main/java/com/upgrad/patterns/Filters/AuthFilter.java`

The chain is constructed as:

```text
JwtAuthProcessor -> BasicAuthProcessor
```

JWT authentication is attempted first. A non-JWT provider is passed to Basic Auth. An invalid JWT is rejected rather than silently falling back to Basic Auth.
The demo Basic Auth credentials are `India` / `India`; missing or incorrect credentials are rejected. This is assignment/demo authentication, not production credential management.



## API Verification With Postman

Start the application, import `Design+Patterns+Project.postman_collection (1).json`, and send the requests with the required authorization header.

| Request | Expected purpose |
|---|---|
| `GET http://localhost:8080/v1/disease/disease-sh-io/count` | Returns the DiseaseSh count |
| `GET http://localhost:8080/v1/disease/john-hopkins/count` | Returns the John Hopkins count |
| `GET http://localhost:8080/v1/disease/infected-ratio?sourceType=DiseaseSh` | Returns the DiseaseSh infected ratio |
| `GET http://localhost:8080/v1/disease/infected-ratio?sourceType=JohnHopkins` | Returns the John Hopkins infected ratio |

For an invalid `sourceType`, the infected-ratio endpoint returns HTTP 400 with the available source types.

## Verification Checklist

- [ ] `java -version` reports Java 8.
- [ ] `mvnw.cmd clean compile` finishes with `BUILD SUCCESS`.
- [ ] `mvnw.cmd test` passes the Spring context test.
- [ ] The application starts on port 8080.
- [ ] All Postman requests return responses.
- [ ] Requests without valid authentication are rejected.
- [ ] Packaged JAR can load the bundled JSON fixtures from the classpath.
- [ ] Both source strategies return data from their fixture files.
- [ ] Infected ratio works for both `DiseaseSh` and `JohnHopkins`.

## Contributors

| Name | Contribution |
|---|---|
| Abhijeet Kad | Project implementation, testing, and documentation |
