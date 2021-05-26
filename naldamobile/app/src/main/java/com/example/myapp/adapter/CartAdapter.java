package com.example.myapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.CartActivity;
import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.example.myapp.listener.OnItemClick;
import com.example.myapp.model.CartModel;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private OnItemClick mCallback;

    public CartAdapter(Context context, OnItemClick listener) {
        this.context = context;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.pdtImage.setImageResource(ProductActivity.cartImg.get(position));
        holder.pdtType.setText("("+txtType(ProductActivity.cartChoice.get(position).toString())+")");
        holder.txtQuantity.setText(new StringBuilder().append(ProductActivity.cartAmount.get(position).toString()));
        holder.pdtName.setText(ProductActivity.cartPdtName.get(position));
        holder.pdtShot.setText("샷 : "+ ProductActivity.cartShot.get(position).toString());
        holder.pdtPay.setText("가격 : "+(Integer.parseInt(ProductActivity.cartPay.get(position).toString())*Integer.parseInt(ProductActivity.cartAmount.get(position).toString()))+"원");

        holder.plusBtn.setOnClickListener(v -> plusCartItem(holder, position));
        holder.minusBtn.setOnClickListener(v -> minusCartItem(holder, position));
        holder.deleteBtn.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("메뉴 삭제")
                    .setMessage("삭제하시겠습니까?")
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // 모두 삭제
                            ProductActivity.cartMenuCount -= 1;
                            ProductActivity.cartKey.remove(position);
                            ProductActivity.cartPdtName.remove(position);
                            ProductActivity.cartChoice.remove(position);
                            ProductActivity.cartAmount.remove(position);
                            ProductActivity.cartShot.remove(position);
                            ProductActivity.cartPay.remove(position);
                            ProductActivity.cartImg.remove(position);
//                            holder.pdtPay.setText("가격 : "+(Integer.parseInt(ProductActivity.cartPay.get(position).toString())*Integer.parseInt(ProductActivity.cartAmount.get(position).toString()))+"원");
                            mCallback.updateCartTotalPrice();
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        });
    }

    private String txtType(String type) {
         if(type.equals("1")){
            return "HOT";
        }else if(type.equals("2")){
             return "ICE";
        }
        return "";
    }

    private void plusCartItem(CartViewHolder holder, int position) {
            ProductActivity.cartAmount.set(position, Integer.parseInt(ProductActivity.cartAmount.get(position).toString())+1);
            holder.txtQuantity.setText(new StringBuilder().append(ProductActivity.cartAmount.get(position).toString()));
            holder.pdtPay.setText("가격 : "+(Integer.parseInt(ProductActivity.cartPay.get(position).toString())*Integer.parseInt(ProductActivity.cartAmount.get(position).toString()))+"원");

            // 장바구니 전체가격도 수정해주기(함수써서)
            // updateCartTotalPrice() return cartPay*amount
            mCallback.updateCartTotalPrice();
    }

    private void minusCartItem(CartViewHolder holder, int position) {
        int txtAmount = Integer.parseInt(ProductActivity.cartAmount.get(position).toString());
        if(txtAmount > 1){
            ProductActivity.cartAmount.set(position, txtAmount-1);
            holder.txtQuantity.setText(new StringBuilder().append(ProductActivity.cartAmount.get(position).toString()));
            holder.pdtPay.setText("가격 : "+(Integer.parseInt(ProductActivity.cartPay.get(position).toString())*Integer.parseInt(ProductActivity.cartAmount.get(position).toString()))+"원");
            
            // 장바구니 전체가격도 수정해주기(함수써서)
            // updateCartTotalPrice()
            mCallback.updateCartTotalPrice();
        }
    }


    @Override
    public int getItemCount() {
        return ProductActivity.cartKey.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        Button deleteBtn;
        ImageView pdtImage, plusBtn, minusBtn;
        TextView pdtType, pdtName, pdtShot, txtQuantity, pdtPay;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            pdtImage = (ImageView) itemView.findViewById(R.id.pdt_image);
            deleteBtn = (Button) itemView.findViewById(R.id.delete_btn);
            plusBtn = (ImageView) itemView.findViewById(R.id.plus_btn);
            minusBtn = (ImageView) itemView.findViewById(R.id.minus_btn);
            pdtType = (TextView) itemView.findViewById(R.id.pdt_type);
            pdtName = (TextView) itemView.findViewById(R.id.pdt_name);
            pdtShot = (TextView) itemView.findViewById(R.id.pdt_shot);
            txtQuantity = (TextView) itemView.findViewById(R.id.txt_quantity);
            pdtPay = (TextView) itemView.findViewById(R.id.pdt_pay);
        }
    }
}
