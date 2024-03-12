# Chaper 4 스프링 자동 구성과 액추에이터
### 4.1 스프링 부트 자동 구성 이해
- 스프링 부트가 인기를 끌게된 이유? -> 스프링 개발에 필요한 컴포넌트를 자동으로 설정
- 자동 구성은 사용할 어플리케이션 컴포넌트를 적절히 추론하고 기본 설정 값을 자동으로 구성해서 어플리케이션을 초기화
- ex)
  - spring-boot-starter-web 의존 관계를 추가하면 웹 서버가 필요할 것이라고 추론하고 아파치 톰캣 웹 서버를 기본 웹서버로 추가
  - 물론 개발자는 다른 웹 서버를 사용하도록 지정할 수 있음

- 스프링 설정 빈을 모든 팀에서 복사해서 사용하고 있다는 사실을 발견 => 공통으로 사용되는 빈을 추출해서 공통 어플리케이션 컨텍스트 설정에 모아서 사용하려고 한다.
```java
package com.manning.sbip.ch04;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonApplicationContextConfiguration {

  // 여러 팀에서 공통으로 사용되는 RelationalDataSourceConfiguration 스프링 빈 생성
  @Bean
  public RelationalDataSourceConfiguration dataSourceConfiguration() {
    return new RelationalDataSourceConfiguration();
  }
}
```
- @Configuration annotation이 붙어 있는 클래스는 스프링 설정을 담당한다.

- CommonApplicationContextConfiguration는 별도의 분리된 프로젝트에 존재하는 설정 클래스로 이 설정 클래스를 포함하는 프로젝트는 메이븐 혹은 그레이들 컴포넌트로 배포되고 개발 팀에서는 이 프로젝트를 의존 관계로 추가해서 설정 클래스를 사용할 수 있다.
- RelationalDataSourceConfiguration 클래스는 관계형 데이터베이스를 초기화하는 데이터 소스 설정 빈이다. 이를 추출해서 설정의 중복과 복사를 막을 수 있다.
- 공통으로 사용되는 다른 빈도 여러 개 추가할 수 있다.

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CommonApplicationContextConfiguration.class)
public class CommonPaymentContextConfiguration{
  // 다른 빈 정의 내용 생략
}
```

#### 4.1.1 @Conditional annotation 이해
- @Conditional annotation을 사용해서 코드가 실행되지 않고 빈도 생성되지 않도록 막을 수 있다.
- Condition 인터페이스를 구현한 클래스를 인자로 받는다.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonApplicationContextConfiguration {

  // 여러 팀에서 공통으로 사용되는 RelationalDataSourceConfiguration 스프링 빈 생성
  @Bean
  @Conditional(RelationDatabaseCondition.class)
  public RelationalDataSourceConfiguration dataSourceConfiguration() {
    return new RelationalDataSourceConfiguration();
  }
}
```

```java
public class RelationalDatabaseCondition implements Condition {

  @Override
  public boolean matches(
    ConditionContext conditionContext,
    AnnotatedTypeMetadata annotatedTypeMetadata) {
      return (true or false);
  }
}
```

- 예제는 간단하지만 일반적으로 Condition 구현체는 다음 두 가지 방식으로 구현한다.
  - 특정 라이브러리가 클래스패스에 존재하는지 확인한다.
  - ConditionContext를 통해 application.properties 파일에 정의된 어플리케이션 설정 프로퍼티에 접근해서 특정 프로퍼티가 정의되어 있는지 확인한다.
- p.162 ~ p.163에서 다양한 조건을 쉽게 사용해줄 수 있게 만드는 annotation을 확인할 수 있다.

### 4.2 스프링 부트 개발자 도구
- 스프링 부트는 개발 과정에서 필요한 기능을 제공하고 이를 통해 개발 경험을 개선하고 생산성을 높일 수 있다.
- gradle에서는 다음과 같이 파일을 작성하면 spring-boot-devtools를 사용할 수 있다.
```
dependencies {
    compileOnly("org.springframework.boot:spring-boot-devtools")
}
```
#### 4.2.1 프로퍼티 기본값
- 캐시 기능을 지원하는 라이브러리는 상용 환경에서는 매우 유용하지만 개발 단계에서는 생산성을 떨어뜨리기도 한다.
- org.springfraework.boot.devtools.env 패키지에 있는 DevToolsPropertyDefaultsPostProcessor 클래스를 사용해서 기본적으로 캐시 기능을 모두 비활성화한다.

#### 4.2.2 자동 재시작
- 일반적으로 개발 환경을 구성한 후 소스 코드를 변경하고 변경 내용을 확인하려면 어플리케이션을 재시작해야한다.
- spring-boot-dev-tools는 classpath에 변경 사항이 있을 때마다 자동으로 어플리케이션을 재시작 해준다.

#### 4.2.3 라이브 리로드
- 웹 페이지를 구성하는 리소스가 변경됐을 때 브라우저 새로고침을 유발하는 내장된 라이브 리로드 서버가 포함되어 있다.

### 4.3 커스텀 실패 분석기 생성
- 실패 분석기는 크게 두 가지 관점에서 유용하다.
  - 실제 발생한 에러에 대한 상세한 메시지를 제공한다.
  - 어플리케이션 시작 시점에 검증을 수행해서 발생할 수 있는 에러를 가능한 한 일찍 파악할 수 있게 해준다.

#### 4.3.1 기법: 커스텀 스프링 부트 실패 분석기 생성
- 요구사항: 의존하는 REST 서비스를 사용할 수 있는지 어플리케이션 시작 시점에 확인해야 한다. 또한 서비스 사용 불가 시 상세한 내용을 확인할 수 있어야 한다.
- 해법: 실패 분석기 만들기

- 스프링 부트의 ContextRefreshedEvent를 사용해서 검증 프로세스를 구동한다. Application-Context가 갱신되면 ContextRefreshedEvent를 발행한다.
- API가 사용할 수 없는 상태이면 개발자가 작성한 런타임 에러인 UrlNotAccessibleException을 던진다.
- UrlNotAccessibleException 예외가 던저지면 호출되는 UrlNotAccessibleFailureAnalyzer를 작성한다.
- spring.factories 파일에 UrlNotAccessibleFailureAnalyzer를 추가한다.
  - spring.factories: 어플리케이션 시작 시점에 스프링으로 로딩하는 특수 파일로 여러 가지 설정 클래스에 대한 참조가 포함되어 있다.
```java
package com.manning.sbip.ch04.exception;

import lombok.Getter;

@Getter
public class UrlNotAccessibleException extends RuntimeException {
  private String url;

  public UrlNotAccessibleException(String url) {
    this(url, null);
  }

  public UrlNotAccessibleException(String url, Throwable cause) {
    super("URL " + url + " is not accessbile", cause);
    this.url = url;
  }
}
```

```java
package com.manning.sbip.ch04.listener;

// import 문 생략

@Component
public class UrlAccessibilityHandler {

  @Value($"{api.url:url}")
  private String url;

  @Eventlistener(classes = ContextRefreshedEvent.class)
  public void listen(){
    // 원래는 접근 확인하고 던져야 함
    throw new UrlNotAccessbileException(url);
  }
}
```

```java
package com.manning.sbip.ch04.exception;

// import 문 생략

public class UrlNotAccessibleFailureAnalyzer extends AbstractFailureAnalyzer<UrlNotAccessibleEXception> {
  @Override
  protected FailureAnalysis analyze(Throwable rootFailure, UrlNotAccessibleException cause){
    return new FailureAnalysis("", "", cause);
  }
}
```
```
org.springframework.boot.diagnotics.FailureAnalyzer=\
com.manning.sbip.ch04.exception.UrlNotAccessibleFailureAnalyzer
```

p.171 그림 4.1

### 4.4 스프링 부트 액추에이터
- 스프링 부트는 어플리케이션 개발에 필요한 핵심 기능뿐만 아니라 어플리케이션 운영에 필요한 기능을 제공한다.
- 고객에게 문제 없이 매끄럽게 서비스를 제공하기 위해서는 어플리케이션을 지속적으로 모니터링하고 관리해야 한다.
- 모니터링과 관리에는 어플리케이션 상태 점검, 성능, 트래픽, 감사, 각종 측정 지표, 재시작, 로그 레벨 변경 등 다양한 작업이 포함된다.

#### 4.4.1 기법: 스프링 부트 액추에이터 설정
- 의존 관계 추가
```
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```
- 추가하면 http://localhost:8080/actuator/health에 접속해서 화면을 확인할 수 있다.
- 이 엔드포인트에서 확인할 수 있는 정보도 커스터마이징 할 수 있고 4장 뒷부분에서 다룬다.

#### 4.4.2 스프링 부트 액추에이터 엔드포인트 이해
- health 이외에도 http 또는 jmx를 통해 호출할 수 있는 엔드포인트가 있고 이를 활성화/비활성화할 수 있다.
- http://localhost:8080/actuator에 접속하면 현재 접속할 수 있는 액추에이터 엔드포인트 목록이 표시된다.
- p.175, p.176에 여러 엔트포인트가 표시된다.

#### 4.4.3 스프링 부트 액추에이터 엔드포인트 관리
- application.properties의 management.endpoints.web.exposure의 include, exclude 프로퍼티 설정을 통해 다른 엔드포인트를 노출하거나 노출하지 않을 수 있다.
```
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=threaddump,heapdump,health
```
- managetment.server.port를 설정하면 액추에이터 엔드포인트에 접근할 수 있는 포트를 설정할 수 있다.

#### 4.4.4 Health 엔드포인트 탐구
- 스프링부트는 다양한 HealthIndicator 구현체를 제공한다.
- 
- 상세히 보기
```
management.endpoint.health.show-details=always
```
- status: UP | DOWN | OUT-OF-SERVICE | UNKNOWN
  - UP: 컴포넌트가 의도한 대로 동작하는 상태
  - DOWN: 컴포넌트를 정상적으로 사용할 수 없는 상태
  - OUT-OF-SERVICE: 컴포넌트가 일시적으로 동작하지 않는 상태
  - UNKNOWN: 컴포넌트 상태를 알 수 없는 상태

#### 4.4.5 커스텀 스프링 부트 HealthIndicator 작성, 4.4.6 기법 : 커스텀 스프링 부트 액추에이터 HealthIndicator 정의
- 기본으로 제공하는 HealthIndicator을 활용할 수도 있지만 연동하는 다른 REST API 시스템의 상태를 보여주도록 커스텀하게 만들 수 있다.

- 요구사항: 어플리케이션이 의존하고 있는 핵심 외부 REST API 시스템의 상태를 health 액추에이어 엔드포인트를 통해 확인할 수 있다.

```java
package com.manning.sbip.ch04.health.indicator;

// import 문 생략

@Component
public class DogsApiHealthIndicator implements HealthIndicator {

  public Health health() {
    try {
      // api 상태에 따라 Health값 반환
      // return Health.down().withDetails("status", ...).build();
      return Health.status("FATAL").build(); // custom한 status도 생성 가능능
    }
    catch(RestClientException ex){
      return Health.down().withException(ex).build();
    }
  }
}
```

