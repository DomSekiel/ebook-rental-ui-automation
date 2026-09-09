package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver createDriver(String browser, boolean headless) {

        switch (browser.toLowerCase()) {

            case "chrome":

                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();

                if (headless) {

                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");

                } else {

                    chromeOptions.addArguments("--start-maximized");
                }

                chromeOptions.addArguments("--disable-notifications");

                return new ChromeDriver(chromeOptions);

            case "firefox":

                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (headless) {

                    firefoxOptions.addArguments("-headless");
                }

                return new FirefoxDriver(firefoxOptions);

            default:

                throw new IllegalArgumentException(
                        "Browser not recognised: " + browser);
        }
    }
}