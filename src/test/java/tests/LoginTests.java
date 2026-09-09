package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.TitlesPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    void initPage() {
        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("should login with valid credentials")
    void shouldLoginWithValidCredentials() { // TC #1 Logowanie poprawne

        loginPage.login(
                ConfigReader.getProperty("valid.login"),
                ConfigReader.getProperty("valid.password")
        );

        TitlesPage titlesPage = new TitlesPage(driver);

        assertThat(titlesPage.isTitlesPageDisplayed())
                .isTrue();
    }

    @Test
    @DisplayName("should not login with invalid password")
    void shouldNotLoginWithInvalidPassword() { // TC #2 Logowanie błędne hasło

        loginPage.login(
                ConfigReader.getProperty("valid.login"),
                ConfigReader.getProperty("invalid.password")
        );

        TitlesPage titlesPage = new TitlesPage(driver);

        assertThat(titlesPage.isTitlesPageDisplayed())
                .isFalse();

        assertThat(loginPage.getErrorMessage())
                .contains("Login failed");
    }

    @Test
    @DisplayName("should not login with empty credentials")
    void shouldNotLoginWithEmptyCredentials() { // TC #3 Logowanie puste pola

        loginPage.login("", "");

        TitlesPage titlesPage = new TitlesPage(driver);

        assertThat(titlesPage.isTitlesPageDisplayed())
                .isFalse();

        assertThat(loginPage.getErrorMessage())
                .isNotEmpty();
    }

    @Test
    @DisplayName("should not login after multiple invalid attempts")
    void shouldNotLoginAfterMultipleInvalidAttempts() { // TC #4 Wielokrotne błędne logowanie

        for (int i = 0; i < 3; i++) {

            loginPage.login(
                    ConfigReader.getProperty("valid.login"),
                    ConfigReader.getProperty("invalid.password")
            );

            assertThat(loginPage.getErrorMessage())
                    .contains("Login failed");
        }

        TitlesPage titlesPage = new TitlesPage(driver);

        assertThat(titlesPage.isTitlesPageDisplayed())
                .isFalse();
    }

    @Test
    @DisplayName("should register new user")
    void shouldRegisterNewUser() { // TC #5 Rejestracja użytkownika

        String login =
                TestDataGenerator.generateLogin() + "@bayern.de";

        String password =
                ConfigReader.getProperty("valid.password");

        loginPage.register(
                login,
                password
        );

        driver.navigate().to(
                ConfigReader.getProperty("base.url")
        );

        loginPage = new LoginPage(driver);

        loginPage.login(
                login,
                password
        );

        TitlesPage titlesPage = new TitlesPage(driver);

        assertThat(titlesPage.isTitlesPageDisplayed())
                .isTrue();
    }
}