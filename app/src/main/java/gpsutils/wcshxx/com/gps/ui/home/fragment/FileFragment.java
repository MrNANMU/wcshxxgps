package gpsutils.wcshxx.com.gps.ui.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.callback.NewGPSFileCreateListener;
import gpsutils.wcshxx.com.gps.base.callback.OnItemClickedListener;
import gpsutils.wcshxx.com.gps.base.view.BaseFragment;
import gpsutils.wcshxx.com.gps.config.Config;
import gpsutils.wcshxx.com.gps.ui.history.activity.HistoryActivity;
import gpsutils.wcshxx.com.gps.ui.home.adapter.FileAdapter;
import gpsutils.wcshxx.com.gps.ui.home.dialog.OpenSetDialog;
import gpsutils.wcshxx.com.gps.ui.home.dialog.RenameDialog;
import gpsutils.wcshxx.com.gps.utils.DialogHelper;
import gpsutils.wcshxx.com.gps.utils.FileUtils;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;

public class FileFragment extends BaseFragment implements OnItemClickedListener,NewGPSFileCreateListener{

    private RecyclerView rv_files;
    private FileAdapter adapter;
    private LinearLayoutManager manager;
    private File base;
    private File[] files;
    private List<String> list;

    public static FileFragment create(){
        return new FileFragment();
    }

    public static FileFragment create(Bundle bundle){
        FileFragment fragment = new FileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getResource() {
        return R.layout.fragment_files;
    }

    @Override
    public void getParameter(Bundle bundle) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        list = new ArrayList<>();
        rv_files = $(R.id.rv_files);
        adapter = new FileAdapter(context,list);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_files.setLayoutManager(manager);
        rv_files.setAdapter(adapter);
        adapter.setOnItemClickedListener(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        /*File file = FileUtils.create("测试文件",true);
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            for(int i=0; i<100; i++){
                fileWriter.write("测试内容"+i+"\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        getFilesPath();

    }

    private void getFilesPath(){
        base = new File(FileUtils.getBasePath());
        if(base.exists() && base.isDirectory()){
            files = base.listFiles();
            if(files != null && files.length > 0){
                for(File f:files){
                    if(f.exists() && f.isFile()){
                        list.add(f.getAbsolutePath());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(final int position, View view) {
        switch (view.getId()){
            case R.id.tv_rename:
                RenameDialog renameDialog = new RenameDialog(context,list.get(position));
                renameDialog.addToManager();
                renameDialog.setRenameListener(new RenameDialog.RenameListener() {
                    @Override
                    public void renameSuccess(String newPath) {
                        Collections.replaceAll(list,list.get(position),newPath);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void renameFail() {

                    }
                });
                renameDialog.show();
                break;
            case R.id.tv_delete:
                File file = new File(list.get(position));
                if(file.exists()){
                    if(file.delete()){
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        ToastUtils.show("删除成功");
                    }
                }
                break;
            case R.id.tv_share:
                ToastUtils.show("开发中");
                break;
            case R.id.tv_load:
            default:
                switch (Config.getReader()){
                    case Config.Reader.APP:
                        Intent intent1 = new Intent(context, HistoryActivity.class);
                        intent1.putExtra("file_path",list.get(position));
                        startActivity(intent1);
                        break;
                    case Config.Reader.SYSTEM:
                        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                        //设置类型，我这里是任意类型，任意后缀的可以这样写。
                        intent2.setType("*/*");
                        intent2.addCategory(Intent.CATEGORY_OPENABLE);
                        getActivity().startActivityForResult(intent2,1);
                        break;
                    case Config.Reader.NOT_SELECT:
                        OpenSetDialog openSetDialog = new OpenSetDialog(context);
                        openSetDialog.addToManager();
                        openSetDialog.setOpenPath(list.get(position));
                        DialogHelper.fromBottom(openSetDialog);
                        openSetDialog.show();
                        break;
                }
                break;
        }
    }

    @Override
    public void newFileCreate(String path) {
        list.add(path);
        adapter.notifyDataSetChanged();
    }
}
