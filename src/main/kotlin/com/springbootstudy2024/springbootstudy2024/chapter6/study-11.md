### 6.6 리멤버 미 구현

- 어플리케이션과 사용자의 연결 세션 사이에 정보를 공유하여 인증 정보 입력 빈도를 줄여주는 것을 리멤버 미 기능이라고 한다.
- 스프링에서는 해당 기능을 지원하며 SecurityConfiguration을 구현할 때 아래와 같이 추가하여 구현 가능

```kotlin
            .formLogin {
  it.loginPage("/login")
    .defaultSuccessUrl("/index", true).permitAll()
    .failureHandler(customAuthenticationFailureHandler)
}
  // Chapter 6.6 Remember me 기능 구현하기
  .rememberMe {
    it.key("remember-me-key").rememberMeCookieName("course-tracker-remember-me")
  }
  .logout {
    it.deleteCookies("course-tracker-remember-me")
  }
```

- 이 설정을 추가하고 로그인을 진행하면, 브라우저 쿠키에 리멤버 미 value가 들어가있는 것을 볼 수 있음.
- **다만 이렇게 쿠키에 값을 넣어서 진행하는 방법은 악의적인 공격자가 해당 값을 탈취해서 이용할 수 있으므로 추천되지 않음.**
- 쿠키에 값을 넣지 않고도 DB 기반의 퍼시스턴트 토큰 방식도 지원함.
  <br>
  <br>
  <br>
  <br>
  <br>

### 6.7 리캡차 구현

- 캡차를 통해서 사용자와 스팸을 유발하는 봇(bot)을 구별할 수 있다.
- 사용자에게 약간의 불편함을 제공하지만 효과적으로 스팸을 막을 수 있다는 점이 큰 장점이다.
- 여기서는 reCAPTCHA를 통해서 리캡차를 구현함.
- reCAPTCHA 발급은 google에서 진행할 수 있으며 받아온 site key와 secret key로 리캡차를 구현할 수 있다.

```kotlin
@Service
class GoogleRecaptchaService {
  private val VERIFY_URL =
    "https://www.google.com/recaptcha/api/siteverify" + "?secret={secret}&remoteip={remoteip}&response={response}"

  private val restTemplate = RestTemplate()

  private var secret: String = "6LdrdIEpAAAAAG8l2iCKTThCM90Ws1WPxjK5RUyH"

  fun verify(
    ip: String,
    recaptchaResponse: String
  ): RecaptchaDto {
    val request = HashMap<String, String>()
    request["remoteip"] = ip
    request["response"] = recaptchaResponse
    request["secret"] = secret

    val response = restTemplate.getForEntity(VERIFY_URL, Map::class.java, request)
    val body = response.body as Map<String, Any>
    val success = body["success"] as Boolean

    var errors: List<String> = ArrayList()

    if (!success) {
      errors = body["error-codes"] as List<String>
    }

    return RecaptchaDto(success, errors)
  }
}
```

- 예제에서는 상수로 secretKey를 관리하지만 실제 환경에서는 볼트나 다른 외부 비밀 저장 도구를 사용하는 것이 좋음.
- 그 후 회원가입 페이지에 reCAPTCHA 관련 로직을 추가하면, 리캡차를 간단하게 구현할 수 있다.
- 앞서 사용했던 로그인 횟수 실패 카운트 로직에 붙여서, 일정 횟수 이상 실패 시 리캡차 시도를 하게 할 수 있다. 이렇게하면 사용자 경험과 보안성이 균형을 이룰 수 있다.
  <br>
  <br>
  <br>
  <br>
  <br>

### 6.8 구글 오센티케이터 2단계 인증

- 다단계 인증(MFA)은 사용자가 여러 단계의 인증 과정을 거치도록 강제하는 인증 패턴이다.
- 2단계 인증은 다단계 인증의 한 방식으로 서로 다른 수준의 2단계 인증 과정으로 구분된다.
- 대부분의 웹 어플리케이션은 아이디/패스워드 기반 -> OTP(One Time Password)를 이용하여 2단계 인증을 구현
- 실습에서는 구글의 Authenticator 앱을 통하여 생성되는 OTP를 사용한다.
  <br>
  <br>
  <br>
  <br>
  <br>

### 6.9 OAuth2 인증

- 앞에서 다양한 로그인 방법을 살펴봤는데, 사용자 등록 없이도 사용자를 인증할 수 있는 방법이 있다.
- 구글, 페이스북, 깃허브 등의 여러 사이트에서는 인증에 OAuth라는 공개 표준을 사용하는데, 이를 이용하여 다른 사이트에서도 해당 사이트의 인증 정보를 사용할 수 있다.
- Spring Security 또한 OAuth 표준과 연동할 수 있는 독립적인 모듈을 제공한다.
- 스프링 부트는 OAuth2ClientAutoConfiguration 클래스를 통해서 OAuth에 대한 자동 구성을 지원한다.
- 구글 클라우드 플랫폼에서 OAuth 인증 정보를 발급 받은 뒤 Gradle에 아래와 같이 의존성을 추가한다.

```kotlin
    // 6장 스프링 시큐리티 응용
implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
```

- 그 후 application.yml에 아래와 같이 발급받은 secret과 clientId를 넣는다

```kotlin
  security:
oauth2:
client:
registration:
google:
client - id: <Your Client -Id >
  client - secret: <Your Secret >
scope: email, profile
```

- 마지막으로 Security에 다음과 같이 추가하면 OAuth 인증 등록이 완료된다.

```kotlin
            .oauth2Login {
  it.loginPage("/login")
    .successHandler(Oauth2AuthenticationSuccessHandler())
}
```

  <br>
  <br>
  <br>
  <br>
  <br>

### 6.10 액추에이터 엔드포인트 보안

- 4장에서 Spring boot의 액추에이터에 대해서 알아보았는데, 액추에이터는 어플리케이션의 민감한 정보를 노출하기 때문에 아무에게나 노출되어서는 안된다.
- 따라서 해당 엔드포인트는 특정 권한을 가진 사람만 접근 가능해야한다.
- 여기서는 /health 엔드포인트를 제외한 모든 액추에이터 엔드포인트를 특정 권한을 가진 사람만 접근할 수 있도록 한다.
- 먼저 SecurityConfiguration에 아래와 같이 엔드포인트 보안을 추가한다.

```kotlin
           .authorizeHttpRequests { authorize ->
  authorize
    .requestMatchers("/adduser", "/login", "/login-error", "/login-locked").permitAll()
    .requestMatchers("/webjars/**", "/css/**", "/h2-console/**", "/images/**").permitAll()
    .requestMatchers(EndpointRequest.to("health")).hasAnyRole("USER", "ADMIN")
    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
    .anyRequest().authenticated()
}
```

- 여기서 EndpointRequest는 actuator의 엔드포인트들에 대한 requestMatcher를 자동으로 생성해주는 클래스이다.
- 그 후 구현되어있는 CustomUserDetailsService에 role에 대한 정보를 추가한다.

```kotlin
@Service
class CustomUserDetailsService(
  private val userService: UserService,
  private val loginAttemptService: LoginAttemptService,
) : UserDetailsService {
  override fun loadUserByUsername(username: String): UserDetails {
    if (loginAttemptService.isBlocked(username)) {
      throw LockedException("User Account is Locked")
    }

    val user = userService.findByUsername(username)
      ?: throw UsernameNotFoundException("User with username $username not found")
    return User(
      user.username, user.password,
      user.authorities.map { SimpleGrantedAuthority(it) },
    )
  }
}
```

- 이렇게하면 user에 담긴 role이 spring security에 그대로 authorities로 담기게 되며, 엔드포인트에 접근할 때 사용자의 ROLE을 확인하여 특정 권한을 가진 사람만
  접근할 수 있게 할 수 있다.
