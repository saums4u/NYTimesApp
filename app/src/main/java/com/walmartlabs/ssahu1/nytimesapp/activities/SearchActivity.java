package com.walmartlabs.ssahu1.nytimesapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walmartlabs.ssahu1.nytimesapp.AdvancedSearchDialog;
import com.walmartlabs.ssahu1.nytimesapp.Article;
import com.walmartlabs.ssahu1.nytimesapp.ArticleArrayAdapter;
import com.walmartlabs.ssahu1.nytimesapp.DatePickerFragment;
import com.walmartlabs.ssahu1.nytimesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    Calendar date = Calendar.getInstance();
    AdvancedSearchDialog searchDialog = AdvancedSearchDialog.newInstance("Filter Setting");
    private String searchCriteria;
    private String searchOrder;
    private String beginDateString;
    private String query;

    String dateText;
    public int pageNo = 0;
    public RequestParams params;

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String month = Integer.toString(monthOfYear), day = Integer.toString(dayOfMonth);

        // append 0
        if(monthOfYear < 10){
            month = "0"+monthOfYear;
        }
        if(dayOfMonth < 10){
            day = "0"+dayOfMonth;
        }

        dateText = month+"/"+day+"/"+year;
//        editText.setText(dateText, TextView.BufferType.EDITABLE);
        beginDateString = Integer.toString(year)+month+month;
//        Log.d("DEBUG",date.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        searchDialog.show(fm, "advanced_search");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
//        Log.d("DEBUG",menu.toString());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle when settings is selected

        switch (item.getItemId()){
            case R.id.miSetting:
                //Toast.makeText(this, "menu is tapped", Toast.LENGTH_SHORT).show();
                showEditDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(View view, int pageNo) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        getArticles(view, pageNo);

    }

    public void setupViews(){
        etQuery = (EditText)findViewById(R.id.etQuery);

        gvResults = (GridView) findViewById(R.id.gvResults);

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(View view, int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(view, page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }

            @Override
            public int getFooterViewType() {
                return 0;
            }
        });

//        gvResults = (GridView) findViewById(R.id.gvResults);
//        etQuery = (EditText)findViewById(R.id.etQuery);

        // hook up the listener for grid click

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);

                //get the article to display
                Article article = articles.get(position);

                //pass on the article to intent
                i.putExtra("article", article);

                //launch the activity
                startActivity(i);
            }
        });
    }

    public void getArticles(View view, int pageNo){


        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("api-key", "4e9f7298f8694c05bdcaf010dafec327");
        params.put("q", query);
        params.put("fq", searchCriteria);
        params.put("begin_date", beginDateString);
        params.put("sort", searchOrder);

        params.put("page",pageNo);

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");

                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    Log.d("DEBUG", articles.toString());
                }catch ( JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }
    public void clear(){
        this.pageNo = 0;
        this.query = null;
        this.searchCriteria = null;
        this.beginDateString = null;
        this.searchOrder = null;
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
//        Toast.makeText(this, " searching for "+query, Toast.LENGTH_SHORT).show();
        // clear the adapter
        adapter.clear();
        this.clear();
        this.query = query;

        // 20 items per screen.
        for(int i=0;i<2;i++) {
            getArticles(view, i);
        }
    }

    public void onSubmit(View view) {
//        Toast.makeText(this, "on submit", Toast.LENGTH_SHORT).show();
        searchDialog.dismiss();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_arts:
                if (checked)
                    setSearchCriteria("Arts");
                    break;
            case R.id.radio_fashion:
                if (checked)
                    setSearchCriteria("Fashion");
                    break;
            case R.id.radio_sports:
                if (checked)
                    setSearchCriteria("Sports");
                    break;
            case R.id.radio_older:
                if (checked)
                    setSearchOrder("oldest");
                    break;
            case R.id.radio_newer:
                if (checked)
                    setSearchOrder("newest");
                    break;

        }
    }

    private void setSearchCriteria(String criteria) {
        searchCriteria = criteria;
    }

    private void setSearchOrder(String order) {
        searchOrder = order;
    }
}

