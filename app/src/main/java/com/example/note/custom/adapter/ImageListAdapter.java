package com.example.note.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.utils.FileUtil;

import java.io.File;
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

    public ArrayList<String> getData(){
        return mListPics;
    }

    public void setData(ArrayList<String> list){
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
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_grid_picture, null);
        }

        String path = mListPics.get(position);
        TextView tvposition = (TextView) convertView.findViewById(R.id.tv_position);
        tvposition.setText(position+"");
        ImageView imgView = (ImageView) convertView.findViewById(R.id.iv_picture);
        if(!mListPics.isEmpty()) {
            if (FileUtil.isFileExists(path) && !path.equals("")) {
                imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                //can chỉnh lại hình cho đẹp
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                final int REQUIRED_SIZE = 100;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                imgView.setImageBitmap(BitmapFactory.decodeFile(path, options));
            } else {
                imgView.setImageResource(R.drawable.ic_image);
                imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
        return convertView;
    }
}
