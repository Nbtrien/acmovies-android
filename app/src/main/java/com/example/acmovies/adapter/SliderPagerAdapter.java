package com.example.acmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.R;
import com.example.acmovies.model.Movie;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {
    private Context context;
    private List<Movie> slidemovieList;
    ItemClickListener itemClickListener;

    public SliderPagerAdapter(Context context, List<Movie> slidemovieList, ItemClickListener itemClickListener) {
        this.context = context;
        this.slidemovieList = slidemovieList;
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sliderLayout = layoutInflater.inflate(R.layout.slide_item,null);

        ImageView sliderImg = sliderLayout.findViewById(R.id.slide_Img);
        TextView sliderTxt = sliderLayout.findViewById(R.id.slide_title);
        CardView sliderCard = sliderLayout.findViewById(R.id.cardView_slide);

        Glide.with(context).load(slidemovieList.get(position).getCoverimage().getImageUrl()).into(sliderImg);
        sliderTxt.setText(slidemovieList.get(position).getName());
        container.addView(sliderLayout);

        sliderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onMovieClick(slidemovieList.get(position));
            }
        });

        return sliderLayout;
    }

    @Override
    public int getCount() {
        return slidemovieList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
