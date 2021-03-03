package com.example.myapp.shopFragment;

import android.content.res.AssetManager;
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

import org.ini4j.Ini;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment {

    protected Ini ini;

    View view;
    RecyclerView shopRecycler;
    ShopAdapter shopAdapter;
    String shop_name[] = new String[10];
    String address1[] = new String[10];
    String address2[] = new String[10];
    String order_text[] = new String[10];
    String reserve_text[] = new String[10];
    // 이미지 갯수를 string.xml 갯수와 맞출 것(이미지가 지점갯수 기준)
    int images[] = {R.drawable.shop2, R.drawable.shop2, R.drawable.shop3, R.drawable.shop4, R.drawable.shop5};

    public TwoFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_two, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Load_IniFile();
        Ini.Section section;

//        shop_name = getResources().getStringArray(R.array.gyeonggi_shopname);
//        int iniCount = Integer.parseInt(ini.get("Shop_Info", "Count"));
        String iniShop1 = ini.get("Shop1","Name");
        String iniShop2 = ini.get("Shop2","Name");
        String iniShop3 = ini.get("Shop3","Name");
        String iniShop4 = ini.get("Shop4","Name");
        String iniShop5 = ini.get("Shop5","Name");
        shop_name[0] = iniShop1;
        shop_name[1] = iniShop2;
        shop_name[2] = iniShop3;
        shop_name[3] = iniShop4;
        shop_name[4] = iniShop5;

        address1 = getResources().getStringArray(R.array.gyeonggi_address1);
        address2 = getResources().getStringArray(R.array.gyeonggi_address2);

        String iniOrder1 = ini.get("Shop1","Open");
        String iniOrder2 = ini.get("Shop2","Open");
        String iniOrder3 = ini.get("Shop3","Open");
        String iniOrder4 = ini.get("Shop4","Open");
        String iniOrder5 = ini.get("Shop5","Open");
        if(iniOrder1.equals("N")){iniOrder1 = "주문 불가능 |";}else{iniOrder1 = "주문 가능 |";}
        if(iniOrder2.equals("N")){iniOrder2 = "주문 불가능 |";}else{iniOrder2 = "주문 가능 |";}
        if(iniOrder3.equals("N")){iniOrder3 = "주문 불가능 |";}else{iniOrder3 = "주문 가능 |";}
        if(iniOrder4.equals("N")){iniOrder4 = "주문 불가능 |";}else{iniOrder4 = "주문 가능 |";}
        if(iniOrder5.equals("N")){iniOrder5 = "주문 불가능 |";}else{iniOrder5 = "주문 가능 |";}
        order_text[0] = iniOrder1;
        order_text[1] = iniOrder2;
        order_text[2] = iniOrder3;
        order_text[3] = iniOrder4;
        order_text[4] = iniOrder5;

        String iniReserve1 = ini.get("Shop1","Reserve");
        String iniReserve2 = ini.get("Shop2","Reserve");
        String iniReserve3 = ini.get("Shop3","Reserve");
        String iniReserve4 = ini.get("Shop4","Reserve");
        String iniReserve5 = ini.get("Shop5","Reserve");
        if(iniReserve1.equals("Y")){iniReserve1 = "| 예약 가능";}else{iniReserve1 = "| 예약 불가능";}
        if(iniReserve2.equals("Y")){iniReserve2 = "| 예약 가능";}else{iniReserve2 = "| 예약 불가능";}
        if(iniReserve3.equals("Y")){iniReserve3 = "| 예약 가능";}else{iniReserve3 = "| 예약 불가능";}
        if(iniReserve4.equals("Y")){iniReserve4 = "| 예약 가능";}else{iniReserve4 = "| 예약 불가능";}
        if(iniReserve5.equals("Y")){iniReserve5 = "| 예약 가능";}else{iniReserve5 = "| 예약 불가능";}
        reserve_text[0] = iniReserve1;
        reserve_text[1] = iniReserve2;
        reserve_text[2] = iniReserve3;
        reserve_text[3] = iniReserve4;
        reserve_text[4] = iniReserve5;

        shopRecycler = getView().findViewById(R.id.rv_2);
        shopAdapter = new ShopAdapter(getActivity(),shop_name, address1, address2, order_text, reserve_text, images);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        shopRecycler.setLayoutManager(layoutManager);
        shopRecycler.setAdapter(shopAdapter);
    }
    
//     모델을 사용할 경우
//    private void setShopRecycler(List<Shop> shopList){
//
//        shopRecycler = getView().findViewById(R.id.rv_2);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//        shopRecycler.setLayoutManager(layoutManager);
//        shopAdapter = new ShopAdapter(getActivity(), shopList);
//        shopRecycler.setAdapter(shopAdapter);
//    }

    private boolean Load_IniFile(){
        AssetManager aMgr = getResources().getAssets();
        InputStream is = null;

        try{
            is = aMgr.open("ShopInfo.ini");
            ini = new Ini(is);
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }
}
