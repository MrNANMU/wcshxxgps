package gpsutils.wcshxx.com.gps.ui.home.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.view.BaseDialog;
import gpsutils.wcshxx.com.gps.utils.FileUtils;
import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;

public class RenameDialog extends BaseDialog implements View.OnClickListener{

    private EditText et_rename;
    private TextView tv_cancel,
            tv_commit,
            tv_old_name;
    private String filePath;
    private RenameListener listener;

    public RenameDialog(@NonNull Context context,String filePath) {
        super(context);
        this.filePath = filePath;
        initData();
    }

    public RenameDialog(@NonNull Context context, int themeResId,String filePath) {
        super(context, themeResId);
        this.filePath = filePath;
        initData();
    }

    protected RenameDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener,String filePath) {
        super(context, cancelable, cancelListener);
        this.filePath = filePath;
        initData();
    }

    @Override
    protected int getResourceId() {
        return R.layout.dialog_rename;
    }

    @Override
    protected void initView() {
        et_rename = $(R.id.et_rename);
        tv_cancel = $(R.id.tv_cancel);
        tv_commit = $(R.id.tv_commit);
        tv_old_name = $(R.id.tv_old_name);
        setListeners();
    }

    private void setListeners(){
        tv_cancel.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        LogUtils.e("Dialog.filePath = "+filePath);
        if(!TextUtils.isEmpty(filePath)){
            String[] paths = filePath.split(File.separator);
            String fileName = paths[paths.length-1];
            tv_old_name.setText(fileName);
            et_rename.setHint(fileName);
        }
    }

    @Override
    public void reset() {

    }

    public void setRenameListener(RenameListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_commit:
                String newName = et_rename.getText().toString();
                if(TextUtils.isEmpty(newName)){
                    ToastUtils.show("名称不能为空");
                    return;
                }
                if(FileUtils.rename(filePath,newName)){
                    ToastUtils.show("更改成功");
                    listener.renameSuccess(FileUtils.getBasePath()+newName);
                }else{
                    ToastUtils.show("更改失败");
                    listener.renameFail();
                }
                dismiss();
                break;
        }
    }

    public interface RenameListener{
        void renameSuccess(String newPath);
        void renameFail();
    }
}
