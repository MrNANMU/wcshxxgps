package gpsutils.wcshxx.com.gps.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.App;
import gpsutils.wcshxx.com.gps.base.context.BaseActivity;
import gpsutils.wcshxx.com.gps.base.view.BaseDialog;

public class PermissionUtils {

    public static final int REQUEST_PERMISSION = 1001;

    /**
     * 此方法直接请求权限
     * @param activity 需要申请权限的Activity
     * @param permissions 请求的权限列表
     * @return 是否请求了权限
     */
    public static boolean request(BaseActivity activity, String[] permissions){
        List<String> list = check(permissions);
        if(list.size() != 0){
            ActivityCompat.requestPermissions(activity, list.toArray(new String[list.size()]), REQUEST_PERMISSION);
            return true;
        }
        if(activity instanceof PermissionListener){
            ((PermissionListener)activity).afterPermissionRequested(false);
        }
        return false;
    }

    /**
     * 此方法会弹出一个对话框，表明需要哪些权限，以及权限的作用
     * @param context 需要申请权限的Context
     * @param permissionDescription Map的key为权限名（非严格，可以自己随便起名），value为权限作用
     * @param permissions 请求的权限列表
     * @return 是否请求了权限
     */
    public static boolean request(Context context,Map<String,String> permissionDescription,String[] permissions){
        if(needRequest(permissions)){
            PermissionDialog dialog = new PermissionDialog(context);
            dialog.setCancelable(false);
            dialog.setPermissionMap(permissionDescription);
            dialog.setRequest(permissions);
            dialog.show();
            return true;
        }
        if(context instanceof PermissionListener){
            ((PermissionListener)context).afterPermissionRequested(false);
        }
        return false;
    }

    /**
     * 返回需要申请的权限列表，过滤掉已经申请过的权限
     * @param permissions
     * @return
     */
    private static List<String> check(String[] permissions){
        List<String> list = new ArrayList<>();
        for(String p:permissions){
            if (ContextCompat.checkSelfPermission(App.getApplication(),p) != PackageManager.PERMISSION_GRANTED){
                //说明没有权限
                list.add(p);
            }
        }
        return list;
    }

    /**
     * 检查是否需要弹窗
     * @param permissions
     * @return
     */
    private static boolean needRequest(String[] permissions){
        for(String p:permissions){
            if (ContextCompat.checkSelfPermission(App.getApplication(),p) != PackageManager.PERMISSION_GRANTED){
                //说明没有权限
                return true;
            }
        }
        return false;
    }

    /**
     * 此Dialog只会在申请权限时弹出，不会在其他时候使用
     */
    static class PermissionDialog extends BaseDialog implements View.OnClickListener{

        TextView tv_commit;
        String[] permissions;
        ViewPager vp_permission;
        PermissionAdapter adapter;

        public PermissionDialog(@NonNull Context context) {
            super(context);
            addToManager();
        }

        @Override
        protected int getResourceId() {
            return R.layout.dialog_permission2;
        }

        @Override
        protected void initView() {
            vp_permission = $(R.id.vp_permission);
            tv_commit = $(R.id.tv_commit);
            tv_commit.setOnClickListener(this);
        }

        @Override
        protected void initData() {

        }

        @Override
        public void reset() {

        }

        @Override
        public void onClick(View v) {
            dismiss();
            if(permissions != null && permissions.length !=0){
                PermissionUtils.request((BaseActivity) context,permissions);
            }
        }

        public void setPermissionMap(Map<String,String> map){
            adapter = new PermissionAdapter(context,map);
            vp_permission.setAdapter(adapter);
        }

        public void setRequest(String[] permissions){
            this.permissions = permissions;
        }
    }

    static class PermissionAdapter extends PagerAdapter{

        private Context context;

        private List<String> permissionName;
        private List<String> permissionDescription;
        private List<View> roots = new ArrayList<>();

        public PermissionAdapter(Context context,Map<String,String> map){
            this.context = context;
            split(map);
        }

        private void split(Map<String,String> map){
            if(map != null && map.size() > 0){
                Set<String> key = map.keySet();
                permissionName = new ArrayList<>(key);
                permissionDescription = new ArrayList<>();
                for(String k:permissionName){
                    permissionDescription.add(map.get(k));
                }
            }
        }


        @Override
        public int getCount() {
            return permissionName == null ? 0 : permissionName.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(roots.get(position));
            roots.remove(roots.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View root = LayoutInflater.from(context).inflate(R.layout.fragment_permission,container,false);
            TextView tv_permission_name = root.findViewById(R.id.tv_permission_name);
            TextView tv_permission_description = root.findViewById(R.id.tv_permission_description);
            tv_permission_name.setText(permissionName.get(position));
            tv_permission_description.setText(permissionDescription.get(position));
            roots.add(root);
            container.addView(root);
            return root;
        }
    }

    public interface PermissionListener{

        void onPermissionResult(List<String> allow,List<String> deny);

        /**
         *
         * @param isRequested 是否请求了权限
         */
        void afterPermissionRequested(boolean isRequested);
    }
}
