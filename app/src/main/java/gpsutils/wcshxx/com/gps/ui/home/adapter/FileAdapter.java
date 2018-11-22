package gpsutils.wcshxx.com.gps.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.callback.OnItemClickedListener;
import gpsutils.wcshxx.com.gps.utils.DateUtils;
import gpsutils.wcshxx.com.gps.utils.LogUtils;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileHolder> {

    private Context context;
    private List<String> files;
    private OnItemClickedListener listener;

    public FileAdapter(Context context,List<String> files){
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_file,parent,false);
        return new FileHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, int position) {
        File file = new File(files.get(position));
        if(file.exists()){
            try {
                holder.tv_file_name.setText(file.getName());
                Scanner scanner = new Scanner(file);
                if(scanner.hasNext()){
                    String time = DateUtils.longToString(Long.valueOf(scanner.nextLine()),"yyyy-MM-dd HH:mm:ss");
                    holder.tv_time.setText(time);
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }

    public void setOnItemClickedListener(OnItemClickedListener listener){
        this.listener = listener;
    }

    public void updateList(List<String> list){
        files.removeAll(files);
        files.addAll(list);
        notifyDataSetChanged();
    }

    class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_file_name,
                tv_time,
                tv_rename,
                tv_load,
                tv_delete,
                tv_share;

        public FileHolder(View itemView) {
            super(itemView);
            tv_file_name = $(R.id.tv_file_name);
            tv_time = $(R.id.tv_time);
            tv_rename = $(R.id.tv_rename);
            tv_load = $(R.id.tv_load);
            tv_delete = $(R.id.tv_delete);
            tv_share = $(R.id.tv_share);
            setListeners();
        }

        private void setListeners(){
            itemView.setOnClickListener(this);
            tv_rename.setOnClickListener(this);
            tv_load.setOnClickListener(this);
            tv_delete.setOnClickListener(this);
            tv_share.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClick(getLayoutPosition(),v);
            }else{
                throw new NullPointerException("未设置监听");
            }
        }

        private <V extends View> V $(int id){
            return (V)itemView.findViewById(id);
        }
    }
}
