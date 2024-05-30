package ca.bc.gov.open.jag;

import ca.bc.gov.open.jag.properties.TestProperties;
import org.openqa.selenium.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@EnableConfigurationProperties
public class CommonUtils {

	private static TestProperties testProperties;

	public CommonUtils(TestProperties testProperties) {
		this.testProperties = testProperties;
	}

	private static Logger log = Logger.getLogger("CommonUtils.class");

	public static void openLogin() throws Exception {

		WebDriver driver = CustomWebDriverManager.getDriver();
		WebElement element = CustomWebDriverManager.getElement();

		if (Config.ENVIROMENT.equals(Constants.DEV)) {


			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
			driver.get(testProperties.getBaseUrl());

		} else if (Config.ENVIROMENT.equals(Constants.TST)) {
			driver.get(testProperties.getBaseUrl());
			driver.navigate().to(testProperties.getBaseUrl());
			driver.navigate().refresh();

		}
	}

	// Construct jdbcUrl for Environment
	public static String getJdbcUrl() {
		String jdbcUrl = "";
		if (Config.ENVIROMENT.equals(Constants.DEV)) {
			jdbcUrl = testProperties.getDbConnection();
		} else if (Config.ENVIROMENT.equals(Constants.TST)) {
			jdbcUrl = testProperties.getDbConnection();
		}

		String dbName = "";

		if (Config.ENVIROMENT.equals(Constants.DEV)) {
			dbName = "DatabaseName=";
		} else if (Config.ENVIROMENT.equals(Constants.TST)) {
			dbName = "DatabaseName=";
		}

		return jdbcUrl;
	}

	// Construct userName for Environment
	public static String getJdbcUserName() {
		String userName = "";
		if (Config.ENVIROMENT.equals(Constants.DEV)) {
			userName = "jdbcUserName.DEV";
		} else if (Config.ENVIROMENT.equals(Constants.TST)) {
			userName = "jdbcUserName.TST";
		}
		return userName;
	}

	// Construct password for Environment
	public static String getJdbcPassword() {
		String password = "";
		if (Config.ENVIROMENT.equals(Constants.DEV)) {
			password = "jdbcPassword.DEV";
		} else if (Config.ENVIROMENT.equals(Constants.TST)) {
			password = "jdbcPassword.TST";
		}
		return password;
	}

}
