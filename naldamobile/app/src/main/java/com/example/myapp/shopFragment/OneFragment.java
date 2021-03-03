package com.example.myapp.shopFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.adapter.ShopAdapter;

public class OneFragment extends Fragment {

    View view;
    RecyclerView shopRecycler;
    ShopAdapter shopAdapter;
    String shop_name[] = new String[10];
    String address1[] = new String[10];
    String address2[] = new String[10];
    String order_text[] = new String[10];
    String reserve_text[] = new String[10];
    // 이미지 갯수를 string.xml 갯수와 맞출 것
    int images[] = {R.drawable.shop1, R.drawable.shop2, R.drawable.shop3, R.drawable.shop4, R.drawable.shop5, R.drawable.shop1};

    public OneFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_one, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shop_name = getResources().getStringArray(R.array.seoul_shopname);
        address1 = getResources().getStringArray(R.array.seoul_address1);
        address2 = getResources().getStringArray(R.array.seoul_address2);
        order_text = getResources().getStringArray(R.array.seoul_order_text);
        reserve_text = getResources().getStringArray(R.array.seoul_reserve_text);

        shopRecycler = getView().findViewById(R.id.rv_1);
        shopAdapter = new ShopAdapter(getActivity(),shop_name, address1, address2, order_text, reserve_text, images);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        shopRecycler.setLayoutManager(layoutManager);
        shopRecycler.setAdapter(shopAdapter);
    }
    
    // 모델사용할 경우
//    private void setShopRecycler(List<Shop> shopList){
//
//        shopRecycler = getView().findViewById(R.id.rv_1);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//        shopRecycler.setLayoutManager(layoutManager);
//        shopAdapter = new ShopAdapter(getActivity(), shopList);
//        shopRecycler.setAdapter(shopAdapter);
//    }
}
