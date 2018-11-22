package gpsutils.wcshxx.com.gps.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.config.Config;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoHolder> {

    private Context context;
    private List<String> list;

    public InfoAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();
    }

    public InfoAdapter(Context context,List<String> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_info,parent,false);
        return new InfoHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoHolder holder, int position) {
        holder.tv_info.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void updateData(List<String> list){
        this.list.removeAll(this.list);
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void add(String GPAAS){
        if(list.size() > Config.getMaxItemsNumber()){
            list.remove(0);
        }
        list.add(GPAAS);
        notifyDataSetChanged();
    }

    class InfoHolder extends RecyclerView.ViewHolder{

        TextView tv_info;

        public InfoHolder(View itemView) {
            super(itemView);
            tv_info = (TextView)itemView.findViewById(R.id.tv_info);
        }
    }
}
