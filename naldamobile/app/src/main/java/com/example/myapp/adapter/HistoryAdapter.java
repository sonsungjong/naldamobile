package com.example.myapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.DetailPageActivity;
import com.example.myapp.ItemViewHolder;
import com.example.myapp.LoadMore;
import com.example.myapp.LoadingViewHolder;
import com.example.myapp.R;
import com.example.myapp.model.HistoryModel;

import java.util.ArrayList;
import java.util.List;
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    ArrayList<HistoryModel> items;
    int visibleThreshold =5;
    int lastVisibleItem, totalItemCount;
    Context context;

    public HistoryAdapter(RecyclerView recyclerView, Activity activity, ArrayList<HistoryModel> items) {
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
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.history_row_item,parent,false);
            return new HistoryViewHolder(view);
        }
        else if (viewType==VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.loading_progress_bar,parent,false);
            return new HistoryViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

            holder.h_shop_name.setText(items.get(position).gethShopName());
            holder.h_state.setText(items.get(position).gethState());
            holder.h_date_time.setText(items.get(position).gethDateTime());
            holder.h_pdt_name.setText(items.get(position).gethPdtName());
            holder.h_pay_price.setText(items.get(position).gethPayPrice());
            holder.h_classify.setText(items.get(position).gethClassify());
            holder.h_reserve_time.setText(items.get(position).gethReserveTime());
            holder.goDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailPageActivity.class);
                    intent.putExtra("h_shop_name", items.get(position).gethShopName());
                    intent.putExtra("h_state", items.get(position).gethState());
                    intent.putExtra("h_date_time", items.get(position).gethDateTime());
//                    intent.putExtra("h_pdt_name", items.get(position).gethPdtName());
                    intent.putExtra("h_pay_price", items.get(position).gethPayPrice());
                    intent.putExtra("h_classify", items.get(position).gethClassify());
                    intent.putExtra("h_reserve_time", items.get(position).gethReserveTime());
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoded(){
        isLoading = false;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView h_shop_name;
        TextView h_state;
        TextView h_date_time;
        TextView h_pdt_name;
        TextView h_pay_price;
        TextView h_classify;
        TextView h_reserve_time;
        LinearLayout goDetail;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            h_shop_name = itemView.findViewById(R.id.h_shop_name);
            h_state = itemView.findViewById(R.id.h_state);
            h_date_time = itemView.findViewById(R.id.h_date_time);
            h_pdt_name = itemView.findViewById(R.id.h_pdt_name);
            h_pay_price = itemView.findViewById(R.id.h_pay_price);
            h_classify = itemView.findViewById(R.id.h_classify);
            h_reserve_time = itemView.findViewById(R.id.h_reserve_time);
            goDetail = itemView.findViewById(R.id.go_detail);
        }
    }
}
