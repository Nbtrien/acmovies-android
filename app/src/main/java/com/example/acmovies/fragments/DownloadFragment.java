package com.example.acmovies.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.acmovies.MainActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DownloadFragment extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_download,container,false);

        return view;
    }

    private void inView() {
    }
}
