package com.example.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.example.myapp.R;
import com.example.myapp.model.ProductImageModel;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {

    private List<ProductImageModel> productModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public ProductImageAdapter(List<ProductImageModel> productModels, Context context) {
        this.productModels = productModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.pdt_item_one, container, false);

        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        imageView.setImageResource(productModels.get(position).getImage());
        title.setText(productModels.get(position).getTitle());
        desc.setText(productModels.get(position).getDesc());

        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
