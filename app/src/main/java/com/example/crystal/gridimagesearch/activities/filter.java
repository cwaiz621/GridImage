package com.example.crystal.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.crystal.gridimagesearch.R;
import com.example.crystal.gridimagesearch.models.FilterModel;

import java.util.logging.Filter;

public class filter extends ActionBarActivity {

    String FilterSize;
    String spinnerValue;
    Spinner sp_size;
    Spinner sp_color_filter;
    Spinner sp_imagetype;
    EditText sp_siteFilter;
    FilterModel filters;
    ArrayAdapter<CharSequence> adapter_sizeArray;
    ArrayAdapter<CharSequence> adapter_colorArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        sp_size = (Spinner) findViewById(R.id.sp_ImageSize);
        sp_color_filter = (Spinner)findViewById(R.id.sp_colorFilter);
        sp_imagetype = (Spinner)findViewById(R.id.sp_ImageType);
        sp_siteFilter= (EditText)findViewById(R.id.spSiteFilter);


        //array adapter for spinner
        //ArrayAdapter<CharSequence> sp_ImageSizeAdapter = ArrayAdapter.createFromResource(this, R.array.size_array,
                //android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_TypeArray = ArrayAdapter.createFromResource(this, R.array.type_array,
                android.R.layout.simple_spinner_item);
       adapter_sizeArray = ArrayAdapter.createFromResource(this, R.array.size_array,
                android.R.layout.simple_spinner_item);
        adapter_colorArray = ArrayAdapter.createFromResource(this, R.array.color_array,
                android.R.layout.simple_spinner_item);
        //specifying layout of dropdown
        adapter_TypeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_sizeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_colorArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //applying spinner to the adapter
        sp_imagetype.setAdapter(adapter_TypeArray);
        sp_size.setAdapter(adapter_sizeArray);
        sp_color_filter.setAdapter(adapter_colorArray);


        //get the Extra
        filters = (FilterModel) getIntent().getSerializableExtra("filter");
        sp_size.getSelectedItem();
        setSpinnerToValue(adapter_sizeArray, sp_size, filters.size);
        sp_color_filter.getSelectedItem();
        setSpinnerToValue(adapter_colorArray, sp_color_filter, filters.color);
        sp_imagetype.getSelectedItem();
        setSpinnerToValue(adapter_TypeArray, sp_imagetype, filters.type);
        sp_siteFilter.setText(filters.site);


        //sp_size.setOnItemSelectedListener(this);
        //FilterSize=spinnerValue;


    }

    public void setSpinnerToValue(ArrayAdapter<CharSequence>  adapter, Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
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

    public void onSave(View view) {
        //1. Set the form data
        //get value
        //String size =sp_size.getSelectedItem().toString()
        String size = sp_size.getSelectedItem().toString();
        setSpinnerToValue(adapter_sizeArray, sp_size, sp_size.getSelectedItem().toString());
        String color = sp_color_filter.getSelectedItem().toString();
        setSpinnerToValue(adapter_colorArray, sp_color_filter, sp_color_filter.getSelectedItem().toString());
        String type = sp_imagetype.getSelectedItem().toString();
        String site = sp_siteFilter.getText().toString();

        filters.size = size;
        filters.color=color;
        filters.type = type;
        filters.site =site;

        //create an intent
        Intent i = new Intent();
        i.putExtra("filter",filters);

        //set the result
        setResult(RESULT_OK,i);
        //2. dismiss the screen
        finish();
    }

  /*  //what to do when item is selected...save selection...but the selection does not seem to be saving
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                parent.setSelection(position);
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                parent.setSelection(position);
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                parent.setSelection(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "inComplete",Toast.LENGTH_SHORT).show();
    }*/
}
