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

}
