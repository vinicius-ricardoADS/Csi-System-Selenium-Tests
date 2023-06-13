package org.example;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsiTests {

    private static final String BASE_URL = "http://localhost:3000/";
    private WebDriver driver;
    private Faker faker;

    @BeforeEach
    void setUp(){
        faker = new Faker();

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }
    @AfterEach
    void tearDown(){
        driver.quit();
    }

    @Nested
    @DisplayName ("Requests GET")
    class RequestsGet {
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
            assertEquals(BASE_URL, driver.getCurrentUrl());
        }

        @Test
        @DisplayName("Being on crimes page, clicking on the logo present in the footer should return to the main page")
        void beingOnCrimesPageClickingOnTheLogoPresentInTheFooterShouldReturnToTheMainPage () throws InterruptedException {
            driver.get(BASE_URL + "crimes");
            final WebElement logo = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//footer//a[@href='/']")));

            var actions = new Actions(driver).scrollByAmount(0, ((Long) ((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight")).intValue());

            actions.perform();
            logo.click();
            assertEquals(BASE_URL, driver.getCurrentUrl());
        }

        @Test
        @DisplayName("Being on register page, clicking on the logo present in the footer should return to the main page")
        void beingOnRegisterPageClickingOnTheLogoPresentInTheFooterShouldReturnToTheMainPage () {
            driver.get(BASE_URL + "crimes/register");
            final WebElement logo = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//footer/a/img")));
            logo.click();
            assertEquals(BASE_URL, driver.getCurrentUrl());            
        }

        @Test
        @DisplayName("Being on home page, clicking on 'Registros Atuais' should lead to crimes page.")
        void beingOnHomePageClickingOnRegistrosAtuaisShouldLeadToCrimesPage(){
            driver.get(BASE_URL);
            final WebElement crimesLink = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//ul//a"))
                    );
            crimesLink.click();
            assertEquals((BASE_URL + "crimes"), driver.getCurrentUrl());
        }

        @Test
        @DisplayName("Being on home page, clicking on 'Cadastrar' should lead to crime register page.")
        void beingOnHomePageClickingOnCadastrarShouldLeadToCrimeRegisterPage(){
            driver.get(BASE_URL);
            final WebElement crimesLink = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//ul//li[2]/a"))
                    );
            crimesLink.click();
            assertEquals((BASE_URL + "crimes/register"), driver.getCurrentUrl());
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

        @Test
        @DisplayName("Once on the page to register a crime, clicking on 'Cancelar' should return to '/crimes'")
        void onceOnThePageToRegisterACrimeClickingOnCancelarShouldReturnToCrimes () {
            driver.get(BASE_URL + "crimes/register");
            final WebElement btnCancel = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//input[@class='btn']"))
                    );
            btnCancel.click();
            assertEquals((BASE_URL + "crimes"), driver.getCurrentUrl());
        }
    }

    @Nested
    @DisplayName ("Testing CRUD")
    class TestingCRUD {
        @Test
        @DisplayName("Should remove crime clicking on button 'Excluir'")
        void shouldRemoveCrimeClickingOnButtonExcluir () {
            driver.get(BASE_URL + "crimes");
            final WebElement tbodyInitial = driver.findElement(By.tagName("tbody"));
            final List<WebElement> trInitial = tbodyInitial.findElements(By.tagName("tr"));
            final WebElement btnDelete = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[2]")
                    ));
            btnDelete.click();
            final WebElement tbodyRefresh = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(
                            By.tagName("tbody")
                    )));
            final List<WebElement> trModified = tbodyRefresh.findElements(By.tagName("tr"));
            assertThat(trModified.size()).isLessThan(trInitial.size());
        }

        @Test
        @DisplayName("Should create crime using the form in page register")
        void shouldCreateCrimeUsingTheFormInPageRegister() {
            driver.get(BASE_URL + "crimes/register");
            new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//input[@class='btn']"))
                    );

            final var inputElements = driver.findElements(By.cssSelector(".form-control"));

            final var crimeSuspectElement = inputElements.get(0);
            final var crimeTypeElement = inputElements.get(1);
            final var crimeLocationElement = inputElements.get(2);
            final var crimeDateElement = inputElements.get(3);

            assertThat(crimeSuspectElement.getAttribute("name")).isEqualTo("crimeSuspect");
            assertThat(crimeTypeElement.getAttribute("name")).isEqualTo("crimeType");
            assertThat(crimeLocationElement.getAttribute("name")).isEqualTo("crimeLocation");
            assertThat(crimeDateElement.getAttribute("name")).isEqualTo("crimeDate");

            final var sendButton = driver.findElement(By.xpath("//input[@class='btn'][@type='submit']"));

            final var fullName = faker.name().fullName();
            final var crimeType = String.join(" ", faker.lorem().words(10));
            final var crimeLocation = faker.address().streetAddress();

            final var crimeDate = LocalDateTime.now().minusDays(5);

            final var crimeDateCalendar = crimeDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
            final var crimeDateTime = crimeDate.format(DateTimeFormatter.ofPattern("HHmm"));

            crimeSuspectElement.sendKeys(fullName);
            crimeTypeElement.sendKeys(crimeType);
            crimeLocationElement.sendKeys(crimeLocation);

            crimeDateElement.sendKeys(crimeDateCalendar);
            crimeDateElement.sendKeys(Keys.TAB);

            crimeDateElement.sendKeys(crimeDateTime);

            assertThat(crimeSuspectElement.getAttribute("value")).isEqualTo(fullName);
            assertThat(crimeTypeElement.getAttribute("value")).isEqualTo(crimeType);
            assertThat(crimeLocationElement.getAttribute("value")).isEqualTo(crimeLocation);
            assertThat(crimeDateElement.getAttribute("value")).isEqualTo(crimeDate.format(DateTimeFormatter.ofPattern("yyyy-dd-MM'T'HH:mm")));

            sendButton.click();

            final var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL + "crimes"));
        }

        @Test
        @DisplayName("Should not create crime due to empty form")
        void shouldNotCreateCrimeDueToEmptyForm() throws InterruptedException {
            driver.get(BASE_URL + "crimes/register");
            new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//input[@class='btn']"))
                    );

            final var inputElements = driver.findElements(By.cssSelector(".form-control"));

            final var crimeSuspectElement = inputElements.get(0);
            final var crimeTypeElement = inputElements.get(1);
            final var crimeLocationElement = inputElements.get(2);
            final var crimeDateElement = inputElements.get(3);

            assertThat(crimeSuspectElement.getAttribute("name")).isEqualTo("crimeSuspect");
            assertThat(crimeTypeElement.getAttribute("name")).isEqualTo("crimeType");
            assertThat(crimeLocationElement.getAttribute("name")).isEqualTo("crimeLocation");
            assertThat(crimeDateElement.getAttribute("name")).isEqualTo("crimeDate");

            final var sendButton = driver.findElement(By.xpath("//input[@class='btn'][@type='submit']"));

            sendButton.click();

            final var warningElements = driver.findElements(By.cssSelector(".text-danger"));
            final var EXACT_NUMBER_OF_WARNINGS = 4;
            assertThat(warningElements.size()).isEqualTo(EXACT_NUMBER_OF_WARNINGS);
        }

        @Test
        @DisplayName("should be able to alter all values of an existing crime")
        void shouldBeAbleToEditAllValuesOfExistingCrime() {
            driver.get(BASE_URL);

            final WebElement buttonRegister = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//a[@href='/crimes']"))
                    );
            buttonRegister.click();

            WebElement rowCrime = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("/html/body/div/div/div/table/tbody/tr[1]"))
                    );

            final String suspectOldValue = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[2]")).getText();
            final String crimeOldValue = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[3]")).getText();
            final String crimeSceneOldValue = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[4]")).getText();
            final String dateOldValue = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[5]")).getText();

            final WebElement buttonEdit = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[6]/div/button[1]"));

            buttonEdit.click();

            final WebElement suspectInput = driver.findElement(By.xpath("//input[@name='crimeSuspect']"));
            final WebElement crimeInput = driver.findElement(By.xpath("//input[@name='crimeType']"));
            final WebElement crimeSceneInput = driver.findElement(By.xpath("//input[@name='crimeLocation']"));
            final WebElement dateInput = driver.findElement(By.xpath("//input[@name='crimeDate']"));

            final LocalDateTime randomOldDate = LocalDateTime.now().minusYears(20);

            final var crimeDateCalendar = randomOldDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
            final var crimeDateTime = randomOldDate.format(DateTimeFormatter.ofPattern("HHmm"));

            suspectInput.sendKeys("a");
            crimeInput.sendKeys("a");
            crimeSceneInput.sendKeys("a");

            dateInput.sendKeys(crimeDateCalendar);
            dateInput.sendKeys(Keys.chord(Keys.TAB));
            dateInput.sendKeys(crimeDateTime);

            driver.findElement(By.xpath("/html/body/div/div/div/form/div[5]/input[2]")).click();

            rowCrime = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("/html/body/div/div/div/table/tbody/tr[1]"))
                    );

            final String suspectTdElement  = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[2]")).getText();
            final String crimeInputTdElement = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[3]")).getText();
            final String crimeSceneTdElement = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[4]")).getText();
            final String dateInputTdElement = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[5]")).getText();

            assertThat(suspectTdElement).isNotEqualTo(suspectOldValue);
            assertThat(crimeInputTdElement).isNotEqualTo(crimeOldValue);
            assertThat(crimeSceneTdElement).isNotEqualTo(crimeSceneOldValue);
            assertThat(dateInputTdElement).isNotEqualTo(dateOldValue);
        }

        @Test
        @DisplayName("Should not edit if date picked is further than today")
        void shouldNotEditIfDateSelectedIsFutherThanToday() {
            driver.get(BASE_URL);

            final WebElement buttonRegister = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//a[@href='/crimes']"))
                    );
            buttonRegister.click();

            WebElement rowCrime = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("/html/body/div/div/div/table/tbody/tr[1]"))
                    );

            final var buttonEdit = rowCrime.findElement(By.xpath("/html/body/div/div/div/table/tbody/tr[1]/td[6]/div/button[1]"));

            buttonEdit.click();

            final WebElement dateInput = driver.findElement(By.xpath("//input[@name='crimeDate']"));

            final var futureDate = LocalDate.now().plus(5, ChronoUnit.YEARS);
            final var crimeDateCalendar = futureDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));

            dateInput.sendKeys(crimeDateCalendar);

            final WebElement warning = new WebDriverWait(driver, Duration.ofSeconds(10)) // 10s timeout
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("/html/body/div/div/div/form/div[4]/div[2]/p"))
                    );

            driver.findElement(By.xpath("/html/body/div/div/div/form/div[5]/input[2]")).click();

            assertThat(warning).isNotNull();
        }
    }
}
