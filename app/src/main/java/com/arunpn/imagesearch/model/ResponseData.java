package com.arunpn.imagesearch.model;

import java.util.List;

/**
 * Created by a1nagar on 10/18/15.
 */
public class ResponseData {
    List<ImageDetails>  results;
    SearchInfo cursor;

    public List<ImageDetails> getResults() {
        return results;
    }

    public void setResults(List<ImageDetails> results) {
        this.results = results;
    }

    public SearchInfo getCursor() {
        return cursor;
    }

    public void setCursor(SearchInfo cursor) {
        this.cursor = cursor;
    }
}
