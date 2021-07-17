package org.uc3m.covid.virtual.scraper;

import java.io.IOException;

public interface Scraper<T> {
    T scrapeData() throws IOException;
    void saveData();
}
