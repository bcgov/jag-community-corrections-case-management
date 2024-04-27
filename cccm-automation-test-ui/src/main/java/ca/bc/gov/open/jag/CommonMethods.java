package ca.bc.gov.open.jag;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class CommonMethods {

    private static String appUSERNAME = System.getenv("USERNAME_APP");
    private static String appPASSWORD = System.getenv("PASSWORD_APP");


    public static void login(WebDriver driver) throws Exception {
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();

        WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        element.sendKeys(appUSERNAME);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        element.sendKeys(appPASSWORD);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("login")));
        element.click();

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Logout')]")));
    }

    public static void clientSearchByCSNumber(WebDriver driver, String CSNumber) {
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();

        driver.get("https://dev.jag.gov.bc.ca/cccm/clientsearch");
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Client Search')]")));

        WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("data[idNumber]")));
        element.sendKeys(CSNumber);

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-value='Please select id type']")));
        element.click();

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'CS Number')]")));
        element.click();

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Search')]")));
        element.click();

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(), '" + CSNumber + "')]/..//a")));
        element.click();

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Client Record')]")));

    }

    public static void clickOnButton(String buttonName) {
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();

        WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), '" + buttonName + "')]")));
        element.click();
    }

    public static void clickOnTag(String tag, String containsText) {
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();

        WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + tag + "[contains(text(), '" + containsText + "')]")));
        element.click();
    }

    public static void clickOnTagByClass(String tag, String className) {
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();

        WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + tag + "[@class='" + className + "']")));

        element.click();
    }

    public static void clickOnTag(String tag) {
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();

        WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//" + tag)));
        element.click();
    }

    public static void textShouldExist(WebDriver driver, String text) {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), '" + text + "')]")));
    }
   public static void elementShouldExist(WebDriver driver, String tag , String text) {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//" + tag + "[contains(text(), '" + text + "')]")));
    }


    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }


}
