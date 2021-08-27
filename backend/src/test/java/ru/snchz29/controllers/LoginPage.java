package ru.snchz29.controllers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    public WebDriver driver;
    @FindBy(xpath = "//*[@id=\"login_submit\"]/div/div/input[6]")
    private WebElement loginField;
    @FindBy(xpath = "//*[@id=\"login_submit\"]/div/div/input[7]")
    private WebElement passwordField;
    @FindBy(xpath = "//*[contains(@id, 'install_allow')]")
    private WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void inputLogin(String login) {
        loginField.sendKeys(login);
    }

    public void inputPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void clickLoginBtn() {
        loginBtn.click();
    }
}
