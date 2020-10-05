package io.baselogic.springsecurity.web.functional.objects;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * See https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-webdriver-why
 */
// Lombok Annotations:
@Data
@Slf4j
public class IndexPage //extends AbstractPage
{

//    static String URL = "https://localhost:8443/";
    static String URL = "http://localhost:8080/";

    private WebDriver driver;

    //-----------------------------------------------------------------------//
    // Standard Page Elements

    private boolean isUserAuthenticated = false;


    @FindBy(id = "pageTitle")
    private WebElement title;
    private String expectedTitle = "Welcome to the EventManager!";

    @FindBy(id = "chapterHeading")
    private WebElement chapterHeading;
    private String expectedChapterHeading = "Chapter 07.01";

    @FindBy(id = "summary")
    private WebElement summary;
    private String expectedSummary = "Advanced Certificate Authentication";

    //-----------------------------------------------------------------------//
    // Page Specific Elements:

    @FindBy(id = "navLoginLink")
    private WebElement loginLink;

    @FindBy(id = "navLogoutLink")
    private WebElement logoutLink;

    @FindBy(xpath = "<p class=\"navbar-text\">")
    private WebElement authenticationName;

    @FindBy(id = "navRegistrationLink")
    private WebElement registrationLink;


    public IndexPage(WebDriver driver) {
        log.info("*************************************************************");
        log.info("public IndexPage(WebDriver driver)");
        log.info("*************************************************************");
        this.driver = driver;
    }


    public <T> T viewHomePage(String heading, String summary) throws Exception {

//        HtmlPage result = login.click();
//        boolean error = IndexPage.at(result);

//        return (T) (error ? new IndexPage(result) : new ViewMessagePage(result));
        return null; //(T) (new IndexPage(result));
    }

    //-----------------------------------------------------------------------//

    public LoginPage clickLogin() {
        loginLink.click();
        return LoginPage.to(driver);
    }

    //@Override
    public String getTitle() {
        return driver.getTitle();
    }


    //@Override
    public void currentLocation() {
        log.info("*************************************************************");
        log.info("driver.getCurrentUrl: {}", driver.getCurrentUrl());
//        log.info("current source: {}", driver.getPageSource());
        log.info("*************************************************************");
    }

    //-----------------------------------------------------------------------//

//    @Override
    public boolean validate(boolean isUserAuthenticated) {
        this.isUserAuthenticated = isUserAuthenticated;

        return validate();
    }


//    @Override
    public boolean validate() {

        boolean result = validateStandard();

        log.info("*************************************************************");
        log.info("validate()");
        log.info("*************************************************************");

        log.info("isUserAuthenticated(): {}", isUserAuthenticated());
        log.info("*************************************************************");

        if(isUserAuthenticated()) {
            log.info("logoutLink: {}", logoutLink);
            assertThat(getLogoutLink().isDisplayed()).isEqualTo(true);
            assertThat(getLogoutLink().isEnabled()).isEqualTo(true);

            log.info("authenticationName: {}", authenticationName);
//            assertThat(getLogoutLink().isDisplayed()).isEqualTo(true);
//            assertThat(getLogoutLink().isEnabled()).isEqualTo(true);

        } else {
            log.info("login: {}", loginLink.isDisplayed());
            log.info("login: {}", loginLink.isEnabled());
            assertThat(getLoginLink().isDisplayed()).isEqualTo(true);
            assertThat(getLoginLink().isEnabled()).isEqualTo(true);

            log.info("registrationLink: {}", registrationLink.isDisplayed());
            log.info("registrationLink: {}", registrationLink.isEnabled());
            assertThat(getRegistrationLink().isDisplayed()).isEqualTo(true);
            assertThat(getRegistrationLink().isEnabled()).isEqualTo(true);
        }

        return result;
    }

//    @Override
    public boolean validateStandard() {

        boolean result = true;

        log.info("*************************************************************");
        log.info("validateStandard()");
        log.info("*************************************************************");

        log.info("pageTitle: {}", getTitle());
        assertThat(getTitle()).isEqualTo(getExpectedTitle());

        log.info("chapterHeading: {}", chapterHeading.getText());
        assertThat(getChapterHeading().getText()).isEqualTo(getExpectedChapterHeading());

        log.info("summary: {}", summary.getText());
        assertThat(getSummary().getText()).isEqualTo(getExpectedSummary());

        return result;
    }

//    public static boolean at(HtmlPage page) {
//        return "Welcome to the EventManager!".equals(page.getTitleText());
//    }

    //    @Override
    public void navigateTo() {
        log.info("navigateTo( {} )", URL);
        driver.get(URL);
        currentLocation();
    }

    //@Override
    public static <T> T to(WebDriver driver) {
        IndexPage page = PageFactory.initElements(driver, IndexPage.class);

        return (T) page;
    }


    static final String targetDir = "./target/webDriver/";


    /**
     * This function will take screenshot
     * @param webdriver
     * @param fileWithPath
     * @throws Exception
     */
    public void takeSnapShot(WebDriver webdriver, String fileWithPath) {
//        try {
            //Convert web driver object to TakeScreenshot
            log.info("**** driver: {} ***", driver.getClass());
            WebConnectionHtmlUnitDriver d;
            /*TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

            //Call getScreenshotAs method to create image file
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

            //Move image file to new destination
            File DestFile = new File(targetDir + fileWithPath);
            //Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);*/
//        } catch(IOException e){
//            log.error("takeSnapShot IOException: {}", e.getMessage());
//        }
    }


    // Setter / Getter generated by Lombok

} // The End...
