# Tests Examples
Test examples with JDK-17 and JUnit-5

## Service Architecture

### POJO: [BankAccount](https://github.com/meirlustig1983/tests-examples/blob/main/src/main/java/com/ml/testsexamples/dao/BankAccount.java)
### Repository: [BankAccountRepository](https://github.com/meirlustig1983/tests-examples/blob/main/src/main/java/com/ml/testsexamples/repositories/BankAccountRepository.java)
### DataFacade: [DataFacade](https://github.com/meirlustig1983/tests-examples/blob/main/src/main/java/com/ml/testsexamples/facades/DataFacade.java)
### Service: [BankAccountService](https://github.com/meirlustig1983/tests-examples/blob/main/src/main/java/com/ml/testsexamples/services/BankAccountService.java)

## Tests Examples

### Examples of standard 'Unit Tests': [GreetingServiceTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/GreetingServiceTest.java), [BankAccountServiceTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceTest.java), [DataFacadeTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeTest.java)
### Examples of 'IT': [BankAccountServiceIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java), [DataFacadeIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeIT.java)
### Example of 'Suite' tests: [BankSuiteTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/suites/BankSuiteTest.java)
### Examples of 'Naming':
* Using @DisplayName: [BankAccountServiceIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java), [GreetingServiceTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/GreetingServiceTest.java), [BankAccountServiceTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceTest.java)
* Using @DisplayNameGeneration: [CustomDisplayNameGenerator](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/utils/CustomDisplayNameGenerator.java) using in [DataFacadeTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeTest.java) and [DataFacadeIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeIT.java)
### Examples of 'Assumptions: [#1](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L48), [#2](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L68)
### Example of 'Execution Ordered' using @Order: [BankAccountServiceExecutionOrderedIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceExecutionOrderedIT.java)
### Example of 'Dependency Injection' using [BankAccountParameterResolver](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/dao/BankAccountParameterResolver.java) : [DataFacadeDITest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeDITest.java)
### Example of 'Repeated' tests using @RepeatedTest: [BankAccountServiceRepeatedIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceRepeatedIT.java)
### Examples of 'Parameterized' tests using @ParameterizedTest:
* Using @ValueSource: [ValueSource](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeParameterizedIT.java#L33)
* Using @CsvSource: [CsvSource](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeParameterizedIT.java#L43)
* Using @CsvFileSource: [CsvFileSource](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeParameterizedIT.java#LL54C5-L54C19) and [tests-data.csv](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/resources/tests/tests-data.csv)
### Examples of 'Timeout' tests:
* Using @Timeout: [DataFacadeIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/facades/DataFacadeIT.java#L26), [BankAccountServiceIT](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L38)
* Using assertTimeout: [#1](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L183), [#2](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L213)
### Example of 'Parallel Execution' tests: [GreetingServiceParallelExecutionTest](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/GreetingServiceParallelExecutionTest.java) 
* Properties file: [junit-platform.properties](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/resources/junit-platform.properties)
### Examples of 'Conditional' tests:
* Using @EnabledOnOs: [#1](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L39), [#2](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L61) 
* Using @EnabledOnJre: [#1](https://github.com/meirlustig1983/tests-examples/blob/main/src/test/java/com/ml/testsexamples/services/BankAccountServiceIT.java#L40) 