package ca.bc.gov.open.jag;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonMethods {

    public static String user;
    public static void method1(WebDriver driver, WebDriverWait driverWait) throws InterruptedException {
            // Click Next
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("primaryButton")));
            js.executeScript("arguments[0].click();", element);
            System.out.println("Click Next");
            Thread.sleep(1000);
    }
}
