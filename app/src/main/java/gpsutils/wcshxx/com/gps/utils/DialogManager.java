package gpsutils.wcshxx.com.gps.utils;


import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gpsutils.wcshxx.com.gps.base.view.BaseDialog;

/**
 * 此类是方便批量释放Dialog的工具类，适用对象为BaseDialog及其子类
 * 调用BaseDialog的addToManager就可以将Dialog添加到工具类中
 * DialogHelper中也有批量添加的方法
 * Context的Name作为key，保存了指定Context的所有Dialog
 * 只需要在Context的onDestroy中调用free方法即可
 *
 * 此类本身是为了防止Dialog引起内存泄漏，并且方便释放，使用不当反而适得其反。
 */
public class DialogManager {

    private static final ArrayMap<String,List<BaseDialog>> map = new ArrayMap<>();

    public static void add(BaseDialog dialog){
        String key = dialog.getDialogContext().getClass().getSimpleName();
        Set<String> keySet = map.keySet();
        if(keySet.contains(key)){
            List<BaseDialog> list = map.get(key);
            list.add(dialog);
        }else{
            ArrayList<BaseDialog> list = new ArrayList<>();
            list.add(dialog);
            map.put(key,list);
        }
    }

    public static void free(Context context){
        List<BaseDialog> list = map.get(context.getClass().getSimpleName());
        if(list != null){
            for(BaseDialog dialog:list){
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                    dialog = null;
                }
            }
        }
    }

    public static void remove(BaseDialog dialog){
        String key = dialog.getDialogContext().getClass().getSimpleName();
        Set<String> keySet = map.keySet();
        if(keySet.contains(key)){
            List<BaseDialog> list = map.get(key);
            list.remove(dialog);
        }else{
            throw new NullPointerException("Dialog未被添加到DialogManager中");
        }
    }
}
