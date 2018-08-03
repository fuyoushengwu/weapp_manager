package cn.aijiamuyingfang.weapp.manager.commons.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public final class FileUtils {
    private FileUtils() {
    }

    private static final String TAG = FileUtils.class.getName();
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * 创建文件夹
     *
     * @param directory
     */
    public static boolean createDirectory(File directory) {
        return directory != null && (directory.exists() || directory.mkdir());
    }

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    public static boolean createFile(File file) {
        if (null == file) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        if (createDirectory(file.getParentFile())) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static void saveBitmap(Bitmap bitmap, File targetFile) {
        if (null == bitmap || null == targetFile) {
            return;
        }

        try (OutputStream outputStream = new FileOutputStream(targetFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            Log.e(TAG, "save bitmap failed", e);
        }
    }

    /**
     * 清理文件夹
     *
     * @param directory
     * @throws IOException
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception)
            throw exception;
    }

    /**
     * 强制删除文件
     *
     * @param file
     * @throws IOException
     */
    private static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message = "Unable to delete file: " + file;

                throw new IOException(message);
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param directory
     * @throws IOException
     */
    private static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";

            throw new IOException(message);
        }
    }

    /**
     * 是否是符号链接文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (isSystemWindows()) {
            return false;
        }
        File fileInCanonicalDir;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

    /**
     * 是否是Windows操作系统
     *
     * @return
     */
    private static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }
}