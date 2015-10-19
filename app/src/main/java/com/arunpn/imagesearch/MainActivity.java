package com.arunpn.imagesearch;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.arunpn.imagesearch.adapter.SearchAdapter;
import com.arunpn.imagesearch.model.ImageDetails;
import com.arunpn.imagesearch.model.ResponseData;
import com.arunpn.imagesearch.rest.ApiService;
import com.arunpn.imagesearch.rest.RestClient;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;


public class MainActivity extends AppCompatActivity {

    ApiService apiService;
    @Bind(R.id.gridView)
    GridView gridView;
    SearchAdapter adapter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    public static  String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolBar();
        adapter = new SearchAdapter(this, new ArrayList<ImageDetails>());
        apiService = new RestClient().getApiService();
        gridView.setOnScrollListener( new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                getMorePhotos(page);
                return true;
            }
        });


        getPhotos("",0);
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Image Search");
        toolbar.setTitleTextColor(getResources().getColor(R.color.title_bar_text_color));
    }

    private void getPhotos(String query,int page) {
        

//        String searchQuery = "sachin tendulkar";
//        String searchQuery = query;
        String apiVersion = "1.0";
        int resultSize = 8;
        int searchPage = page*resultSize;
        String imageSize="xxlarge";
        String searchSite="espncricinfo.com";
        String imageColor="blue";

        apiService.getImages(searchQuery,apiVersion,resultSize,searchPage,imageSize,searchSite,imageColor, new Callback<ResponseData>() {
            @Override
            public void success(ResponseData responseData, Response response) {

               if (responseData.getCursor().getEstimatedResultCount() > 0) {
                   gridView.setAdapter(adapter);
                   adapter.clear();
                   adapter.addAll(responseData.getResults());

               }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void getMorePhotos(int page) {

//        String searchQuery = "sachin tendulkar";
//        String searchQuery = query;
        String apiVersion = "1.0";
        int resultSize = 8;
        int searchPage = page*resultSize;
        String imageSize="xxlarge";
        String searchSite="espncricinfo.com";
        String imageColor="blue";

        apiService.getImages(searchQuery,apiVersion,resultSize,searchPage,imageSize,searchSite,imageColor, new Callback<ResponseData>() {
            @Override
            public void success(ResponseData responseData, Response response) {

                if (responseData!= null && responseData.getCursor().getEstimatedResultCount() > 0) {
                    adapter.addAll(responseData.getResults());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }

        final SearchView finalSearchView = searchView;
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery= query;
                getPhotos(query,0);
                finalSearchView.setQuery("", false);
                finalSearchView.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
