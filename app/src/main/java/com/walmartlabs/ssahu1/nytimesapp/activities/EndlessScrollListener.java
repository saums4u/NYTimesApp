package com.walmartlabs.ssahu1.nytimesapp.activities;

/**
 * Created by ssahu6 on 5/31/16.
 */

import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

public abstract class EndlessScrollListener implements GridView.OnScrollListener {
    // The minimum number of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {

        loading = onLoadMore(view, currentPage++, totalItemCount);
    }

    // Defines the process for actually loading more data based on page
    // Returns true if more data is being loaded; returns false if there is no more data to load.
    public abstract boolean onLoadMore(View view, int page, int totalItemsCount);

    // set FooterView type
    // if don't use footview loadmore  set: -1
    public abstract int getFooterViewType();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}