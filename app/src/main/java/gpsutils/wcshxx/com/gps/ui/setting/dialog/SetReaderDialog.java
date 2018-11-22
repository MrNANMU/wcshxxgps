package gpsutils.wcshxx.com.gps.ui.setting.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.view.BaseDialog;
import gpsutils.wcshxx.com.gps.config.Config;

public class SetReaderDialog extends BaseDialog implements View.OnClickListener{

    private TextView tv_open_by_gps,
            tv_open_by_system,
            tv_open_by_user,
            v;

    public SetReaderDialog(@NonNull Context context){
        super(context);
    }

    public SetReaderDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SetReaderDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getResourceId() {
        return R.layout.dialog_set_reader;
    }

    @Override
    protected void initView() {
        tv_open_by_gps = $(R.id.tv_open_by_gps);
        tv_open_by_system = $(R.id.tv_open_by_system);
        tv_open_by_user = $(R.id.tv_open_by_user);
        setListeners();
    }

    private void setListeners(){
        tv_open_by_gps.setOnClickListener(this);
        tv_open_by_system.setOnClickListener(this);
        tv_open_by_user.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void reset() {

    }

    public void bindTextView(TextView v){
        this.v = v;
    }

    @Override
    public void onClick(View v) {
        String showStr = tv_open_by_user.getText().toString();;
        switch (v.getId()){
            case R.id.tv_open_by_gps:
                showStr = "GPS记录仪";
                Config.setReader(Config.Reader.APP);
                break;
            case R.id.tv_open_by_system:
                showStr = "系统文件浏览器";
                Config.setReader(Config.Reader.SYSTEM);
                break;
            case R.id.tv_open_by_user:
                Config.setReader(Config.Reader.NOT_SELECT);
                break;
        }
        this.v.setText(showStr);
        dismiss();
    }
}
