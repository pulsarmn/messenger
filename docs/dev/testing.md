# 2026-07-07 | Testing methodology
- - -
Есть 2 методологии, которых можно придерживаться при написании тестов:
- **AAA(Arrange -> Act -> Assert)**
- **BDD(Given -> When -> Then)**

По факту это одно и то же, просто разные названия. Смысл в том, что сначала мы подготавливаем тестовые данные, затем производим действие(то бишь вызываем тестовый метод), а затем проверяем полученный результат.


# 2026-07-07 | Test methods naming
- - -
В современной разработке обычно используют 2 метода именования тестовых методов:

## Классический `метод_ПриКакихУсловиях_ЧтоОжидаем`

- `register_WhenUserAlreadyExists_ShouldThrowException()`
- `register_WhenPasswordsMismatch_ShouldThrowException()`
- `register_WhenDataIsValid_ShouldReturnTokenPair()`

## BDD стиль `should_ЧтоСделать_when_ПриКакихУсловиях`

- `should_ThrowException_when_UserAlreadyExists()`
- `should_ReturnTokens_when_RegistrationIsSuccessful()`
