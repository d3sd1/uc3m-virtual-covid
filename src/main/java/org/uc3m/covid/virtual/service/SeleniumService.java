package org.uc3m.covid.virtual.service;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SeleniumService {
    @Autowired
    private ResourceLoader resourceLoader;
    @Value("${gecko.driver.version}")
    private String geckoDriverVersion;
    public WebDriver getDriver(String initialUrl) throws IOException {

        String driverPath = "classpath:drivers/selenium/gecko/%s/%s";
        if (SystemUtils.IS_OS_WINDOWS && SystemUtils.OS_ARCH.contains("64")) {
            driverPath = String.format(driverPath, this.geckoDriverVersion, "win64.exe");
        } else if (SystemUtils.IS_OS_WINDOWS && SystemUtils.OS_ARCH.contains("32")) {
            driverPath = String.format(driverPath, this.geckoDriverVersion, "win32.exe");
        } else if (SystemUtils.IS_OS_LINUX && SystemUtils.OS_ARCH.contains("64")) {
            driverPath = String.format(driverPath, this.geckoDriverVersion, "linux64");
        } else if (SystemUtils.IS_OS_LINUX && SystemUtils.OS_ARCH.contains("32")) {
            driverPath = String.format(driverPath, this.geckoDriverVersion, "linux32");
        } else if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            driverPath = String.format(driverPath, this.geckoDriverVersion, "macos");
        }
        Resource resource = resourceLoader.getResource(driverPath);

        System.setProperty("webdriver.gecko.driver", resource.getFile().getAbsolutePath());

        WebDriver driver = new FirefoxDriver();
        driver.navigate().to(initialUrl);

        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until((driver1) -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        return driver;
    }
}
