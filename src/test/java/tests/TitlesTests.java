package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ItemsPage;
import pages.LoginPage;
import pages.TitlesPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class TitlesTests extends BaseTest {

    private TitlesPage titlesPage;

    @BeforeEach
    void login() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                ConfigReader.getProperty("valid.login"),
                ConfigReader.getProperty("valid.password")
        );

        titlesPage = new TitlesPage(driver);
    }

    @Test
    @DisplayName("should display titles page")
    void shouldDisplayTitlesPage() {     // TC #6 Wyświetlenie listy tytułów

        assertThat(
                titlesPage.isTitlesPageDisplayed()
        ).isTrue();
    }

    @Test
    @DisplayName("should add new title")
    void shouldAddNewTitle() {       // TC #7 Dodanie nowego tytułu

        String title =
                TestDataGenerator.generateTitle();

        titlesPage.addTitle(
                title,
                TestDataGenerator.generateAuthor(),
                TestDataGenerator.getDefaultYear()
        );

        assertThat(
                titlesPage.isTitleVisible(title)
        ).isTrue();
    }

    @Test
    @DisplayName("should not add empty title")
    void shouldNotAddEmptyTitle() {      // TC #8 Walidacja pustego formularza

        titlesPage.submitEmptyTitleForm();

        assertThat(
                titlesPage.getErrorMessage()
        ).contains("shouldn't be empty");
    }

    @Test
    @DisplayName("should edit title")
    void shouldEditTitle() {     // TC #9 Edycja tytułu

        String oldTitle =
                TestDataGenerator.generateTitle();

        titlesPage.addTitle(
                oldTitle,
                TestDataGenerator.generateAuthor(),
                TestDataGenerator.getDefaultYear()
        );

        String newTitle =
                oldTitle + "_edited";

        titlesPage.editTitle(
                oldTitle,
                newTitle
        );

        assertThat(
                titlesPage.isTitleVisible(newTitle)
        ).isTrue();
    }

    @Test
    @DisplayName("should remove title")
    void shouldRemoveTitle() {      // TC #10 Usunięcie tytułu

        String title =
                TestDataGenerator.generateTitle();

        titlesPage.addTitle(
                title,
                TestDataGenerator.generateAuthor(),
                TestDataGenerator.getDefaultYear()
        );

        titlesPage.removeTitle(title);

        assertThat(
                titlesPage.isTitleVisible(title)
        ).isFalse();
    }

    @Test
    @DisplayName("should navigate to items page")
    void shouldNavigateToItemsPage() {      // TC #11 Przejście do listy egzemplarzy

        String title =
                TestDataGenerator.generateTitle();

        titlesPage.addTitle(
                title,
                TestDataGenerator.generateAuthor(),
                TestDataGenerator.getDefaultYear()
        );

        titlesPage.openItemsForTitle(title);

        ItemsPage itemsPage = new ItemsPage(driver);

        assertThat(
                itemsPage.isItemsPageDisplayed()
        ).isTrue();
    }

    @Test
    @DisplayName("should keep empty form opened")
    void shouldKeepEmptyFormOpened() {      // TC #12 Blokada zapisu pustego formularza

        titlesPage.submitEmptyTitleForm();

        assertThat(
                titlesPage.isTitleFormDisplayed()
        ).isTrue();
    }

    @Test
    @DisplayName("should keep title after relogin")
    void shouldKeepTitleAfterRelogin() {        // #13.1 Zachowanie tytułu po ponownym zalogowaniu

        TitlesPage titlesPage = new TitlesPage(driver);

        String title = TestDataGenerator.generateTitle();

        titlesPage.addTitle(
                title,
                TestDataGenerator.generateAuthor(),
                TestDataGenerator.getDefaultYear()
        );

        driver.navigate().refresh();

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                ConfigReader.getProperty("valid.login"),
                ConfigReader.getProperty("valid.password")
        );

        TitlesPage titlesPageAfterLogin = new TitlesPage(driver);

        assertThat(
                titlesPageAfterLogin.isTitleVisible(title)
        ).isTrue();
    }

    @Test
    @DisplayName("should redirect to login after refresh")
    void shouldRedirectToLoginAfterRefresh() {      // #13.2 Przekierowanie do ekranu logowania po odświeżeniu strony

        driver.navigate().refresh();

        assertThat(
                driver.getCurrentUrl()
        ).contains("/login");
    }

    @Test
    @DisplayName("should not remove title with copies")
    void shouldNotRemoveTitleWithCopies() {     // TC #14 Usunięcie tytułu posiadającego egzemplarze

        String title = TestDataGenerator.generateTitle();

        titlesPage.addTitle(
                title,
                TestDataGenerator.generateAuthor(),
                TestDataGenerator.getDefaultYear()
        );

        titlesPage.openItemsForTitle(title);

        ItemsPage itemsPage = new ItemsPage(driver);

        itemsPage.addItem();

        driver.navigate().back();

        assertThat(titlesPage.isTitlesPageDisplayed())
                .isTrue();

        titlesPage.clickRemoveTitle(title);

        assertThat(titlesPage.getErrorMessage())
                .contains("You can't remove titles with copies");
    }
}