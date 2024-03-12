### 7.1 스프링 부트 RESTful API 개발

- REpresentational State Transfer
- 사용 포맷은 JSON이 가장 대중적
- REST 구성 요소
  - 자원(Resource), URL
  - 행위(Verb), Method
  - 표현(Representation)
- REST의 특징
  - Uniform (유니폼 인터페이스)
  - Stateless (무상태성)
  - Cacheable (캐시 가능)
  - Self-descriptiveness (자체 표현 구조)
  - Client - Server 구조
  - 계층형 구조
- Methods
  - GET
  - POST
  - PUT
  - DELETE
  - PATCH


### 7.2 스프링 부트 RESTful API 예외 처리
* 긴 스택 트레이스 반환하면 불편
* 애플리케이션 내부 세부 내용이 공개되어 보안상 좋지 않음
* 걍 무적권 500으로 날라가서 에러 종류도 알 수 없음
* -> 예외 발생 시 클라이언트가 무슨 문제가 생긴건지 알 수 있게 해야댐

```
"error": "Internal Server Error",
"message": "No class com.manning-sbip.ch07 model.Course entity with id 10 exists!
"path": "/courses/10",
"status": 500,
"timestamp": "2021-06-23T16:38:20.105+00:00"
"trace": "org.springframework.dao.EmptyResultDataAccessException: No class com.manning.
sbip.ch07. model. Course entity with id 10 exists!
at org. springframework.data.jpa.repository.support.SimpleJpaRepository.lambda$deleteById
$0 (SimpleJpaRepository java: 166)
at java.base/java.util.Optional.orElseThrow(Optional.java:401)
at org.springframework.data.jpa.repository.support.SimpleJpaRepository.
deleteById (SimpleJpaRepository-java:165)
at java.base/jdk.internal. reflect.NativeMethodAccessorImpl.invoke0(NativeMethod)
at java.base/jdk.internal. reflect.NativeMethodAccessorImpl.
invoke (NativeMethodAccessorImpl. java:64)
//이하 생략스
```
### 7.3 RESTful API 테스트

```
build.gralde.kts
    implementation("org.springdoc", "springdoc-openapi-starter-webmvc-ui", "2.2.0")

```


### 7.4 RESTful API 문서화
* OenAPI: API 작성 가이드라인. 명세, Swagger: OpenAPI 명세를 통해 만든 도구
* Swagger includes (https://swagger.io/blog/api-strategy/difference-between-swagger-and-openapi/)
  * Swagger Editor
  * Swagger UI
  * Swagger Codegen
  * Swagger Parser
  * Swagger Core
  * Swagger Inspector
  * Swagger Hub
