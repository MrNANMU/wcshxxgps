package gpsutils.wcshxx.com.gps.utils;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import gpsutils.wcshxx.com.gps.base.context.App;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class FileUtils {

    private static final String BASE_PATH = App.getApplication().getFilesDir().getAbsolutePath()+File.separator;

    /**
     * 更改文件名
     * @param file 要更改的文件
     * @param name 要更改的名字
     * @return 是否更改成功
     */
    public static boolean rename(File file,String name){
        if(file != null && file.exists()){
            String path = file.getAbsolutePath();
            int pos = path.lastIndexOf(File.separator);
            String base = path.substring(0,pos+1);
            return file.renameTo(new File(base + name));
        }
        return false;
    }

    /**
     * 更改文件名
     * @param filePath 要更改的文件路径
     * @param name 要更改的名字
     * @return 是否更改成功
     */
    public static boolean rename(String filePath,String name){
        return rename(new File(filePath),name);
    }

    public static File create(String fileName,boolean saveCreateTime){
        if(fileName == null || "".equals(fileName)){
            throw new NullPointerException("文件名不能为空");
        }
        File file = new File(BASE_PATH+fileName);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            if(saveCreateTime){
                FileWriter writer = new FileWriter(file);
                writer.write(String.valueOf(System.currentTimeMillis())+"\n");
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File open(String fileName){
        if(TextUtils.isEmpty(fileName)){
            throw new NullPointerException("File Name cannot be null");
        }
        File file = new File(BASE_PATH+fileName);
        if(file.exists()){
            return file;
        }
        return null;
    }

    public static String getBasePath(){
        return BASE_PATH;
    }

    /**
     * 判断一个文件是否有内容
     * @param file
     * @param hasTimeTag 传入true则跳过第一行，即使第一行不是时间戳
     * @return 是否有内容
     */
    public static boolean isEmpty(File file,boolean hasTimeTag){
        if(file != null && file.exists() && file.isFile()){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                if(hasTimeTag && reader.ready()){
                    reader.readLine();
                }
                return !reader.ready();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    public static boolean isEmpty(String path,boolean hasTimeTag){
        return isEmpty(new File(path),hasTimeTag);
    }

    /*public static synchronized void write(String content){

    }*/

    public static class WriteHelper{

        private static File currentFile;
        //private static boolean mAppend;

        private WriteHelper(){}

        public static WriteHelper with(String fileName){
            currentFile = FileUtils.open(fileName);
            if(currentFile == null){
                throw new NullPointerException("file not exite");
            }
            return new WriteHelper();
        }

        public static WriteHelper with(File file){
            currentFile = file;
            if(currentFile == null){
                throw new NullPointerException("file not exite");
            }
            return new WriteHelper();
        }

        public void close(){
            currentFile = null;
        }

        public WriteHelper appendAsync(final String txt){
            try {
                final FileWriter writer = new FileWriter(currentFile,true);
                Observable.create(new Observable.OnSubscribe<String>(){

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(txt);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                try {
                                    writer.flush();
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {
                                try {
                                    writer.write(txt);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        public WriteHelper append(String txt){
            try {
                FileWriter writer = new FileWriter(currentFile,true);
                writer.write(txt);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        public WriteHelper write(String txt){
            try {
                FileWriter writer = new FileWriter(currentFile,false);
                writer.write(txt);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        public WriteHelper writeAsync(final String txt){
            try {
                final FileWriter writer = new FileWriter(currentFile,false);
                Observable.create(new Observable.OnSubscribe<String>(){

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(txt);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                try {
                                    writer.flush();
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {
                                try {
                                    writer.write(txt);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this;
        }

    }
}
