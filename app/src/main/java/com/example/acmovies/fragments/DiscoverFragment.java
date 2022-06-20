package com.example.acmovies.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.acmovies.MainActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.GenreRecyclerAdapter;
import com.example.acmovies.adapter.ViewPagerAdapter;
import com.example.acmovies.model.Genres;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
