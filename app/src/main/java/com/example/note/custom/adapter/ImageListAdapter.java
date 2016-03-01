package com.example.note.custom.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phund on 2/25/2016.
 */
public class ImageListAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> mListPics;

    public ImageListAdapter(Context context, ArrayList<String> list){
        mContext = context;
        mListPics = list;
    }

    @Override
    public int getCount() {
        if(mListPics != null) return mListPics.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgView;
        if(convertView == null){
            imgView = new ImageView(mContext);
            String path = mListPics.get(position);
            //can chỉnh lại hình cho đẹp
//            imgView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imgView.setScaleType(ImageView.ScaleType.CENTER);
            imgView.setPadding(8, 8, 8, 8);
            imgView.setImageBitmap(BitmapFactory.decodeFile(path));
        } else{
            imgView = (ImageView)convertView;
        }

        return imgView;
    }
}
