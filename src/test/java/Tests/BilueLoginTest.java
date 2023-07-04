package Tests;

import com.google.common.io.Files;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class BilueLoginTest {
    private AppiumDriver<MobileElement> driver;

    public void loginWith(String email, String password){
        MobileElement emailField = driver.findElement(MobileBy.id("username"));
        emailField.sendKeys(email);

        MobileElement passwordField = driver.findElement(MobileBy.id("password"));
        passwordField.sendKeys(password);

        MobileElement loginBtn = driver.findElement(MobileBy.id("login"));
        loginBtn.click();
    }

    public MobileElement waitForElementToBeVisible(By element){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        MobileElement elementWaited = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        return elementWaited;
    }

    @Parameters({"nameOfApp","platformName", "deviceName", "automationName", "appPath"})
    @BeforeTest
    public void setup(String nameOfApp, String platformName, String deviceName, String automationName, String appPath) throws IOException {

        try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/global.properties")) {
            Properties prop = new Properties();
            prop.load(fis);
            String appName = nameOfApp;
            File fileFolderPath = new File(appPath);
            File filepath = new File(fileFolderPath, appName);

            DesiredCapabilities dc = new DesiredCapabilities();
            dc.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);
            dc.setCapability(MobileCapabilityType.APP, filepath.getAbsolutePath());
            dc.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);

            String serverUrl = (String) prop.get("APPIUM_SERVER_URL");
            URL appiumServerURL = new URL(serverUrl);
            driver = new AppiumDriver<>(appiumServerURL, dc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            TakesScreenshot camera = (TakesScreenshot) driver;
            File screenshot = camera.getScreenshotAs(OutputType.FILE);

            try {
                File directory = new File("src/ScreenShots");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                Files.move(screenshot, new File("src/ScreenShots/"  + result.getName() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }

    @Test
    public void successfulLoginWithCorrectEmailAndPassword() {
        this.loginWith("test@gmail.com", "12345678");
        MobileElement loggedIn = this.waitForElementToBeVisible(By.id("error"));
        Assert.assertTrue(loggedIn.getText().contains("Logged In"), "Logged In success message does not displays");
    }

    @Test
    public void failedLoginWithIncorrectEmailOrPassword() {
        this.loginWith("invalidEmail", "12345");
        MobileElement errorMsg = this.waitForElementToBeVisible(By.id("error"));
        Assert.assertTrue(errorMsg.getText().contains("Invalid Email or Password"), "error message does not displays with incorrect email or password");
    }
}
