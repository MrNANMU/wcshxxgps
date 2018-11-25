package gpsutils.wcshxx.com.gps.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

public class ShareUtils {

    public static void share(Context context,String filePath){
        File file = new File(filePath);
        Uri uri = FileProvider.getUriForFile(context,"gpsutils.wcshxx.com.gps.fileprovider",file);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        // 指定发送内容的类型 (MIME type)
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent,"分享至"));
    }

}
