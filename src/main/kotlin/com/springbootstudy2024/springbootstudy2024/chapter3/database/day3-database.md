# 3.2장 - 스프링 부트 애플리케이션 데이터베이스 연동

<hr/>

## 3.2.1 관계형 데이터베이스 (H2) 연동

책에서는 JPA 모듈을 사용하여 관계형 데이터베이스를 사용하는 예시를 보여줍니다.<br/>
JPA 모듈을 사용하기 위해서는 `org.springframework.boot:spring-boot-starter-data-jpa` 의존성을 추가하면 됩니다.

관계형 데이터베이스는 여러 가지 종류가 있지만, 책에서는 간단히 사용 가능한 H2 데이터베이스를 사용합니다.<br/>
H2 데이터베이스를 사용하기 위해서는 `com.database:h2` 의존성을 추가해야 합니다.

### H2 데이터베이스 설정

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  h2:
    console:
      enabled: true       # 활성화 시 웹 브라우저에서 /h2-console 경로로 H2 콘솔에 접속 가능
```

`spring.datasource.url`은 데이터베이스의 URL을 설정합니다.<br/>
예시에 적혀진 부분에서 `mem:testdb`는 testdb라는 이름의 **인메모리** 데이터베이스를 생성한다는 의미입니다.<br/>
인메모리 데이터베이스는 애플리케이션 실행 시마다 새로 생성되기 때문에, 애플리케이션을 재실행하면 데이터가 초기화됩니다.<br/>

### 데이터베이스 커넥션 풀

스프링 부트는 기본 데이터베이스 커넥션 풀로 [히카리 커넥션 풀](https://github.com/brettwooldridge/HikariCP) 을 사용합니다.<br/>
히카리 커넥션 풀은 `spring-boot-starter-data-jpa` 의존성 내부의 `spring-boot-starter-jdbc` 의존성에 포함되어 있습니다.<br/>

|spring-boot-starter-data-jpa|
|---|
|spring-boot-starter-jdbc|
|HikariCP|

기본 커넥션 풀을 다른 커넥션 풀로 대체하고 싶다면, `spring-boot-starter-data-jpa` 의존성 선언 시 HikariCP를 제외하고 오라클의 UCP, 톰캣의 JDBC, DBCP2 등 다른 커넥션 풀 의존관계를 추가하면 됩니다.

```kotlin
implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
        exclude("com.zaxxer:HikariCP")
    }
implementation("org.apache.tomcat:tomcat-jdbc")
```

위와 같은 예시로 설정된 스프링 부트의 데이터베이스 커넥션 풀 감지 전략은 다음과 같습니다:
1. HikariCP를 사용할 수 없으면 클래스패스에서 아파치 톰캣 데이터베이스 커넥션 풀을 찾아서 있으면 사용
2. HikariCP와 아파치 톰캣 커넥션 풀 둘 다 없으면 클래스패스에서 [아파치 커먼즈 DBCP2](https://commons.apache.org/proper/commons-dbcp) 를 찾아서 있으면 사용
3. DBCP2가 없으면 JDK의 기본 데이터 소스인 `javax.sql.DataSource` 사용

스프링 부트에서는 application.properies(혹은 application.yml) 파일에서 데이터베이스 설정을 커스터마이징 할 수 있는 파라미터들을 제공합니다.<br/>
예를 들어 히카리 커넥션 풀을 사용할 시 `spring.datasource.hikari.maximum-pool-size` 값을 지정하여 히카리 커넥션 풀의 최대 연결 개수를 지정할 수 있습니다.

## 3.2.2 몽고DB 연동
`org.springframework.boot:spring-boot-starter-data-mongodb` 의존성을 추가하면 스프링 부트에서 몽고DB를 사용할 수 있습니다.<br/>
추가적으로, `de.flapdoodle.embed:de.flapdoodle.embed.mongo` 의존성을 추가하면 별도의 몽고 DB를 설치하지 않고 스프링 부트 애플리케이션에 내장된 형태로 사용할 수 있습니다.

책에 나오는 예제들은 H2 데이터베이스 위주이기 때문에, 몽고DB 연동에 대한 세부 내용은 생략하겠습니다.

## 3.2.3 관계형 데이터베이스 초기화
3.2.1절에서 H2 데이터베이스를 연동하였지만, 데이터베이스를 실제로 사용하려면 적절한 스키마(테이블, 인덱스 등)가 초기화<sub>initialize</sub>되어있어야 합니다.

스프링 부트는 ORM<sub>Object Relational Mapping</sub>을 구현한 서드파티 라이브러리와 스프링 부트에 내장된 기능을 활용해서 데이터베이스를 초기화할 수 있습니다.

### schema.sql과 data.sql 스크립트로 데이터베이스 초기화하기
- schema.sql
  - 데이터 정의 언어<sub>Data Definition Language, DDL</sub>를 정의하는 파일입니다.
  - DDL은 데이터베이스 계정, 스키마, 테이블, 인덱스 제약 사항<sub>Constraint</sub> 등의 데이터베이스 구조를 정의할 때 사용합니다.
- data.sql
  - 데이터 조작 언어<sub>Data Manipulation Language, DML</sub>를 정의하는 파일입니다.
  - DML은 INSERT, UPDATE, DELETE 등 데이터베이스에서 데이터를 조작하는 데 사용합니다.

스프링 부트는 기본적으로 `src/main/resources` 경로에 위치한 `schema.sql`과 `data.sql` 파일을 자동으로 실행합니다.<br/>
하지만 application.properties(혹은 application.yml) 파일에서 스키마 파일과 데이터 파일의 이름 및 경로를 직접 설정할 수도 있습니다.
```yaml
spring:
  sql:
    init:
      schema-locations: {스키마 파일 경로}/{스키마 파일 이름}.sql
      data-locations: {데이터 파일 경로}/{데이터 파일 이름}.sql
```
이 때 스키마 파일과 데이터 파일은 쉼표를 사용에서 여러 개를 지정할 수도 있습니다.

### 데이터베이스에 특화된 스키마 및 데이터 파일
스프링 부트에서 여러 종류의 데이터베이스를 활용한다면, 각 데이터베이스에 맞는 schema.sql과 data.sql 파일을 작성해야 합니다.<br/>
이러한 경우 파일 이름을 다음과 같이 지정하면 각 플랫폼에 맞는 스크립트를 실행할 수 있습니다:
- `schema-${platform}.sql`
- `data-${platform}.sql`

데이터베이스 플랫폼은 `spring.datasource.platform` 프로퍼티로 지정할 수 있습니다.<br/>
다음과 같이 설정한다면 애플리케이션은 H2 데이터베이스를 사용하고, schema-h2.sql과 data-h2.sql 파일을 실행합니다:
```yaml
spring:
  datasource:
    platform: h2
```
