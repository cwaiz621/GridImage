package com.example.crystal.gridimagesearch.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crystal.gridimagesearch.R;
import com.example.crystal.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Crystal on 9/24/15.
 */
public class ImageResultsAdapater extends ArrayAdapter<ImageResult> {

    public ImageResultsAdapater(Context context, List<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageinfo = getItem(position);

        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_image_results,parent,false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        //clear out image from last time
        ivImage.setImageResource(0);
        //populate title
        tvTitle.setText(Html.fromHtml(imageinfo.title));
        //poplate image with picasso
        Picasso.with(getContext()).load(imageinfo.thumbUrl).into(ivImage);

        return convertView;
    }
}
