package com.ml.testsexamples.suites;

import com.ml.testsexamples.managers.BankManagerIT;
import com.ml.testsexamples.managers.BankManagerTest;
import com.ml.testsexamples.services.BankAccountServiceIT;
import com.ml.testsexamples.services.BankAccountServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({BankAccountServiceIT.class, BankAccountServiceTest.class,
        BankManagerIT.class, BankManagerTest.class})
public class BankSuiteTest {
    // intentionally empty
}