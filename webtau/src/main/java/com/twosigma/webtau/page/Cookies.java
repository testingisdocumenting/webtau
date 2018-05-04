package com.twosigma.webtau.page;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class Cookies {
    private final WebDriver driver;

    public Cookies(WebDriver driver) {
        this.driver = driver;
    }

    public void add(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
    }

    public void delete(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    public void deleteAll() {
        driver.manage().deleteAllCookies();
    }
}
