## 6. 스프링 시큐리티 응용
스프링 부트 시큐리티를 사용하여 로그인 인증 과정은 다음과 같이 일반적으로 진행됩니다.
- vault, mail 서비스 생략함

### 스프링 시큐리티 인증 과정
![img.png](img.png)
스프링 시큐리티의 가장 큰 특징 중 하나는 스프링 시큐리티 그 자체가 애플리케이션의 필터 단에서 동작한다는 것입니다. 스프링 시큐리티는 다양한 보안 관련 요구사항들을 각각의 필터 형태로 애플리케이션에 삽입하고, 사용자의 요청을 가로채(intercept) 필요한 보안적 역할을 수행하는 형식으로 동작합니다. 이러한 방식은 인증에 관한 처리에서 또한 마찬가지로, 스프링 시큐리티는 AuthenticationFilter라는 인증 필터를 이용해 사용자의 요청을 검증하고 현재 요청의 사용자가 시스템상에 접근 허가된 회원인지를 판별합니다. 그림에서 보이는 1번이 바로 이러한 과정을 나타냅니다.



1번 : 사용자의 요청이 스프링 시큐리티의 인증필터인 AuthenticationFilter에 의해 가로채(intercepted)집니다.


AuthenticationFilter에 의해 가로채진 사용자의 요청은 AuthenticationFilter가 내부적으로 호출하는 AuthenticationManager에 의해 검증됩니다. AuthenticationManager는 AuthenticationFilter에 의해 사용자의 요청을 인증해야 하는 업무를 위임받습니다. 이러한 방식을 사용하는 이유는 객체 지향 설계 원칙 중 하나인 '단일 책임 원칙'과 관련이 깊은데, AuthenticationFilter는 이미 사용자의 요청을 필터 단에서 가로채는 역할을 수행하기 때문에 실제 회원에 대한 인증을 처리하는 별도의 장치를 마련한 것입니다. 이렇게 함으로써 AuthenticationFilter는 필터 상에서 회원의 요청을 가로채는 역할과 책임만을, AuthenticationManager는 AuthenticationFilter에 의해 획득된 회원의 요청을 인증하는 역할과 책임만을 부여받게 되는 것입니다.



3번 : 인증에 대한 책임이 AuthenticationManager에게 위임됩니다.

AuthenticationFilter에 의해 인증에 대한 책임을 위임받은 AuthenticationManager는 다수의 AuthenticationProvider를 주입 받아 인증에 대한 작업을 실행합니다. AuthenticationManager는 하나 이상의 AuthenticationProvider 목록을 순회하며 검증에 필요한 특정 유형의 인증을 수행합니다.



예를 들어 사용자명 / 비밀번호 기반의 인증 형태를 가진 애플리케이션의 경우 AuthenticationManager는 AuthenticationProvider 중 사용자명 / 비밀번호 형태의 인증을 수행하는 DaoAuthenticationProvider를, JWT 토큰 기반의 인증 형태를 가진 애플리케이션의 경우는 JWT 형태의 인증을 수행하는 JwtAuthenticationProvider를 사용할 수 있습니다.


4번 : 실질적이고 구체적인 형태의 특정 인증을 수행하기 위해 AuthenticationManager는 다양한 종류의 AuthenticationProvider 중 특정 AuthenticationProvider를 선택해 사용합니다.


특정 형태의 인증을 수행해야 하는 AuthenticationProvider가 가장 먼저 해야 하는 작업이 무엇일까요? 바로 인증하고자 하는 회원을 회원이 저장된 위치로부터 불러와 인증을 시작하는 것입니다. 인증을 할 회원을 가져오지 못한다면 인증을 하는 것은 아무런 의미가 없습니다. 회원을 인증해야 하는데, 기존 회원의 정보를 열람할 수 없으면 인증이 불가능 할겁니다.

따라서 시스템은 이러한 역할, 즉 시스템 상 혹은 시스템 외부에 저장된 회원을 AuthenticationProvider가 사용할 수 있게 불러오는 역할을 위해 UserDetailsService를 사용하는데요, 조금 후에 살펴볼 예정이지만 UserDetailsService에는 loadUserByUsername 라는 이름의 메소드가 있어 회원을 스프링 시큐리티가 사용할 수 있는 형태로 가져올 수 있습니다.


5번 : AuthenticationProvider는 인증을 하기 위한 회원 정보를 호출하기 위해 UserDetailsService를 사용합니다.


마지막으로 살펴볼 UserDetails는 일종의 데이터 저장 형태로, UserDetailsService에 의해 조회된 회원 정보를 스프링 시큐리티가 사용할 수 있는 형태로 변환한 형태를 의미합니다. UserDetails는 스프링 시큐리티가 사용하는 회원 객체(사실 UserDetails는 인터페이스이기 때문에 객체라는 표현은 무리가 있습니다. 편의상 표현하였습니다.)로 getUsername(), getPassword(), isEnabled()와 같은 회원의 인증에 도움이 되는 각종 메소드를 가지고 있습니다. UserServices를 구현한 대표적인 객체가 스프링 시큐리티의 User 객체입니다. UserDetails와 UserDetailsService는 아래의 장에서 조금 더 자세히 알아보도록 하겠습니다.


6번 : UserDetailsService는 불러온 회원 정보를 스프링 시큐리티가 사용할 수 있는 형태인 UserDetails에 저장합니다.


7번 : 저장된 회원정보를 담은 UserDetails를 AuthenticationProvider에 반환합니다.
8번, 9번, 10번 : 인증에 성공한다면 해당 정보를 SecurityContextHolder 내부에 저장합니다. 인증에 실패할 경우 Exception이 발생합니다.

### 스프링 시큐리티 로그인 로직 구현
1. **사용자가 로그인 페이지에 접근**: 사용자가 웹 애플리케이션의 로그인 페이지에 접근합니다. 이 페이지는 시큐리티 구성에 따라 설정됩니다.
- LoginController
```kotlin
package com.springbootstudy2024.springbootstudy2024.chapter6.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping("/index")
    fun index(): String? {
        return "index"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/login-error")
    fun loginError(
        model: Model
    ): String {
        model.addAttribute("loginError", true)

        return "login"
    }

    @GetMapping("/login-locked")
    fun loginLocked(
        model: Model
    ): String {
        model.addAttribute("loginLocked", true)
        return "login"
    }
}
```
- RegistrationController
```kotlin
package com.springbootstudy2024.springbootstudy2024.chapter6.controller

import com.springbootstudy2024.springbootstudy2024.chapter6.dto.UserDto
import com.springbootstudy2024.springbootstudy2024.chapter6.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class RegistrationController(
    private val userService: UserService
) {
    @GetMapping("/adduser")
    fun register(
        @ModelAttribute("user")
        user: UserDto = UserDto()
    ): String {
        return "add-user"
    }

    @PostMapping("/adduser")
    fun loginError(
        @ModelAttribute("user")
        userDto: UserDto,
        result: BindingResult
    ): String {
        if (result.hasErrors())
            return "add-user"

        userService.createUser(userDto)
        return "redirect: adduser ? success"
    }
}
```

- SecurityConfiguration
```kotlin
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val customAuthenticationFailureHandler: CustomAuthenticationFailureHandler,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors {}
            .csrf {
                it.disable()
            }
            .headers {
                it.frameOptions {
                    it.disable()
                }
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/adduser", "/login", "/login-error", "/login-locked").permitAll()
                    .requestMatchers("/webjars/**", "/css/**", "/h2-console/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin {
                it.loginPage("/login")
                    .defaultSuccessUrl("/index", true).permitAll()
                    .failureHandler(customAuthenticationFailureHandler)
            }
            .build()
    }
}
```

2. **사용자가 자격 증명을 제출**: 사용자가 아이디와 비밀번호를 입력하고 로그인 버튼을 클릭하여 자격 증명을 제출합니다.

3. **로그인 요청 처리**: 스프링 시큐리티는 로그인 요청을 인터셉트하고, 인증 프로세스를 시작합니다.

4. **CustomUserDetailsService 호출**: `CustomUserDetailsService`에서는 제출된 사용자 아이디를 기반으로 사용자 정보를 데이터베이스나 다른 소스에서 조회합니다. 조회된 사용자 정보는 `UserDetails` 객체로 변환되어 반환됩니다.
- CustomUserDetailsService
```kotlin
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userService: UserService,
    private val loginAttemptService: LoginAttemptService,
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        if(loginAttemptService.isBlocked(username)) {
            throw LockedException("User Account is Locked")
        }

        val user = userService.findByUsername(username)
            ?: throw UsernameNotFoundException("User with username $username not found")

        return User(user.username, user.password, emptyList())
    }
}
```
- UserService
```kotlin
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun createUser(userDto: UserDto) {
        val user = ApplicationUser(
            firstName = userDto.firstName,
            lastName = userDto.lastName,
            username = userDto.username,
            email = userDto.email,
            password = passwordEncoder.encode(userDto.password),
        )

        userRepository.save(user)
    }

    fun save(user: ApplicationUser) {
        userRepository.save(user)
    }

    fun findByUsername(username: String): ApplicationUser? {
        return userRepository.findByUsername(username)
    }
}
```

5. **사용자 인증**: 반환된 `UserDetails` 객체를 기반으로 스프링 시큐리티는 사용자를 인증합니다. 이 과정에서는 입력된 비밀번호와 데이터베이스에서 가져온 비밀번호를 비교하여 일치 여부를 확인합니다. 만약 인증에 실패하면 `BadCredentialsException` 등의 예외가 발생할 수 있습니다.

6. **인증 결과 처리**: 인증이 성공하면, 사용자 정보와 권한 정보를 포함한 `Authentication` 객체가 생성됩니다. 이 객체는 보안 컨텍스트에 저장되어 현재 사용자를 나타냅니다.

7. **AuthenticationSuccessHandler 호출**: 로그인 성공 시, 설정된 `AuthenticationSuccessHandler`가 호출됩니다. 이 핸들러를 통해 로그인 성공 후의 동작을 정의할 수 있습니다. 예를 들어, 인증된 사용자를 특정 페이지로 리다이렉트하거나 추가적인 작업을 수행할 수 있습니다.

8. **사용자 리다이렉션 또는 홈페이지로 이동**: `AuthenticationSuccessHandler`가 처리한 후, 사용자는 성공적으로 로그인되었음을 알리는 페이지로 리다이렉션되거나, 설정된 디폴트 페이지로 이동합니다.
- AuthenticationSuccessEventListener
```kotlin
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Service

@Service
class AuthenticationSuccessEventListener(
    private val loginAttemptService: LoginAttemptService,
): ApplicationListener<AuthenticationSuccessEvent> {
    override fun onApplicationEvent(authenticationSuccessEvent: AuthenticationSuccessEvent) {
        val user = authenticationSuccessEvent.authentication as User
        loginAttemptService.loginSuccess(user.username)
    }
}
```

9. **인증 실패 처리**: 만약 인증에 실패하면, 설정된 `AuthenticationFailureHandler`가 호출됩니다. 이 핸들러를 통해 로그인 실패 시의 동작을 정의할 수 있습니다. 일반적으로는 실패 메시지를 표시하거나, 사용자를 로그인 페이지로 리다이렉션합니다.
- AuthenticationFailureEventListener
```kotlin
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.stereotype.Service

@Service
class AuthenticationFailureEventListener(
    private val loginAttemptService: LoginAttemptService,
): ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    override fun onApplicationEvent(authenticationFailureBadCredentialsEvent: AuthenticationFailureBadCredentialsEvent) {
        val username = authenticationFailureBadCredentialsEvent.authentication.principal as String
        loginAttemptService.loginFailed(username)
    }
}
```

- CustomAuthenticationFailureHandler
```kotlin
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import javax.servlet.ServletException

@Service
class CustomAuthenticationFailureHandler: AuthenticationFailureHandler {
  private val defaultRedirectStrategy = DefaultRedirectStrategy()

  @Throws(IOException::class, ServletException::class)
  override fun onAuthenticationFailure(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    exception: AuthenticationException
  ) {
    if (exception.cause is LockedException) {
      defaultRedirectStrategy.sendRedirect(request, response, "/login-locked")
      return
    }

    defaultRedirectStrategy.sendRedirect(request, response, "/login-error")
  }
}
```

10. **사용자 로그인 시도 기록**: 로그인 시도가 실패한 경우, 이를 기록하여 보안 상태를 관리할 수 있습니다. 일정 횟수 이상의 실패 시도가 감지되면 보안 조치를 취할 수 있습니다.
- 구아바 캐시를 사용해 로그인 시도 횟수 제한하기
- LoginAttemptService
```kotlin
package com.springbootstudy2024.springbootstudy2024.chapter6.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@Service
class LoginAttemptService {
    private val loginAttemptCache = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.DAYS)
        .build(
            object: CacheLoader<String, Int>() {
                override fun load(key: String): Int {
                    return 0
                }
            },
        )

    fun loginSuccess(username: String) {
        loginAttemptCache.invalidate(username)
    }

    fun loginFailed(username: String) {
        var failedAttemptCounter = 0

        failedAttemptCounter = try {
            loginAttemptCache.get(username)
        } catch (e: ExecutionException) {
            0;
        }

        failedAttemptCounter++
        loginAttemptCache.put(username, failedAttemptCounter + 1)
    }

    fun isBlocked(username: String): Boolean {
        return try {
            loginAttemptCache[username] >= MAX_ATTEMPTS_COUNT
        } catch (e: ExecutionException) {
            false
        }
    }

    companion object {
        private const val MAX_ATTEMPTS_COUNT = 3
    }
}
```
