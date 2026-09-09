package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ItemsPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsPage.class);

    private final By addButton = By.id("add-item-button");
    private final By purchaseDate = By.name("purchase-date");
    private final By submitButton = By.cssSelector("button[name='submit-button']");
    private final By items = By.cssSelector("li.items-list__item");
    private final By availableStatus = By.cssSelector(".items-list__item__status--available");
    private final By showHistoryButton = By.cssSelector(".show-rents-btn");
    private final By editButton = By.cssSelector(".edit-btn");
    private final By removeButton = By.cssSelector(".remove-btn");
    private final By rentsHeader = By.xpath("//h2[contains(text(),'Rents')]");
    private final By firstItemPurchaseDate = By.cssSelector(".items-list__item:first-child .items-list__item__purchase-date");

    public ItemsPage(WebDriver driver) {

        super(driver);
    }

    public void addItem() {

        waitForLoaderToDisappear();

        click(addButton);

        click(purchaseDate);

        List<WebElement> days =
                driver.findElements(
                        By.cssSelector(".cell.day:not(.blank)")
                );

        if (!days.isEmpty()) {
            days.get(0).click();
        }

        new Actions(driver)
                .moveByOffset(10, 10)
                .click()
                .perform();

        click(submitButton);

        waitForLoaderToDisappear();
    }

    public void editFirstItem() {

        waitForLoaderToDisappear();

        click(editButton);

        WebElement dateInput =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                purchaseDate
                        )
                );

        dateInput.click();

        String currentDate =
                dateInput.getAttribute("value");

        String currentDay =
                currentDate.substring(
                        currentDate.length() - 2
                );

        List<WebElement> days =
                driver.findElements(
                        By.cssSelector(".cell.day:not(.blank)")
                );

        for (WebElement day : days) {

            if (!day.getText().equals(
                    String.valueOf(
                            Integer.parseInt(currentDay)
                    )
            )) {

                day.click();
                break;
            }
        }

        new Actions(driver)
                .moveByOffset(10, 10)
                .click()
                .perform();

        click(submitButton);

        waitForLoaderToDisappear();
    }

    public void clickShowHistoryForFirstItem() {

        waitForLoaderToDisappear();

        click(showHistoryButton);

        wait.until(
                ExpectedConditions.urlContains("/rents/")
        );
    }

    public int getItemsCount() {

        waitForLoaderToDisappear();

        return driver.findElements(items).size();
    }

    public String getFirstItemPurchaseDate() {

        waitForLoaderToDisappear();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        firstItemPurchaseDate
                )
        ).getText();
    }

    public boolean isAvailableStatusDisplayed() {

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(SHORT_TIMEOUT)
            ).until(
                    ExpectedConditions.visibilityOfElementLocated(
                            availableStatus
                    )
            );

            return true;

        } catch (TimeoutException e) {

            return false;
        }
    }

    public boolean isRentsPageDisplayed() {

        try {

            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(SHORT_TIMEOUT)
            ).until(
                    ExpectedConditions.visibilityOfElementLocated(
                            rentsHeader
                    )
            );

            return true;

        } catch (TimeoutException e) {

            return false;
        }
    }

    public String getLastItemId() {

        waitForLoaderToDisappear();

        List<WebElement> allItems = driver.findElements(items);

        WebElement lastItem =
                allItems.get(allItems.size() - 1);

        return lastItem.getAttribute("id");
    }

    public void removeItemById(String itemId) {

        waitForLoaderToDisappear();

        WebElement item =
                driver.findElement(By.id(itemId));

        WebElement button =
                item.findElement(removeButton);

        LOGGER.info("Removing item id: {}", itemId);

        wait.until(
                ExpectedConditions.elementToBeClickable(button)
        ).click();

        wait.until(
                ExpectedConditions.stalenessOf(item)
        );
    }

    public boolean isItemVisibleById(String itemId) {

        waitForLoaderToDisappear();

        return driver.findElements(By.id(itemId))
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    public void clickShowHistoryById (String itemId) {

        waitForLoaderToDisappear ();

        WebElement item =
                wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id(itemId)
                    )
                );

        WebElement button =
                item.findElement(showHistoryButton);

        wait.until(
                ExpectedConditions.elementToBeClickable(button)
        ).click();

        wait.until(
                ExpectedConditions.urlContains("/rents/")
        );
    }

    public String getItemStatusById (String itemId) {

        waitForLoaderToDisappear();

        WebElement item =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.id(itemId)
                        )
                );

        return item.findElement(
                By.cssSelector(".items-list__item__status")
        ).getText();
    }

    public boolean isItemsPageDisplayed() {

        try {
            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(SHORT_TIMEOUT)
            ).until(
                    ExpectedConditions.visibilityOfElementLocated(addButton
                    )
            );

            return true;

        }   catch (TimeoutException e) {

            return false;
        }
    }
}