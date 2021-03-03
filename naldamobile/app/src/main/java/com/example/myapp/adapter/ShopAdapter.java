package com.example.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.example.myapp.ShopActivity;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    Context context;
    String shop_name[], address1[], address2[], order_text[], reserve_text[];
    int images[];

    public ShopAdapter(Context context, String shop_name[], String address1[], String address2[], String order_text[], String reserve_text[], int img[]) {
        this.context = context;
        this.shop_name = shop_name;
        this.address1 = address1;
        this.address2 = address2;
        this.order_text = order_text;
        this.reserve_text = reserve_text;
        this.images = img;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.shop_row_item, parent, false);
        return new ShopViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        holder.shop_name.setText(shop_name[position]);
        holder.address1.setText(address1[position]);
        holder.address2.setText(address2[position]);
        holder.order_text.setText(order_text[position]);
        holder.reserve_text.setText(reserve_text[position]);
        holder.shopImage.setImageResource(images[position]);

        if(holder.order_text.getText().toString().contains("주문 불가능")){
            holder.itemView.setEnabled(false);
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.disable_bg));
        }else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("shop_name", shop_name[position]);
                    intent.putExtra("address1", address1[position]);
                    intent.putExtra("address2", address2[position]);
                    intent.putExtra("order_text", order_text[position]);
                    intent.putExtra("reserve_text", reserve_text[position]);
                    intent.putExtra("images", images[position]);
                    intent.putExtra("member_id", ShopActivity.member_id);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static final class ShopViewHolder extends RecyclerView.ViewHolder{

        TextView shop_name;
        TextView address1;
        TextView address2;
        TextView order_text;
        TextView reserve_text;
        ImageView shopImage;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            // holder에 쓰임
            shop_name = itemView.findViewById(R.id.shop_name);
            address1 = itemView.findViewById(R.id.address1);
            address2 = itemView.findViewById(R.id.address2);
            order_text = itemView.findViewById(R.id.order_text);
            reserve_text = itemView.findViewById(R.id.reserve_text);
            shopImage = itemView.findViewById(R.id.shop_image);
        }
    }
}
