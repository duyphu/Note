package com.example.note.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.note.config.Define;
import com.example.note.custom.adapter.ImageListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by phund on 2/25/2016.
 */
public class FileUtil {
    public static void copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException fe){
            fe.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    public static void createFolder(String folderPath){
        File f = new File(folderPath);
        if (!f.exists()) {
            f.mkdirs();
            f.setWritable(true);
        }
    }

    public static boolean isFileExists(String pathFile){
        File f = new File(pathFile);
        if(f.exists()) return true;
        else return false;
    }

    public static String copyPictureToNoteFolder(Intent data, Context context){
        String newPath = "";
        try {
            FileUtil.createFolder(Define.PICTURE_NOTE_FOLDER);

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            // copy file to picture note folder
            String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
            newPath = Define.PICTURE_NOTE_FOLDER + "/" + fileName;
            FileUtil.copy(new File(picturePath), new File(newPath));
            cursor.close();
        } catch (NullPointerException ne){
            ne.printStackTrace();
        } finally {
            return newPath;
        }
    }
}
