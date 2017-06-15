package com.judd.trump.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王元_Trump
 * @desc 文件管理类
 * @time 2016/10/7 13:31
 */
public class FileUtil {

    /**
     * 指定路径下的所有目录
     */
    public static ArrayList<File> allFileDirectory = new ArrayList<File>();

    /**
     * 指定路径下的所有文件
     */
    public static ArrayList<File> allFile = new ArrayList<File>();

    public static boolean isExistFile(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    /**
     * 判断 sd 卡是否可用
     *
     * @return
     */
    public static boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取 文件，根据文件夹路径，文件名。 </br> 首先判断 文件夹是否存在，然后在 返回 文件File </br> 如果 sdk不可用 则 返回空
     *
     * @param filePath 文件夹路径,
     * @param fileName 文件名
     * @return
     */
    public static File getFile(String filePath, String fileName) {
        if (ExistSDCard()) {
            File fileDir = new File(filePath);
            if (!isExistFile(filePath)) {
                fileDir.mkdirs();
            }
            return new File(filePath, fileName);
        } else {
            return null;
        }
    }

    /**
     * 创建 文件夹
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        if (ExistSDCard()) {
            File fileDir = new File(filePath);
            if (!isExistFile(filePath)) {
                fileDir.mkdirs();
            }
            return isExistFile(filePath);
        }
        return false;
    }

    /**
     * 获取文件格式 ，同 (see {@link #getFile(String filePath, String fileName)}
     *
     * @param filePath
     * @return
     */
    public static File getFile(String filePath) {
        if (ExistSDCard())
            return new File(filePath);
        else
            return null;
    }

    @SuppressWarnings("resource")
    public static void saveBinaryFile(String path, String name, byte[] buffer) throws IOException {
        // if(FileUtil.isSDCARDMounted()){
        // baseDirectory = new File(Environment.getExternalStorageDirectory(),
        // dirPath);
        // }else{
        // baseDirectory = new File(dirPath);
        // }
        File fileDir = new File(path);
        if (!isExistFile(path)) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir, name);
        FileOutputStream fos;
        fos = new FileOutputStream(file);
        fos.write(buffer);
        fos.flush();
        fos.close();
    }

    /**
     * <追加在 文件后面，，>
     *
     * @param path
     * @param name
     * @param buffer
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static void saveBinaryFile_append(String path, String name, byte[] buffer) throws IOException {
        // if(FileUtil.isSDCARDMounted()){
        // baseDirectory = new File(Environment.getExternalStorageDirectory(),
        // dirPath);
        // }else{
        // baseDirectory = new File(dirPath);
        // }

        File fileDir = new File(path);
        if (!isExistFile(path)) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir, name);
        FileOutputStream fos;
        fos = new FileOutputStream(file, true);
        fos.write(buffer);
        fos.flush();
        fos.close();
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    /*
     * 通过递归得到某一路径下所有的目录及其文件
     */
    public static void getFiles(String filePath) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                allFileDirectory.add(file);
                /*
				 * 递归调用
				 */
                getFiles(file.getAbsolutePath());
                System.out.println("显示" + filePath + "下所有子目录及其文件" + file.getAbsolutePath());
            } else {
                allFile.add(file);
                System.out.println("显示" + filePath + "下所有子目录" + file.getAbsolutePath());
            }
        }
    }

    /*
     * 通过递归得到某一路径下所有的目录及其文件
     */
    public static List<File> getFileList(String filePath) {
        List<File> fileList = new ArrayList<File>();
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 写数据 缓存数据到 读写/data/data/目录上的文件:
     *
     * @param context
     * @param fileName
     * @param writestr
     */
    public static void saveCacheFile(Context context, String fileName, String writestr) {
        try {
            FileOutputStream fout = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] bytes = writestr.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存文件是否存在
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean isCacheExist(Context context, String fileName) {
        return context.getDir(fileName, Context.MODE_PRIVATE).exists();
    }

    /**
     * 重命名
     *
     * @param oldFilaName
     * @param newFilaName
     */
    public static void renameFile(String oldFilaName, String newFilaName) {
        File oleFile = new File(oldFilaName); // 要重命名的文件或文件夹
        File newFile = new File(newFilaName); // 重命名为zhidian1
        oleFile.renameTo(newFile); // 执行重命名
    }

    /**
     * 获取 文件大小
     *
     * @param file
     * @return
     */
    public static long getTotalSizeOfFilesInDir(File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }

}
