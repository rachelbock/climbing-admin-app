package com.rage.clamberadmin;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays the wall sections on a given wall.
 */
public class WallSectionFragment extends Fragment implements WallsPageRecyclerViewAdapter.OnWallSelectedListener {

    public static final String TAG = WallSectionFragment.class.getSimpleName();
    public static final String ARG_WALL_SECTION = "Wall Section Id";
    protected List<WallSection> wallSections;
    protected int wall;
    protected WallsPageRecyclerViewAdapter adapter;


    @Bind(R.id.walls_page_grid_recycler_view)
    protected RecyclerView recyclerView;

    public WallSectionFragment() {
        // Required empty public constructor
    }

    public static WallSectionFragment newInstance(int wallId){
        WallSectionFragment fragment = new WallSectionFragment();
        Bundle args = new Bundle();
        args.putInt(WallsFragment.ARG_WALL_ID, wallId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_wall_section, container, false);
        ButterKnife.bind(this, rootView);
        wallSections = new ArrayList<>();
        wall = getArguments().getInt(WallsFragment.ARG_WALL_ID);

        getWallSectionsByWall(wall);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new WallsPageRecyclerViewAdapter(wallSections, WallSectionFragment.this, getActivity());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    /**
     * Takes in the main Wall Id and retrieves the Wall Sections on that given wall.
     */
    public void getWallSectionsByWall(int wallId) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            final Call<List<WallSection>> wallSectionCall = ApiManager.getClamberService().getWallSection(wallId);

            wallSectionCall.enqueue(new Callback<List<WallSection>>() {
                @Override
                public void onResponse(Call<List<WallSection>> call, Response<List<WallSection>> response) {

                    if (response.code() == 200){
                        List<WallSection> wallSectionsArrayList = response.body();
                        wallSections.addAll(wallSectionsArrayList);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Log.d(TAG, "Non 200 response received - check server");
                    }
                }

                @Override
                public void onFailure(Call<List<WallSection>> call, Throwable t) {
                    Log.d(TAG, "Failure on retrieving wall sections by wall id ");
                }
            });
        }
        else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * When a wall is selected the climb update fragment is launched and given the wall section.
     */
    @Override
    public void onWallSelected(WallSection wallSection) {
        Log.d(TAG, "wallSelected");
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.app_activity_frame_layout, ClimbUpdateFragment.newInstance(wallSection));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
