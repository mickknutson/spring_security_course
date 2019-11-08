package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage //extends AbstractPage
{

    @FindBy(css = "input[type=submit]")
    private WebElement submit;

//    public LoginPage(WebDriver driver) {
//        super(driver);
//    }

    public <T> T createMessage(Class<T> resultPage, String summary, String details) {
//        this.summary.sendKeys(summary);
//        this.text.sendKeys(details);
//        this.submit.click();
//        return PageFactory.initElements(driver, resultPage);
        return null;
    }

    public static LoginPage to(WebDriver driver) {
        driver.get("http://localhost:9990/mail/messages/form");
        return PageFactory.initElements(driver, LoginPage.class);
    }
} // The End...