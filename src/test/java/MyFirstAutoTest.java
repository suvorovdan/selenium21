import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MyFirstAutoTest {
    WebDriver driver;

    @Before
    public void startDriver(){
        /**назначение свойств вебдрайверу, который запускает наш тест с параметрами для браузера(в развёрнутом виде
         * и ожиданием загрузки страниц в 40 секунд) и открытие сайта РГС*/
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
        driver.manage().window().maximize();
//        Step 1
        driver.get("https://www.rgs.ru");
    }

    @After
    public void closeDriver(){
        /**Закрытие браузера по завершению теста без ошибок*/
        driver.quit();
    }

    @Test
    public void testRgsVacation(){
//        блок данных
        String dateOfB = "12.12.1984";
        String countries = "Шенген";
        String nameOfUser = "IVANOV VANO";
        String stateOfLeisure = "(включая активный отдых)";
        String newPattern = "//strong[@data-bind = \"%s\"]";
        String countryP = "text: Name";
        String dateP = " text: BirthDay.repr('moscowRussianDate')";
        String nameP = "text: LastName() + ' ' + FirstName()";
        Actions actions = new Actions(driver);
//        Step 2
//        открытие меню страхование
        WebElement insuranceNavBarButton = findByXpath("//*[@id='main-navbar-collapse']//a[contains(text(), 'Страхование')]");
        insuranceNavBarButton.click();
//        Step 3
//        открытие ссылки на страхование за рубежом
        WebElement abroadNavBarButton = findByXpath("//*[@id='main-navbar-collapse']//a[contains(text(), 'за рубеж')]");
        abroadNavBarButton.click();
//        Step 4
//        нажатие на кнопку купить полис онлайн
        WebElement buyInsurenceOnlineButton = findByXpath("//a[contains(text(),'Купить полис онлайн')]");
        buyInsurenceOnlineButton.click();
//        Step 5
//        проверка заголовка на соответствие
        WebElement abroadInsurenceHeader = findByXpath("//span[@class='h1' and contains(text(),'Страхование выезжающих')]");
        String expectedText = "Страхование выезжающих за рубеж";
        Assert.assertEquals("Значения заголовка не соотвествует  ожидаемому", expectedText, abroadInsurenceHeader.getText());
//        Step 6
//        ожидание появления кнопки несколько поездок в течение года
        WebDriverWait wait = new WebDriverWait(driver, 8);
        wait.pollingEvery(Duration.ofMillis(300))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//span[@class = 'btn-content-title']")));
        WebElement fewJourneyButton = findByXpath("//span[@class = 'btn-content-title']");
        wait.pollingEvery(Duration.ofMillis(300)).until(ExpectedConditions.elementToBeClickable(fewJourneyButton));

//        согласие на обработку данных
        driver.findElement(By.xpath("//label[contains(text(),'Я согласен на')]/preceding-sibling::input")).click();
        fewJourneyButton.click();

//        выбор страны Шенген
        wait.pollingEvery(Duration.ofMillis(300))
                .until(ExpectedConditions
                        .elementToBeClickable(By
                                .xpath("//input[@class ='form-control-multiple-autocomplete-actual-input tt-input']")));
        WebElement whereWeGoInput = findByXpath("//input[@class ='form-control-multiple-autocomplete-actual-input tt-input']");
        whereWeGoInput.sendKeys(countries);
        wait.pollingEvery(Duration.ofMillis(300))
                .until(ExpectedConditions
                        .elementToBeClickable(By
                                .xpath("//div[@class = 'tt-menu tt-open']")));
        WebElement shengen = findByXpath("//div[@class = 'tt-menu tt-open']");
        shengen.click();

//        выбор страны Испания
        WebElement selectArrivalCountry = findByXpath("//select[@id = 'ArrivalCountryList']");
        wait.pollingEvery(Duration.ofMillis(300)).until(ExpectedConditions.elementToBeClickable(selectArrivalCountry));
        actions.click(selectArrivalCountry).sendKeys("Испания").click(selectArrivalCountry).perform();

//        выбор даты поездки
        WebElement selectDateOfDeparture = findByXpath("//text()[contains(.,'Дата первой поездки')]//following::input[1]");
        selectDateOfDeparture.sendKeys(Keys.ENTER);
        selectDateOfDeparture.click();
        selectDateOfDeparture.sendKeys("01.11.2018");

//        выбор кнопки не более 90 дней
        WebElement notMore90DaysButton = findByXpath("//div/label[text()[contains(.,'Не более 90 дней')]]");
        notMore90DaysButton.click();

//        Ввод имени и фамилии
        WebElement lastNameInput = findByXpath("//span[contains(text(),'Фамилия')]//following::input[1]");
        lastNameInput.sendKeys(nameOfUser);

//        Сдвиг вниз к кнопке рассчитать

        actions.moveToElement(findByXpath("//button[contains(text(),'Рассчитать')]"));
        actions.perform();

//        Ввод даты рождения
        WebElement dateOfBirth =findByXpath("//label[contains(text(),'Дата рождения')]//following::input[1]");
        dateOfBirth.click();
        dateOfBirth.sendKeys(dateOfB);

//        Сдвиг вниз к кнопке рассчитать
        actions.moveToElement(findByXpath("//button[contains(text(),'Рассчитать')]"));
        actions.perform();

//        Выбор опции активного отдыха
        WebElement leisureButton = findByXpath("//span[contains(text(),'Планируется')]//preceding::div[@data-toggle ='toggle']");
        leisureButton.click();

//        Нажатие на кнопку рассчитать
        WebElement buttonCalculate = findByXpath("//button[contains(text(),'Рассчитать')]");
        buttonCalculate.sendKeys(Keys.ENTER);

//        проверка что прогрузилась форма с нашими введёнными данными
        wait.pollingEvery(Duration.ofMillis(300))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By
                                .xpath("//span[@class ='text-bold']")));

//        Сдвиг к форме(вверх)
        WebElement topOfPage = findByXpath("//h2[contains(text(),'Расчет')]");
        actions.moveToElement(topOfPage);
        actions.perform();

//        Всё что ниже - проверка на правильность заполнения(можно запусть в дебагере и перед проверкой остановить,
//        потом поменять проверяемые данные и посмотреть на вывод ошибок)
//        поездки
        String expectedJourneyText = "Многократные поездки в течение года";
        Assert.assertEquals("Не выбрано Многократные поездки",
                expectedJourneyText,
                findByXpath("//span[@class ='text-bold']").getText());

        actions.moveToElement(topOfPage);
        actions.perform();

        wait.pollingEvery(Duration.ofMillis(300))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By
                                .xpath(String.format(newPattern,countryP))));

//        страны прибытия(шенген)
        Assert.assertEquals("Несовпадение стран",
                countries,
                findByXpath(String.format(newPattern,countryP)).getText());

        actions.moveToElement(topOfPage);
        actions.perform();

//        имени и фамилии
        Assert.assertEquals("Не совпадение имени",
                nameOfUser,
                findByXpath(String.format(newPattern,nameP)).getText());

        actions.moveToElement(topOfPage);
        actions.perform();

//        даты рождения
        Assert.assertEquals("Не совпадение даты рождения",
                dateOfB,
                findByXpath(String.format(newPattern,dateP)).getText());

        actions.moveToElement(topOfPage);
        actions.perform();

//        типа активного отдыха
        Assert.assertEquals("Не совпадение состояния активного отдыха",
                stateOfLeisure,
                findByXpath("//span[@class = 'summary-key']//following::small[1]").getText());
    }

    private WebElement findByXpath(String xpath) {
            return driver.findElement(By.xpath(xpath));
    }


}
