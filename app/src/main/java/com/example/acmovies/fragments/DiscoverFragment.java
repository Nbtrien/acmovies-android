package com.example.acmovies.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.acmovies.R;
import com.example.acmovies.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DiscoverFragment extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover,container,false);

        inView();
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new MovieCollectionFragment(),"Bộ sưu tập");
        adapter.AddFragment(new SeriesMoviesFragment(),"Phim bộ");
        adapter.AddFragment(new FeatureMoviesFragment(),"Phim lẻ");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void inView() {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_discover);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_discover);
    }
}
