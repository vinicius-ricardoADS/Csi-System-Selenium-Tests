package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import javax.xml.xpath.XPath;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CsiTests {

    private static final String BASE_URL = "http://localhost:3000/";
    private WebDriver driver;

    @BeforeEach
    void setUp(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }
    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Test
    @DisplayName("Simple test")
    void simpleTest () throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(2000);
    }

    @Nested
    @DisplayName ("CRUD")
    class Crud {
        @Test
        @DisplayName("Being on crimes page, clicking on the logo should return to the main page")
        void beingOnCrimesPageClickingOnTheLogoShouldReturnToTheMainPage () throws InterruptedException {
            driver.get(BASE_URL + "crimes");
            final WebElement logo = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//img")));
            logo.click();
            final String current_url = driver.getCurrentUrl();
            assertEquals(BASE_URL, current_url);
        }

        @Test
        @DisplayName("Being on register page, clicking on the logo should return to the main page")
        void beingOnRegisterPageClickingOnTheLogoShouldReturnToTheMainPage () {
            driver.get(BASE_URL + "crimes/register");
            final WebElement logo = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//img")));
            logo.click();
            final String current_url = driver.getCurrentUrl();
            assertEquals(BASE_URL, current_url);
        }

        @Test
        @DisplayName("Being on crimes page, clicking on the logo present in the footer should return to the main page")
        void beingOnCrimesPageClickingOnTheLogoPresentInTheFooterShouldReturnToTheMainPage () {
            driver.get(BASE_URL + "crimes");
            final WebElement logo = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//footer/a/img")));
            logo.click();
            final String current_url = driver.getCurrentUrl();
            assertEquals(BASE_URL, current_url);
        }

        @Test
        @DisplayName("Being on register page, clicking on the logo present in the footer should return to the main page")
        void beingOnRegisterPageClickingOnTheLogoPresentInTheFooterShouldReturnToTheMainPage () {
            driver.get(BASE_URL + "crimes/register");
            final WebElement logo = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//footer/a/img")));
            logo.click();
            final String current_url = driver.getCurrentUrl();
            assertEquals(BASE_URL, current_url);
        }

        @Test
        @DisplayName("Being on home page, clicking on 'Registros Atuais' should lead to crimes page.")
        void beingOnHomePageClickingOnRegistrosAtuaisShouldLeadToCrimesPage(){
            driver.get(BASE_URL);
            final WebElement crimes_link = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//ul//a"))
                    );
            crimes_link.click();
            final String current_url = driver.getCurrentUrl();
            assertEquals((BASE_URL + "crimes"), current_url);
        }

        @Test
        @DisplayName("Being on home page, clicking on 'Cadastrar' should lead to crime register page.")
        void beingOnHomePageClickingOnCadastrarShouldLeadToCrimeRegisterPage(){
            driver.get(BASE_URL);
            final WebElement crimes_link = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//ul//li[2]/a"))
                    );
            crimes_link.click();
            final String current_url = driver.getCurrentUrl();
            assertEquals((BASE_URL + "crimes/register"), current_url);
        }
        
        @Test
        @DisplayName("Click on the link 'Visualizar registros' and go to 'crimes', present in the image on the left when hovering the mouse over it")
        void clickOnTheLinkCurrentRecordsAndGoToCrimesPresentInTheImageOnTheLeftWhenHoveringTheMouseOverIt () throws InterruptedException {
            driver.get(BASE_URL);
            final WebElement element = driver.findElement(By.xpath("//div[@class='d-flex']//div[@class='middle']"));
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            actions.click().perform();
            assertEquals((BASE_URL + "crimes"), driver.getCurrentUrl());
        }
        
        @Test
        @DisplayName("Click on the link 'Cadastrar' and go to 'crimes/register', present in the image on the right when hovering the mouse over it")
        void clickOnTheLinkCadastrarAndGoToCrimesRegisterPresentInTheImageOnTheRightWhenHoveringTheMouseOverIt () {
            driver.get(BASE_URL);
            final WebElement element = driver.findElement(By.xpath("//div[@class='d-flex']//div[2]//div[@class='middle']"));
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            actions.click().perform();
            assertEquals((BASE_URL + "crimes/register"), driver.getCurrentUrl());
        }
    }
}
