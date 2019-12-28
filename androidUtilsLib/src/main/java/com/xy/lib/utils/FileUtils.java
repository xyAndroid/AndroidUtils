package com.xy.lib.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    /**
     * Bitmap保存到本地
     * @param bitmap
     * @param localFile
     * @return
     */
    public static File saveBitmapToLocal(Bitmap bitmap, String localFile) {
        File localPathFile = new File(localFile);
        if (localPathFile.exists()) {
            localPathFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(localFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return localPathFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
