package org.uc3m.covid.virtual.scraper;

import org.springframework.stereotype.Component;
import org.uc3m.covid.virtual.entity.Subject;
import org.uc3m.covid.virtual.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserDataScraper implements Scraper<User> {


    @Override
    public User scrapeData() throws IOException {
        return null;
    }



    @Override
    public void saveData() {

    }
}
