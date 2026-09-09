package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ItemsPage;
import pages.LoginPage;
import pages.TitlesPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemsTests extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsTests.class);

    @BeforeEach
    void loginAndOpenItemsPage() {

        LoginPage loginPage =
                new LoginPage(driver);

        loginPage.login(
                ConfigReader.getProperty("valid.login"),
                ConfigReader.getProperty("valid.password")
        );

        TitlesPage titlesPage =
                new TitlesPage(driver);

        String title = TestDataGenerator.generateTitle();

        titlesPage.addTitle(title, TestDataGenerator.generateAuthor(), TestDataGenerator.getDefaultYear());

        titlesPage.openItemsForTitle(title);

        ItemsPage itemsPage = new ItemsPage(driver);

        assertThat(
                itemsPage.isItemsPageDisplayed()
        ).isTrue();
    }

    @Test
    @DisplayName("Should add item")
    void shouldAddItem() {      // TC #15 Dodanie egzemplarza

        ItemsPage itemsPage =
                new ItemsPage(driver);

        itemsPage.addItem();

        assertThat(
                itemsPage.isAvailableStatusDisplayed()
        ).isTrue();
    }

    @Test
    @DisplayName("Should edit item")
    void shouldEditItem() {     // TC #16 Edycja egzemplarza

        ItemsPage itemsPage =
                new ItemsPage(driver);

        itemsPage.addItem();

        String purchaseDateBefore =
                itemsPage.getFirstItemPurchaseDate();

        itemsPage.editFirstItem();

        String purchaseDateAfter =
                itemsPage.getFirstItemPurchaseDate();

        assertThat(purchaseDateAfter)
                .isNotEqualTo(purchaseDateBefore);
    }

    @Test
    @DisplayName("should remove selected item")
    void shouldRemoveSelectedItem() {       // TC #17 Usunięcie egzemplarza

        ItemsPage itemsPage = new ItemsPage(driver);

        itemsPage.addItem();

        int itemsCountBefore = itemsPage.getItemsCount();

        String itemIdToRemove = itemsPage.getLastItemId();

        LOGGER.info("Item selected to remove: {}", itemIdToRemove);

        itemsPage.removeItemById(itemIdToRemove);

        int itemsCountAfter = itemsPage.getItemsCount();

        assertThat(itemsPage.isItemVisibleById(itemIdToRemove))
                .isFalse();

        assertThat(itemsCountAfter)
                .isLessThan(itemsCountBefore);
    }

    @Test
    @DisplayName("should navigate to rents page")
    void shouldNavigateToRentsPage() {      // TC #18 Przejście do listy wypożyczeń

        ItemsPage itemsPage =
                new ItemsPage(driver);

        itemsPage.addItem();

        itemsPage.clickShowHistoryForFirstItem();

        assertThat(
                itemsPage.isRentsPageDisplayed()
        ).isTrue();
    }
}