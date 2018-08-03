package cn.aijiamuyingfang.weapp.manager.commons.utils;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pc on 2018/4/8.
 */

public final class IOUtils {
    private IOUtils() {
    }

    private static final String TAG = IOUtils.class.getName();

    /**
     * @param filePath
     * @param data
     * @throws IOException
     */
    public static void write(String filePath, byte[] data) throws IOException {
        File targetFile = new File(filePath);
        boolean parentExist = targetFile.getParentFile().exists();
        if (!parentExist) {
            parentExist = targetFile.getParentFile().mkdirs();
        }
        if (!parentExist) {
            return;
        }

        boolean fileExist = targetFile.exists();
        if (!fileExist) {
            fileExist = targetFile.createNewFile();
        }
        if (!fileExist) {
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(data);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException ioe) {
            //NO OPERATION
        }
    }

}
