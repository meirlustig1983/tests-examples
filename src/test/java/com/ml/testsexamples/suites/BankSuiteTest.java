package com.ml.testsexamples.suites;

import com.ml.testsexamples.controllers.BankAccountControllerIT;
import com.ml.testsexamples.services.BankAccountServiceIT;
import com.ml.testsexamples.facades.DataFacadeIT;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({DataFacadeIT.class, BankAccountServiceIT.class, BankAccountControllerIT.class})
public class BankSuiteTest {
    // intentionally empty
}