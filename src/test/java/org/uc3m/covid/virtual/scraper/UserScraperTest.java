package org.uc3m.covid.virtual.scraper;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class UserScraperTest {

    @Autowired
    UserDataScraper userDataScraper;

    @Test
    public void scrapeData() throws IOException {
        this.userDataScraper.scrapeData();
    }

}