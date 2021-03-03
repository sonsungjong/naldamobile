package com.example.myapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.LoadMore;
import com.example.myapp.R;
import com.example.myapp.model.DetailModel;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder>{

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    ArrayList<DetailModel> items;
    int visibleThreshold =5;
    int lastVisibleItem, totalItemCount;

    public DetailAdapter(RecyclerView recyclerView, Activity activity, ArrayList<DetailModel> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    if(loadMore!=null)
                        loadMore.onLoadMore();
                    isLoading=true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore){
        this.loadMore=loadMore;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.detail_item,parent,false);
            return new DetailViewHolder(view);
        }
        else if (viewType==VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.loading_progress_bar,parent,false);
            return new DetailViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {

            DetailModel item = items.get(position);
            holder.d_pdt_name.setText(items.get(position).getdPdtName());
            holder.d_type.setText(items.get(position).getdType());
            holder.d_shot.setText(items.get(position).getdShot());
            holder.d_quantity.setText(items.get(position).getdQuantity());
            holder.d_price.setText(items.get(position).getdPrice());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoded(){
        isLoading = false;
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder{

        TextView d_pdt_name;
        TextView d_type;
        TextView d_shot;
        TextView d_quantity;
        TextView d_price;


        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            d_pdt_name = itemView.findViewById(R.id.d_pdt_name);
            d_type = itemView.findViewById(R.id.d_type);
            d_shot = itemView.findViewById(R.id.d_shot);
            d_quantity = itemView.findViewById(R.id.d_quantity);
            d_price = itemView.findViewById(R.id.d_price);
        }
    }
}
