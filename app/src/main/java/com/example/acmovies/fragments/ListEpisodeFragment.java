package com.example.acmovies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.MovieDetailActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.EpisodeRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Video;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEpisodeFragment extends Fragment implements ItemClickListener {
    private View view;
    private RecyclerView recycler_episode;
    private EpisodeRecyclerAdapter adapter;
    private List<Episode> episodeList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_episode, container,false);
        Bundle bundle = getArguments();
        episodeList = (List<Episode>) bundle.getSerializable("listep");

        recycler_episode = (RecyclerView) view.findViewById(R.id.recycler_list_ep);
        adapter = new EpisodeRecyclerAdapter(getContext(),episodeList,this);
        recycler_episode.setHasFixedSize(true);
        recycler_episode.setLayoutManager(new GridAutoFitLayoutManager(getContext(),180));
        recycler_episode.setAdapter(adapter);

        return view;
    }

    @Override
    public void onMovieClick(Movie movie) {

    }

    @Override
    public void onEpisodeClick(Episode episode) {
        MovieDetailActivity activity = (MovieDetailActivity) getActivity();

        Integer video_id = episode.getVideoId();

        DataClient dataClient = APIUtils.getData();
        Call<Video> callMovie = dataClient.GetVideobyId(video_id);
        callMovie.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful()){
                    Video video = response.body();
                    activity.video_url = video.getVideoUrl();
                    activity.exitPlayer();
                    activity.initializePlayer();
                    activity.saveUserView(episode);
                } else {
                    Log.d("ERROR: ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
            }
        });
    }

    @Override
    public void onActorClick(Actor actor) {

    }

    @Override
    public void onGenreClick(Genres genres) {

    }

    public static class GridAutoFitLayoutManager extends GridLayoutManager {
        private int mColumnWidth;
        private boolean mColumnWidthChanged = true;
        private boolean mWidthChanged = true;
        private int mWidth;
        private static final int sColumnWidth = 200; // assume cell width of 200dp

        public GridAutoFitLayoutManager(Context context, int columnWidth) {
            /* Initially set spanCount to 1, will be changed automatically later. */
            super(context, 1);
            setColumnWidth(checkedColumnWidth(context, columnWidth));
        }

        public GridAutoFitLayoutManager(Context context, int columnWidth, int orientation, boolean reverseLayout) {
            /* Initially set spanCount to 1, will be changed automatically later. */
            super(context, 1, orientation, reverseLayout);
            setColumnWidth(checkedColumnWidth(context, columnWidth));
        }

        private int checkedColumnWidth(Context context, int columnWidth) {
            if (columnWidth <= 0) {
                columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sColumnWidth,
                        context.getResources().getDisplayMetrics());
            } else {
                columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, columnWidth,
                        context.getResources().getDisplayMetrics());
            }
            return columnWidth;
        }

        private void setColumnWidth(int newColumnWidth) {
            if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
                mColumnWidth = newColumnWidth;
                mColumnWidthChanged = true;
            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            int width = getWidth();
            int height = getHeight();

            if (width != mWidth) {
                mWidthChanged = true;
                mWidth = width;
            }

            if (mColumnWidthChanged && mColumnWidth > 0 && width > 0 && height > 0
                    || mWidthChanged) {
                int totalSpace;
                if (getOrientation() == VERTICAL) {
                    totalSpace = width - getPaddingRight() - getPaddingLeft();
                } else {
                    totalSpace = height - getPaddingTop() - getPaddingBottom();
                }
                int spanCount = Math.max(1, totalSpace / mColumnWidth);
                setSpanCount(spanCount);
                mColumnWidthChanged = false;
                mWidthChanged = false;
            }
            super.onLayoutChildren(recycler, state);
        }
    }
}
