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

public class Login {

    private static String appUSERNAME = System.getenv("USERNAME_APP");
    private static String appPASSWORD = System.getenv("PASSWORD_APP");

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

        login(driverWait, driver);

    }

    public void login(WebDriverWait driverWait, WebDriver driver) throws Exception {
        WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        element.sendKeys(appUSERNAME);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        element.sendKeys(appPASSWORD);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("login")));
        element.click();

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Logout')]")));
    }


}
