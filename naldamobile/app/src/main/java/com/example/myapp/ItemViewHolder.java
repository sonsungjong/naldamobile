package com.example.myapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView h_shop_name;        // 지점이름
    public TextView h_state;        // 주문현황(결제완료/결제취소)
    public TextView h_date_time;        // 결제 요일 및 시간
    public TextView h_pdt_name;     // 상품명 요약
    public TextView h_pay_price;        // 결제가격
    public TextView h_classify;     // 실시간주문 or 예약
    public TextView h_reserve_time;     // null or 예약시간

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        h_shop_name = itemView.findViewById(R.id.h_shop_name);
        h_state = itemView.findViewById(R.id.h_state);
        h_date_time = itemView.findViewById(R.id.h_date_time);
        h_pdt_name = itemView.findViewById(R.id.h_pdt_name);
        h_pay_price = itemView.findViewById(R.id.h_pay_price);
        h_classify = itemView.findViewById(R.id.h_classify);
        h_reserve_time = itemView.findViewById(R.id.h_reserve_time);
    }
}
