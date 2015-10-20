package com.arunpn.imagesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

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


public class MainActivity extends AppCompatActivity {

    ApiService apiService;
    @Bind(R.id.gridView)
    GridView gridView;
    SearchAdapter adapter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    public static String searchQuery = "";

    String colorChoice = "";
    String imageSizeChoice = "";
    String siteFilter = "";
    ArrayAdapter<CharSequence> adColorization;
    ArrayAdapter<CharSequence> adSize;
    public static final String API_VERSION = "1.0";
    public static final int RESULT_SIZE = 8;
    public static  String IMAGE_SIZE = "";
    public static  String SEARCH_SITE = "";
    public static  String IMAGE_COLOR = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolBar();
        storeSharedPrefs("","","");
        adapter = new SearchAdapter(this, new ArrayList<ImageDetails>());
        apiService = new RestClient().getApiService();
        gridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                getMorePhotos(page);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageDetails imageDetails = (ImageDetails) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this,ImageZoom.class);
                intent.putExtra("url",imageDetails.getUrl());
                startActivity(intent);
            }
        });


        getPhotos(0);
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Image Search");
        toolbar.setTitleTextColor(getResources().getColor(R.color.title_bar_text_color));
    }

    private void getPhotos(int page) {

        int searchPage = page * RESULT_SIZE;
        IMAGE_SIZE = returnSharedPref("imageSizeChoice");
        SEARCH_SITE = returnSharedPref("siteFilter");
        IMAGE_COLOR = returnSharedPref("colorChoice");


        apiService.getImages(searchQuery, API_VERSION, RESULT_SIZE, searchPage, IMAGE_SIZE, SEARCH_SITE, IMAGE_COLOR, new Callback<ResponseData>() {
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

        int searchPage = page * RESULT_SIZE;
        IMAGE_SIZE = returnSharedPref("imageSizeChoice");
        SEARCH_SITE = returnSharedPref("siteFilter");
        IMAGE_COLOR = returnSharedPref("colorChoice");

        apiService.getImages(searchQuery, API_VERSION, RESULT_SIZE, searchPage, IMAGE_SIZE, SEARCH_SITE, IMAGE_COLOR, new Callback<ResponseData>() {
            @Override
            public void success(ResponseData responseData, Response response) {
                if (responseData != null && responseData.getCursor().getEstimatedResultCount() > 0) {
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                getPhotos(0);
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {


        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.settings_layout, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Advanced Filters").setView(convertView);

        adSize = ArrayAdapter.createFromResource(this,
                R.array.size_choice, android.R.layout.simple_spinner_item);
        adSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adColorization = ArrayAdapter.createFromResource(this,
                R.array.color_choice, android.R.layout.simple_spinner_item);
        adColorization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spSize = (Spinner) convertView.findViewById(R.id.spinnerImageSize);
        spSize.setAdapter(adSize);
        Spinner spColorization = (Spinner) convertView.findViewById(R.id.spinnerColorFilter);
        spColorization.setAdapter(adColorization);
        final EditText siteFilter = (EditText) convertView.findViewById(R.id.siteEditText);
        setDefaultSpinnerValues(spSize, spColorization, siteFilter);



        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                imageSizeChoice = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spColorization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                colorChoice = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                storeSharedPrefs(colorChoice, imageSizeChoice, siteFilter.getText().toString());
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create();
        alertDialog.show();

    }

    public void setDefaultSpinnerValues(Spinner spSize, Spinner spColorization, EditText siteFilter) {
        String value = returnSharedPref("colorChoice");
        if (value != "") {
            spColorization.setSelection(adColorization.getPosition(value));
        }
        value = returnSharedPref("imageSizeChoice");
        if (value != "") {
            spSize.setSelection(adSize.getPosition(value));
        }
        value = returnSharedPref("siteFilter");
        siteFilter.setText(value);
    }


    public void storeSharedPrefs(String colorChoice, String imageSizeChoice, String siteFilter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("colorChoice", colorChoice);
        editor.putString("imageSizeChoice", imageSizeChoice);
        editor.putString("siteFilter", siteFilter);
        editor.apply();
    }

    public String returnSharedPref(String choice) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(choice, "");
    }
}
