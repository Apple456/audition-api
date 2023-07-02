# Audition API

The purpose of this Spring Boot application is to test general knowledge of SpringBoot, Java, Gradle etc. It is created
for hiring needs of our company but can be used for other purposes.

## Overarching expectations & Assessment areas

- clean, easy to understand code
- good code structures
- Proper code encapsulation
- unit tests with minimum 80% coverage.
- A Working application to be submitted.
- Observability. Does the application contain Logging, Tracing and Metrics instrumentation?
- Input validation.
- Proper error handling.
- Ability to use and configure rest template. We allow for half-setup object mapper and rest template
- Not all information in the Application is perfect. It is expected that a person would figure these out and correct.

## Getting Started

### Prerequisite tooling

- Any Springboot/Java IDE. Ideally IntelliJIdea.
- Java 17
- Gradle 8

### Prerequisite knowledge

- Java
- SpringBoot
- Gradle
- Junit

### Importing Google Java codestyle into INtelliJ

```
- Go to IntelliJ Settings
- Search for "Code Style"
- Click on the "Settings" icon next to the Scheme dropdown
- Choose "Import -> IntelliJ Idea code style XML
- Pick the file "google_java_code_style.xml" from root directory of the application
__Optional__
- Search for "Actions on Save"
    - Check "Reformat Code" and "Organise Imports"
```

---
**NOTE** -
It is highly recommended that the application be loaded and started up to avoid any issues.

---

## Audition Application information

This section provides information on the application and what the needs to be completed as part of the audition
application.

The audition consists of multiple TODO statements scattered throughout the codebase. The applicants are expected to:

- Complete all the TODO statements.
- Add unit tests where applicants believe it to be necessary.
- Make sure that all code quality check are completed.
- Gradle build completes sucessfully.
- Make sure the application if functional.

## Submission process

Applicants need to do the following to submit their work:

- Clone this repository
- Complete their work and zip up the working application.
- Applicants then need to send the ZIP archive to the email of the recruiting manager. This email be communicated to the
  applicant during the recruitment process.

---

## Additional Information based on the implementation

## Api documentation

- http://localhost:8080/api/swagger-ui/index.html
- http://localhost:8080/api/v3/api-docs

## Application Build

```sh 
./gradlew clean build
```

### Full list of available gradle tasks

```sh 
./gradlew tasks
```

### Build Reports

Under /build

| Report          | Path                            |
|-----------------|---------------------------------|
| Jacoco          | /jacocoHtml/index.html          |
| CheckStyle main | /reports/checkstyle/main.html   |
| CheckStyle test | /reports/checkstyle/test.html   |
| PMD Main        | /reports/pmd/main.html          |
| PMD Test        | /reports/pmd/test.html          |
| SpotBug         | /reports/spotbugs/spotbugs.html |

## Application Run

```sh
java -jar build/libs/audition-api-0.0.1-SNAPSHOT.jar
```

### Notes

---
[application.yml](src%2Ftest%2Fresources%2Fapplication.yml) contains properties to enable/disable log of request and
response for the integration clients.

- application.config.interceptor.logRequest
- application.config.interceptor.logResponse

This has been implemented
in [RestTemplateRequestResponseLoggingInterceptor.java](src%2Fmain%2Fjava%2Fcom%2Faudition%2Fcommon%2Flogging%2FRestTemplateRequestResponseLoggingInterceptor.java)

these variables can also be overridden at application start

```shell
java -jar build/libs/audition-api-0.0.1-SNAPSHOT.jar --application.config.interceptor.logRequest=false --application.config.interceptor.logResponse=true
```

---

####                         

Basic auth has been added to *protect* the endpoint for `/actuator/bean`, allowing access only to users with ADMIN role

User defined in [application.yml](src%2Fmain%2Fresources%2Fapplication.yml)[application.yml] and
security configured in [WebSecurity.java](src%2Fmain%2Fjava%2Fcom%2Faudition%2Fconfiguration%2FWebSecurity.java)

Example request to actuator/beans authenticated

```sh
curl --location 'http://localhost:8080/api/actuator/beans' \
--header 'Authorization: Basic YWRtaW46bXktc2VjcmV0LXBhc3N3b3Jk'
```

#### Example requests not authenticated to other actuator endpoints samples

```sh 
curl --location 'http://localhost:8080/api/actuator/health'
```

```sh 
curl --location 'http://localhost:8080/api/actuator/info'
```

---

#### Integration tests have been implemented using `https://app.wiremock.cloud/`, mocks can be regenerated using [auditionapi-stubs.json](src%2Ftest%2Fresources%2Fauditionapi-stubs.json)

#### Postman collection to interact with the application is available [AuditionApi.postman_collection.json](src%2Ftest%2Fresources%2FAuditionApi.postman_collection.json) (select environment)

#### TODO in [ResponseHeaderInjector.java](src%2Fmain%2Fjava%2Fcom%2Faudition%2Fconfiguration%2FResponseHeaderInjector.java) has not been implemented, just an example has been given of possible implementation of the response header injection

##### Please note unit and integration tests are at the very minimum required, different types of test should allow the interviewer to understand which kind of scenarios would be implemented.  