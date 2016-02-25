package com.example.note.utils;

import android.util.Log;

import com.example.note.config.Define;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
}
