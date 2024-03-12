# 2.5장 - Bean Validation

<hr/>

- 사용자가 입력한 데이터가 비즈니스 요구 사항에 적합한지 **검증**해야 하는 경우가 있습니다.
- Bean Validation은 자바 기반 프로그램에서 사용되는 사실상의 표준 검증입니다.

## 2.5.1 - Hibernate Validator

- Hibernate Validator는 비즈니스 엔티티 유효성 검증시 사용할 수 있는 프레임워크로,<br/>어노테이션(Annotation)을 엔티티 필드에 명시하여 해당 필드의 유효 조건을 부여할 수 있습니다.
- `spring-boot-starter-validation` 의존 관계를 추가하면 사용할 수 있습니다.

Hibernate Validator 어노테이션 예시:

|어노테이션|용도|
|---|---|
|@NotBlank|CharSequence 타입 필드에 사용되어 문자열이 null이 아니고, 앞뒤 공백 문자를 제거한 후 문자열길이가 0보다 크다는 것을 검사한다.<br/>ex) `""`나 `" "`는 부적합, `"sample string"`은 통과.|
|@NotEmpty|CharSequence, Collection, Map 타입과 배열에 사용되어 null이 아니고 비어있지 않음을 검사한다.<br/>ex) `""`는 부적합, `" "`나 `"sample string"`은 통과.|
|@NotNull|모든 타입에 사용할 수 있으며 null이 아님을 검사한다.|
|@Min(value =)|최솟값을 지정해서 이 값보다 크거나 같은지 검사한다.|
|@Max(value =)|최댓값을 지정해서 이 값보다 작거나 같은지 검사한다.|
|@Pattern(regex=, flags)|regex로 지정한 정규 표현식을 준수하는지 검사한다. 정규 표현식의 플래그도 사용할 수 있다.|
|@Size(min=, max=)|개수의 최솟값, 최댓값을 준수하는지 검사한다.|
|@Email|문자열이 유효한 이메일 주소를 나타내는지 검사한다.|

## 2.5.2 - 커스텀 Validator

- Hibernate Validator와 같이 범용적으로 사용되는 검증이 아닌, 특정 비즈니스에 맞는 검증을 직접 만들어야 할 경우가 있습니다.
- 이러한 경우에는 직접 Validator와 어노테이션을 구현하여 사용할 수 있습니다.

```kotlin
annotation class CustomAnnotation(
  val message: String = "해당 문자열은 hello로 시작해야 합니다.",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)

class CustomValidator : ConstraintValidator<CustomAnnotation, String> {
  override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
    return value.startsWith("hello")
  }
}
```

<hr/>

## 부록 - Kotlin에서의 annotation
책에서 나온 예시는 entity의 필드 위에 어노테이션을 명시하고 있습니다.

```java
public class Course {
  // 다른 필드는 생략
  
  @Min(value = 1, message = "A course should have a minimum of 1 rating")
  @Max(value = 5, message = "A course should have a mmaximum of 5 rating")
  private int rating;
}
```
그런데 Kotlin에서는 필드를 생성자에 바로 명시할 수 있는데(필드이자 생성자 파라미터), 이럴 경우 해당 어노테이션은 getter 메서드에 명시한 것으로 취급됩니다.

이렇게 생성된 클래스를 Validator를 통해 검증 시, 정상적으로 violation이 감지되지 않습니다.

이를 해결하기 위해서는 어노테이션 작성 시 다음과 같은 형태로 작성하여 어노테이션이 getter 함수가 아닌 필드에 적용되야 함을 명시해야 합니다.

```kotlin
class Course(
  // 다른 필드는 생략
  
  @field:Min(value = 1, message = "A course should have a minimum of 1 rating")
  @field:Max(value = 5, message = "A course should have a mmaximum of 5 rating")
  private val rating: Int,
) {
    
    // 생성자가 아닌 body에 정의한 경우 필드이기 때문에 `field:`를 명시하지 않아도 됨
    @Size(min = 1, max = 50)
    var name: String = "courseName"
}
```
