package com.example.crystal.gridimagesearch.activities;

import android.widget.AbsListView;

/**
 * Created by Crystal on 9/26/15.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 3; // minimum number of items to have below your current scroll position before loading more
    private int currentPage = 0; // current offset index of data you have loaded
    private int previousTotalItemCount = 0; // total number of items in the data set after the last load
    private boolean stillLoading = true;
    private int startingPageIndex = 0;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this(visibleThreshold);
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // if total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if ( totalItemCount < previousTotalItemCount ) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if ( 0 == totalItemCount ) { this.stillLoading = true; }
        }

        // once data set count has changed, it has finished loading
        // update the current page number and total item count
        if ( stillLoading && totalItemCount > previousTotalItemCount ) {
            stillLoading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // need to load more data if visible threshold is breached
        if ( !stillLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) ) {
            // fetch the data
            onLoadMore(currentPage + 1, totalItemCount);
            stillLoading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // empty
    }

    /**
     * Method for loading more data based on page
     */
    public abstract void onLoadMore(int page, int totalItemsCount);
}