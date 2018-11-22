package gpsutils.wcshxx.com.gps.config;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gpsutils.wcshxx.com.gps.utils.LogUtils;
import gpsutils.wcshxx.com.gps.utils.SharePreUtils;
import gpsutils.wcshxx.com.gps.utils.ToastUtils;

public class Config {

    /**
     * APP设置字典表
     */
    public interface Setting{
        String MAX_ITEMS = "MAX_ITEMS";
        String INTERVAL = "INTERVAL";
        String READER = "READER";
        String SHOW_TYPE = "SHOW_TYPE";
    }

    /**
     * 浏览文本阅读器设置
     * 0：使用系统浏览器打开
     * 1：使用APP打开
     * -1：无默认设置
     */
    public interface Reader{
        int SYSTEM = 0;
        int APP = 1;
        int NOT_SELECT = -1;
    }

    //初始化
    public static void reset(){
        setInterval(10000);
        setMaxItemsNumber(35);
        setReader(Reader.NOT_SELECT);
        setShowType((Set) null);
    }

    public static void setInterval(int millisecond){
        SharePreUtils.saveInt(Setting.INTERVAL,millisecond);
    }

    //GPS更新时间间隔
    public static int getInterval(){
        return SharePreUtils.getInt(Setting.INTERVAL);
    }

    public static void setMaxItemsNumber(int max){
        SharePreUtils.saveInt(Setting.MAX_ITEMS,max);
    }

    //动态数据最大显示条数
    public static int getMaxItemsNumber(){
        return SharePreUtils.getInt(Setting.MAX_ITEMS);
    }

    //文件浏览方式
    public static void setReader(int reader){
        switch (reader){
            case Reader.APP:
            case Reader.NOT_SELECT:
            case Reader.SYSTEM:
                SharePreUtils.saveInt(Setting.READER, reader);
                break;
            default:
                LogUtils.e("参数不在预设范围[-1,0,1]内 : reader = "+reader);
                ToastUtils.show("设置默认阅读器失败");
                break;
        }

    }

    public static int getReader(){
        return SharePreUtils.getInt(Setting.READER);
    }

    public static void setShowType(String...types){
        Set<String> set = null;
        if(types != null && types.length > 0){
            set = new HashSet<>(Arrays.asList(types));
        }
        SharePreUtils.saveSet(Setting.SHOW_TYPE,set);
    }

    public static void setShowType(Set<String> types){
        SharePreUtils.saveSet(Setting.SHOW_TYPE,types);
    }

    public static List<String> getShowType(){
        Set<String> set = SharePreUtils.getSet(Setting.SHOW_TYPE);
        if(set == null){
            set = new HashSet<>();
            set.add("$GPGGA");
        }
        return new ArrayList<>(set);
    }

    public static Set<String> getShowTypeSet(){
        return SharePreUtils.getSet(Setting.SHOW_TYPE);
    }
}
