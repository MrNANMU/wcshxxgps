package gpsutils.wcshxx.com.gps.ui.history.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.BaseActivity;
import gpsutils.wcshxx.com.gps.utils.DateUtils;

public class HistoryActivity extends BaseActivity{

    private TextView tv_file_path,
            tv_file_name,
            tv_file_create_time,
            tv_infos;

    private String filePath;
    private File file;
    private String content;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_infos = $(R.id.tv_infos);
        tv_file_path = $(R.id.tv_file_path);
        tv_file_name = $(R.id.tv_file_name);
        tv_file_create_time = $(R.id.tv_file_create_time);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        filePath = getIntent().getStringExtra("file_path");
        if(!TextUtils.isEmpty(filePath)){
            tv_file_path.setText(filePath);
            file = new File(filePath);
            if(file.exists() && file.isFile()){
                tv_file_name.setText(file.getName());
                try {
                    Scanner scanner = new Scanner(file);
                    StringBuffer buffer = new StringBuffer();
                    boolean skip = true;
                    while (scanner.hasNext()){
                        if(skip){
                            skip = false;
                            long time = Long.valueOf(scanner.nextLine());
                            tv_file_create_time.setText(DateUtils.longToString(time,"yyyy-MM-dd HH:mm:ss"));
                        }else{
                            buffer.append(scanner.nextLine()).append("\n");
                        }
                    }
                    scanner.close();
                    content = buffer.toString();
                    tv_infos.setText(content);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
