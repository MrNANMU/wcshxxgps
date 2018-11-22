package gpsutils.wcshxx.com.gps.base.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

abstract public class BaseFragment extends Fragment {

    protected Context context;
    protected View root;
    protected Bundle bundle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化view
        root = inflater.inflate(getResource(),container,false);
        bundle = getArguments();
        getParameter(bundle);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
        //初始化数据
        initData(savedInstanceState);
    }

    protected abstract int getResource();

    public abstract void getParameter(Bundle bundle);

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initData(Bundle savedInstanceState);

    public <V extends View> V $(int id){
        return root.findViewById(id);
    }

}
