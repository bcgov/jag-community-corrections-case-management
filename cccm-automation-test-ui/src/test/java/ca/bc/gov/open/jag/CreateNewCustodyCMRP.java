package ca.bc.gov.open.jag;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ca.bc.gov.open.jag.CommonMethods.*;
import static ca.bc.gov.open.jag.Constants.MAXIMIZE_XPATH;
import static ca.bc.gov.open.jag.Constants.MINIMIZE_XPATH;

public class CreateNewCustodyCMRP {

    //    private static final String CSNumber = "06039788"; //Community Client
    private static final String CSNumber = "05873237"; //Custody Client
    private static final String clientName = "CHER, CHER";
    private static final String createdByName = "null, null";
    private static final String clientGender = "F";
    private static final String clientDateOfBirth = "1965-01-01";
    private static final String clientCurrentActiveLocation = "0451 - Abbotsford Probation";
    private static final String clientCustodyStatus = "Inactive";

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
        System.out.println("Started 'Create New Custody CMRP' Testing.");

        driver = CustomWebDriverManager.getDriver();

        CommonUtils.openLogin();

        login();

        clientSearchByCSNumber(CSNumber);

        clickOnTagByText("a", "RNA List");
        elementShouldExist("button", "Create New Custody-CMRP");
        clickOnButton("Create New Custody-CMRP");
        clickOnTagByText("span", "Yes, Continue");
        textShouldExist("Rating");

        validateClientDetailsOnCustodyCMRP("Name", clientName);
        validateClientDetailsOnCustodyCMRP("CS", CSNumber);
        validateClientDetailsOnCustodyCMRP("Gender", clientGender);
        validateClientDetailsOnCustodyCMRP("Date of Birth", clientDateOfBirth);
        validateClientDetailsOnCustodyCMRP("Current Active Location", clientCurrentActiveLocation);
        validateClientDetailsOnCustodyCMRP("Custody Status", clientCustodyStatus);


        validatCustodyCMPRDetailsOnCustodyCMRP("Created Date", getTodaysDate());
        validatCustodyCMPRDetailsOnCustodyCMRP("Updated Date", getTodaysDate());
        validatCustodyCMPRDetailsOnCustodyCMRP("Created By", createdByName);

        textShouldExist("Custody-CMRP");
        clickOnElementByXpath(MAXIMIZE_XPATH); //maximize screen
        Assert.assertFalse("Client details not hidden, maximize button didn't work" , getElementByTagAndText("span", "Client Details").isDisplayed());

        inputTextByTagAttributeContainsValue("textarea", "id", "COMMENT", "Test Comment");
        scrollToBottom();
        clickOnButton("Save and Continue");

        inputTextByTagAttributeContainsValue("textarea", "id", "S02Q01", "Test Family Relationships");

        clickOnElementByXpath("//input[@name='data[S02Q01_intervention_checkbox]']");
        textShouldExist("Select Intervention Type");

        clickOnElementByXpathJS("//select[contains(@id, 'S02Q01_intervention_type')]");
        clickOnElementByXpath("//div[@data-value='LWV']");
        inputTextByTextAreaIdContainsValue("S02Q01_intervention_desc", "Test Select Intervention Type Living Without Violence");
        inputTextByTextAreaIdContainsValue("S02Q02", "Test Select Intervention Type Living Without Violence");
        inputTextByTextAreaIdContainsValue("S02Q03", "Test Companions / Significant Others");
        inputTextByTextAreaIdContainsValue("S02Q04", "Test Academic / Vocational Skills");
        inputTextByTextAreaIdContainsValue("S02Q05", "Test Employment Pattern");
        inputTextByTextAreaIdContainsValue("S02Q06", "Test Financial Management");
        inputTextByTextAreaIdContainsValue("S02Q07", "Test Behavioural / Emotional Stability");
        inputTextByTextAreaIdContainsValue("S02Q08", "Test Substance Misuse");
        inputTextByTextAreaIdContainsValue("S02Q09", "Test Attitude");

        scrollToBottom();
        clickOnButton("Save and Continue");

        textShouldExist("Custody Transition and Release Plan");

        inputTextByTextAreaIdContainsValue("S03Q01", "Test Proposed Residence");
        clickOnElementByXpath("//input[@value='TERR']"); // Hub Location Terrace

        selectCheckboxByInputNameAttributeContainsValue("data[S03Q05]", "Yes"); // Transportation
        inputTextByTagAttributeContainsValue("textarea", "id", "S03Q05_COMMENT", "Test Transportation Comment");

        selectCheckboxByInputNameAttributeContainsValue("data[S03Q06]", "No"); // Finances N
        inputTextByTagAttributeContainsValue("textarea", "id", "S03Q06_COMMENT", "Test Finances Comment");

        selectCheckboxByInputNameAttributeContainsValue("data[S03Q07]", "Yes"); // Health Care Services Y
        inputTextByTextAreaIdContainsValue("S03Q07_COMMENT", "Test Health Care Services Comment");
        selectCheckboxByInputNameAttributeContainsValue("data[S03Q08]", "Yes");  // Other Community Appointments Y
        inputTextByTextAreaIdContainsValue("S03Q08_COMMENT", "Test Other Community Appointments Comment");
        selectCheckboxByInputNameAttributeContainsValue("data[S03Q09]", "Yes");  // Post-Release Appointments Y
        inputTextByTextAreaIdContainsValue("S03Q09_COMMENT", "Test Post-Release Appointments Comment");

        scrollToBottom();
        clickOnButton("Save and Continue");

        inputTextByTextAreaIdContainsValue("S04Q01", "Test Provide summary / information for Community Probation Officer");

        scrollToBottom();
        clickOnButton("Save and Continue");

        clickOnElementByName("data[responsivity][PRR2]"); // Previous responses to supervision and motivation to engage in interventions
        inputTextByTextAreaIdContainsValue("PRR2_desc", "Test Previous responses to supervision and motivation to engage in interventions");
        inputTextByTextAreaIdContainsValue("COMMENT_TXT", "Test Assessment Comments");

        scrollToBottom();
        clickOnButton("Save and Continue");

        textShouldExist("Summary");
        clickOnElementByXpath(MINIMIZE_XPATH); //minimize screen
        Assert.assertTrue("Client details are hidden, minimize button didn't work" , getElementByTagAndText("span", "Client Details").isDisplayed());

        clickOnButton("Add Support");
        inputTextByTextAreaIdContainsValue("input_key_sourceContacted", "Test Community Supports field");
        clickOnElementByName("data[add_source]");

        scrollToBottom();
        textShouldExist("Submit Form");
        clickOnButton("Submit Form");

        textShouldNotExist("CMRP form validation failed");

        textShouldExist("Client Record");
        textShouldExist("RNA List");
    }

    public static void validateClientDetailsOnCustodyCMRP(String fieldName, String expectedValue) {
        WebDriver driver = CustomWebDriverManager.getDriver();
        Boolean result = true;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[contains(text(), '" + expectedValue + "')]//strong[contains(text(),'" + fieldName + "')]")));
        } catch (Exception e) {
            result = false;
        }
        Assert.assertTrue("Client details are not correct. " + fieldName + " expected to be " + expectedValue, result);
    }

    public static void validatCustodyCMPRDetailsOnCustodyCMRP(String fieldName, String expectedValue) {
        WebDriver driver = CustomWebDriverManager.getDriver();
        Boolean result = true;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(), '" + expectedValue + "')]//strong[contains(text(),'" + fieldName + "')]")));
        } catch (Exception e) {
            result = false;
        }
        Assert.assertTrue("Custody CMRP details are not correct. " + fieldName + " expected to be " + expectedValue, result);
    }

    public static String getTodaysDate() {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the date using the formatter
        String formattedDate = today.format(formatter);

        return formattedDate;
    }
}
