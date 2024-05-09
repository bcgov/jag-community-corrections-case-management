package ca.bc.gov.open.jag;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
	public static void main(String[] args) {
		Result result;

		Boolean genericResponse = true;

		// Process the command-line arguments
		for (String arg : args) {
			String[] pairs = arg.split(",");
			for (String pair : pairs) {
				String[] keyValue = pair.split("=");
				if (keyValue.length == 2) {
					String key = keyValue[0].trim();
					Boolean value = Boolean.valueOf(keyValue[1].trim());
					System.out.println("Key: " + key + ", Value: " + value);

					switch (key) {
						case "Login":
							if (value) {
								result = JUnitCore.runClasses(Login.class);
								printResults(result, "Login");
								if (!result.wasSuccessful()) {
									genericResponse = false;
								}
							} else {
								System.out.println("Login test skipped");
							}
							break;

						case "CreateNewCustodyCMRP":
							if (value) {
								result = JUnitCore.runClasses(CreateNewCustodyCMRP.class);
								printResults(result, "CreateNewCustodyCMRP");
								if (!result.wasSuccessful()) {
									genericResponse = false;
								}
							} else {
								System.out.println("CreateNewCustodyCMRP test skipped");
							}
							break;
						case "regression":
							if (value) {
								System.out.println("Started Regression Scope Testing.");
								result = JUnitCore.runClasses(CreateNewCustodyCMRP.class);
								printResults(result, "TestSuiteAllTests");
								if (!result.wasSuccessful()) {
									genericResponse = false;
								}
							} else {
								System.out.println("TestSuiteAllTests skipped");
							}
							break;
						// add other options

					}
				}
			}
		}

		//Set the exist code based on all tests.
		if (genericResponse)
			System.exit(0);  // good
		else
			System.exit(1);  // fail
	}

	private static void printResults(Result result, String testName) {
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("'" + testName + "' Test successful: " + result.wasSuccessful());
	}
}
