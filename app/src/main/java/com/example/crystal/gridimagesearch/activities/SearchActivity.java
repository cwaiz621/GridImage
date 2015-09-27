package com.example.crystal.gridimagesearch.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.crystal.gridimagesearch.adapter.ImageResultsAdapater;
import com.example.crystal.gridimagesearch.models.FilterModel;
import com.example.crystal.gridimagesearch.models.ImageResult;
import com.example.crystal.gridimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Filter;

import cz.msebera.android.httpclient.Header;


public class SearchActivity extends ActionBarActivity {

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageresults;
    private ImageResultsAdapater aImageResults;
    private FilterModel Filters;
    private String sizeUrl;
    private String colorUrl;
    private String typeUrl;
    private String siteUrl;
    private String Varquery;
    private boolean newSearch=true;


    public static final int REQUEST_FILTER_CODE=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageresults = new ArrayList<ImageResult>();
        //attach image source to adapter
        aImageResults=new ImageResultsAdapater(this, imageresults);
        //link adapter to grid view
        gvResults.setAdapter(aImageResults);
        Filters = new FilterModel();
        sizeUrl="";
        colorUrl="";
        typeUrl="";
        siteUrl="";

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                LoadData(page-1, Varquery, false);
            }
        });

    }

    private void setupViews(){
        etQuery= (EditText)findViewById(R.id.etQuery);
        gvResults=(GridView) findViewById(R.id.gvResults);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch image display activity
                //1. create intent (activity context, new activtiy you want to launch)
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);

                //2. get image result to dispay
                ImageResult result = imageresults.get(position);
                //3. pass image result into the intent
                i.putExtra("result", result);
                //4. launch new activity
                startActivity(i);
            }
        });

    }

    public void LoadData(int page, String query, boolean new_search){
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        AsyncHttpClient client = new AsyncHttpClient();
        //new query string
        int offeset = 8*page;

        String searchUrl= "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+
                query+ "&rsz=8"+sizeUrl+colorUrl+typeUrl+siteUrl+"&start="+Integer.toString(offeset);

        if ( new_search ){
            aImageResults.clear();
            newSearch=false;
        }

        client.get(searchUrl,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        JSONArray imageResultsJson = null;

                        try {

                            imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                            //adding the items all into the adpater. Making changes to adapter, changes the underlying source
                            aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson)); // calls the ImageResult object to parse the items


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Info", imageresults.toString());
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //newSearch = true;
                Varquery=query;
                LoadData(0, query, true);
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
        if (id == R.id.mnu_filter) {
//            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            //1.Navigate to filter activity
            //create intent
            Intent i = new Intent(this, filter.class);
            //bundle
            i.putExtra("filter", Filters);


            //run start activity method
            startActivityForResult(i, REQUEST_FILTER_CODE);

                //2. pass current value
        }


        return super.onOptionsItemSelected(item);
    }
    //3. get the form data. Handle the form data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Get result out of form
        if(REQUEST_FILTER_CODE == requestCode){
            if(resultCode==RESULT_OK){
                //need to cast
                Filters = (FilterModel) data.getSerializableExtra("filter");
                sizeUrl="";
                colorUrl="";
                typeUrl = "";
                sizeFilter();
                colorFilter();
                typeFilter();

                if(Filters.site.isEmpty()){
                    siteUrl="";

                }else{
                    siteUrl="&as_sitesearch"+Filters.site;
                }


                Toast.makeText(this, typeUrl, Toast.LENGTH_SHORT).show();
            }

        }

    }

    //filter Size
    public void sizeFilter(){
        if (Filters.size.equals("any")){
            sizeUrl= "";
        }else{
            sizeUrl= "&imgsz="+Filters.size;
        }
    }

    //filter color
    public void colorFilter(){
        if (Filters.color.equals("any")){
            colorUrl= "";
        }else{
            colorUrl = "&imgcolor="+Filters.color;
        }
    }

    //filter type

    public void typeFilter(){
        if (Filters.type.equals("any")){
            typeUrl= "";
        }else{
            typeUrl = "&imgtype="+Filters.type;
        }
    }

    //upon clicking Search button
    public void onImageSearch(View view) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this, query,Toast.LENGTH_SHORT).show();
        //https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        LoadData(0, query, true);


    }

}
