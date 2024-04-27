package ca.bc.gov.open.jag;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static ca.bc.gov.open.jag.CommonMethods.*;

public class CreateNewCustodyCMRP {

    private static String appUSERNAME = System.getenv("USERNAME_APP");
    private static String appPASSWORD = System.getenv("PASSWORD_APP");
    private static final String CSNumber = "06039788"; //Community Client

    private WebDriver driver;

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    @AfterClass
    public static void afterClass() {
        CustomWebDriverManager.instance = null;
    }

    @Test
    public void test() throws Exception {
        driver = CustomWebDriverManager.getDriver();
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();
        WebElement element = CustomWebDriverManager.getElement();
        CustomWebDriverManager.getElements();

        CommonUtils.openLogin();

        login(driver);

        clientSearchByCSNumber(driver, CSNumber);

        clickOnTag("a", "RNA List");
        elementShouldExist(driver, "button", "Create New Custody-CMRP");
        sleep(2);
        clickOnButton("Create New Custody-CMRP");
//        clickOnTag("span", "Yes, Continue");




    }




}
