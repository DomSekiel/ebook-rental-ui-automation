package base;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

public class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected static final int SHORT_TIMEOUT =
            ConfigReader.getIntProperty("short.timeout.seconds");

    private final By loadingOverlay =
            By.cssSelector(".fog, .lds-ripple");

    public BasePage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        ConfigReader.getIntProperty("timeout.seconds")
                )
        );
    }

    protected void click(By locator) {

        waitForLoaderToDisappear();

        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element
        );

        waitForLoaderToDisappear();

        try {

            element.click();

        } catch (ElementClickInterceptedException e) {

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    element
            );
        }

        waitForLoaderToDisappear();
    }

    protected void type(By locator, String text) {

        waitForLoaderToDisappear();

        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );

        element.clear();
        element.sendKeys(text);
    }

    protected void waitForLoaderToDisappear() {

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(SHORT_TIMEOUT)
            ).until(webDriver -> {

                try {

                    return webDriver.findElements(loadingOverlay)
                            .stream()
                            .noneMatch(WebElement::isDisplayed);

                } catch (StaleElementReferenceException e) {

                    return false;
                }
            });

        } catch (TimeoutException ignored) {
        }
    }
}