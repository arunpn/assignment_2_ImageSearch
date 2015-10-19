package com.arunpn.imagesearch.model;

/**
 * Created by a1nagar on 10/18/15.
 */
public class SearchInfo {
    int estimatedResultCount;
    int currentPageIndex;

    public int getEstimatedResultCount() {
        return estimatedResultCount;
    }

    public void setEstimatedResultCount(int estimatedResultCount) {
        this.estimatedResultCount = estimatedResultCount;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }
}
