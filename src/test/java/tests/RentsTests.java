package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ItemsPage;
import pages.LoginPage;
import pages.RentsPage;
import pages.TitlesPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class RentsTests extends BaseTest {

    private RentsPage rentsPage;
    private String itemId;

    @BeforeEach
    void loginAndOpenRentsPage() {

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

        itemsPage.addItem();

        itemId = itemsPage.getLastItemId();

        itemsPage.clickShowHistoryById(itemId);

        rentsPage =
                new RentsPage(driver);
    }

    @Test
    @DisplayName("should add rent")
    void shouldAddRent() {      // TC #19 Dodanie wypożyczenia

        int before =
                rentsPage.getRentsCount();

        rentsPage.addRent("George Clooney");

        assertThat(
                rentsPage.getRentsCount()
        ).isGreaterThan(before);
    }

    @Test
    @DisplayName("should validate empty rent form")
    void shouldValidateEmptyRentForm() {        // TC #20 Walidacja pustych danych

        rentsPage.openAddRentForm();

        rentsPage.submitEmptyForm();

        assertThat(
                rentsPage.isValidationErrorDisplayed()
        ).isTrue();
    }

    @Test
    @DisplayName("should edit rent")
    void shouldEditRent() {     // TC #21 Edycja wypożyczenia

        rentsPage.addRent("Brad Pitt with Jennifer");

        String before =
                rentsPage.getFirstCustomerName();

        rentsPage.editFirstRent(
                "Brad Pitt with Angelina");

        String after =
                rentsPage.getFirstCustomerName();

        assertThat(after)
                .isNotEqualTo(before);
    }

    @Test
    @DisplayName("should not rent already rented item")
    @Disabled("BUG TC#22: aplikacja pozwala ponownie wypożyczyć ten sam egzemplarz")
    void shouldNotRentAlreadyRentedItem ()  {

        rentsPage.addRent("Matt Damon");

        int rentsBeforeSecondAttempt = rentsPage.getRentsCount();

        rentsPage.addRent("Ben Affleck");

        assertThat(
                rentsPage.getRentsCount()
        ).isEqualTo(rentsBeforeSecondAttempt);
    }

    @Test
    @DisplayName("should remove rent")
    void shouldRemoveRent() {       // TC #23 Usunięcie wypożyczenia

        rentsPage.addRent("George Clooney");

        int before = rentsPage.getRentsCount();

        rentsPage.removeFirstRent();

        assertThat(rentsPage.getRentsCount())
                .isEqualTo(before - 1);
    }

    @Test
    @DisplayName("should change item status after rent")
    @Disabled("BUG TC#24: status egzemplarza pozostaje Available po wypożyczeniu")
    void shouldChangeItemStatusAfterRent()  {

        rentsPage.addRent("Matt Damon");

        driver.navigate().back();

        ItemsPage itemsPage = new ItemsPage(driver);

        assertThat(
                itemsPage.getItemStatusById(itemId)
        ).isEqualTo("rented");
    }
}