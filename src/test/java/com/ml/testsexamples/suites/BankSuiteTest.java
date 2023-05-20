package com.ml.testsexamples.suites;

import com.ml.testsexamples.services.BankAccountServiceIT;
import com.ml.testsexamples.services.BankAccountServiceTest;
import com.ml.testsexamples.facades.DataFacadeIT;
import com.ml.testsexamples.facades.DataFacadeTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({DataFacadeIT.class, DataFacadeTest.class,
        BankAccountServiceIT.class, BankAccountServiceTest.class})
public class BankSuiteTest {
    // intentionally empty
}