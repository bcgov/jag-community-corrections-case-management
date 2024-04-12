package ca.bc.gov.open.jag;

import org.openqa.selenium.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CommonUtils {

	private static Logger log = Logger.getLogger("CommonUtils.class");

	public static void openLogin() throws Exception {

		WebDriver driver = CustomWebDriverManager.getDriver();
		WebElement element = CustomWebDriverManager.getElement();

		if (Config.ENVIROMENT.equals(Constants.DEV)) {


			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
			driver.get("https://dev.jag.gov.bc.ca/cccm");

		} else if (Config.ENVIROMENT.equals(Constants.TST)) {
			driver.get("https://test.jag.gov.bc.ca/cccm");
			driver.navigate().to("https://test.jag.gov.bc.ca/cccm");
			driver.navigate().refresh();

		}
	}

	// Construct jdbcUrl for Environment
	public static String getJdbcUrl() {
		String jdbcUrl = "";
		if (Config.ENVIROMENT.equals(Constants.DEV)) {
			jdbcUrl = "jdbc:oracle:thin:@devdb.bcgov:1521:devj";
		} else if (Config.ENVIROMENT.equals(Constants.TST)) {
			jdbcUrl = "jdbc:oracle:thin:@testdb.bcgov:1521:tstj";
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
