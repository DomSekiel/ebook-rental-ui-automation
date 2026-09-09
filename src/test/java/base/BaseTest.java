package base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.DriverFactory;

public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {

        String browser =
                ConfigReader.getProperty("browser");

        boolean headless =
                Boolean.parseBoolean(
                        ConfigReader.getProperty("headless")
                );

        driver = DriverFactory.createDriver(
                browser,
                headless
        );

        driver.get(
                ConfigReader.getProperty("base.url")
        );
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}