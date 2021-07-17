package org.uc3m.covid.virtual.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.uc3m.covid.virtual.entity.Subject;
import org.uc3m.covid.virtual.exception.InvalidSubjectException;
import org.uc3m.covid.virtual.model.Login;
import org.uc3m.covid.virtual.model.UserBasicData;
import org.uc3m.covid.virtual.repository.SubjectRepository;
import org.uc3m.covid.virtual.service.SeleniumService;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthScraper {
    private final SeleniumService seleniumService;
    private final SubjectRepository subjectRepository;

    public AuthScraper(SeleniumService seleniumService, SubjectRepository subjectRepository) {
        this.seleniumService = seleniumService;
        this.subjectRepository = subjectRepository;
    }

    public UserBasicData doLogin(Login login) throws IOException, LoginException {
        UserBasicData userBasicData = new UserBasicData();
        WebDriver driver = this.seleniumService.getDriver("https://aulaglobal.uc3m.es/");
        driver.findElement(By.xpath("/html/body/div/div[4]/div[1]/div[1]/div/table/tbody/tr/td[1]/form/div/div[1]/div[2]/input"))
                .sendKeys(Long.toString(login.getUserUc3mId()));

        driver.findElement(By.xpath("/html/body/div/div[4]/div[1]/div[1]/div/table/tbody/tr/td[1]/form/div/div[2]/div[2]/input"))
                .sendKeys(login.getPass());

        driver.findElement(By.xpath("/html/body/div/div[4]/div[1]/div[1]/div/table/tbody/tr/td[1]/form/div/div[3]/input"))
                .click();
        (new WebDriverWait(driver, 30)).until((driver1) -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));

        try {
            driver.findElement(By.id("login_authn_error_msg")).isDisplayed();
            driver.quit();
            throw new LoginException();
        } catch (NoSuchElementException ignored) {
        }

        userBasicData.setSubjects(this.getUserSubjects(driver));
        userBasicData.setFullName(this.getUserFullname(driver));
        userBasicData.setUserMoodleId(this.getUserMoodleId(driver));
        userBasicData.setEmail(this.getUserEmail(driver, userBasicData.getUserMoodleId()));
        driver.quit();

        return userBasicData;
    }

    private String getUserFullname(WebDriver driver) {
        return driver.findElement(By.id("dropdown-1")).findElement(By.cssSelector("span > span"))
                .getText();
    }


    private Set<Subject> getUserSubjects(WebDriver driver) {
        Set<Subject> subjects = new HashSet<>();
        List<WebElement> courses = driver.findElements(By.cssSelector(".courses > .coursebox"));
        for (WebElement course : courses) {
            String courseName = course.getText();
            String courseUrl = course.findElement(By.cssSelector(".info .coursename > a")).getAttribute("href");
            try {
                Subject subject = this.detectSubject(courseName, courseUrl);
                subjects.add(subject);
            } catch (InvalidSubjectException ignored) {

            }
        }
        return subjects;
    }

    private Subject detectSubject(String courseName, String courseUrl) throws InvalidSubjectException {
        int courseId = Integer.parseInt(courseUrl.split("id=")[1]);
        courseName = courseName.replace("* ", " ");
        Pattern patternPractical = Pattern.compile("(\\w+).(\\w+).(\\w+)-(\\w+) ([^1-9\\/1-9]+) (\\w+)\\/(\\w+)-(\\w+)");   // the pattern to search for
        Matcher matchesPractical = patternPractical.matcher(courseName);
        Pattern patternMagistral = Pattern.compile("(\\w+).(\\w+).(\\w+)-(\\w+) MAG. ([^1-9\\/1-9]+) (\\w+)\\/(\\w+)-(\\w+)");   // the pattern to search for
        Matcher matchesMagistral = patternMagistral.matcher(courseName);

        Matcher foundMatcher;


        boolean isPractical = false;
        boolean isMagistral = false;
        if (matchesMagistral.find()) {
            foundMatcher = matchesMagistral;
            isMagistral = true;
        } else if (matchesPractical.find()) {
            foundMatcher = matchesPractical;
            isPractical = true;
        } else {
            throw new InvalidSubjectException();
        }
        int subjectId = Integer.parseInt(foundMatcher.group(3));
        Subject subject = this.subjectRepository.findByCourseUc3mId(subjectId).orElseGet(() -> {
            Subject newSubject = new Subject();
            newSubject.setCourseUc3mId(subjectId);
            return newSubject;
        });

        if (isPractical) {
            subject.setMoodleCoursePraId(courseId);
        } else if (isMagistral) {
            subject.setMoodleCourseMagId(courseId);
        }

        subject.setName(foundMatcher.group(5));
        subject.setFromYear(Byte.parseByte(foundMatcher.group(6)));
        subject.setToYear(Byte.parseByte(foundMatcher.group(7)));
        subject.setGroup(Integer.parseInt(foundMatcher.group(4)));
        subject.setSemester(Byte.parseByte(foundMatcher.group(8).toLowerCase().replace("c", "")));
        subject = this.subjectRepository.save(subject);
        return subject;
    }

    private long getUserMoodleId(WebDriver driver) {
        return Long.parseLong(driver.findElement(By.id("nav-message-popover-container")).getAttribute("data-userid"));
    }

    private String getUserEmail(WebDriver driver, long userMoodleId) {
        driver.navigate().to(
                "https://aulaglobal.uc3m.es/user/profile.php?id=" + userMoodleId
        );

        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until((driver1) -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        return driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/section[1]/div/div/div/div/div/section[1]/ul/li[2]/dl/dd/a")).getText();
    }
}
