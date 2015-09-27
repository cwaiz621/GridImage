package com.example.crystal.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Crystal on 9/24/15.
 */
public class ImageResult implements Serializable{

    public static final long serialVersionUID=12345L;
    public String fullUrl;
    public String thumbUrl;
    public String title;
    public int height;
    public int width;


    //new image result from raw JSON object
    public ImageResult(JSONObject object){
        try{
            this.fullUrl=object.getString("url");
            this.thumbUrl=object.getString("tbUrl");
            this.title=object.getString("title");
            this.height=object.getInt("height");
            this.width=object.getInt("width");

        }catch(JSONException e){

        }

    }

    //Take an array of JSON results and get an array of image results
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i=0; i<array.length();i++){
            try{
                results.add(new ImageResult(array.getJSONObject(i)));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        return results;
    }

}
