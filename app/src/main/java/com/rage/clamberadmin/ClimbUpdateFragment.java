package com.rage.clamberadmin;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Displays the climb information for a given Wall Section.
 */
public class ClimbUpdateFragment extends Fragment {

    public static final String TAG = ClimbUpdateFragment.class.getSimpleName();
    protected WallSection wallSection;
    protected List<Climb> climbs;
    protected ClimbRecyclerViewAdapter adapter;
    @Bind(R.id.climb_update_fragment_wall_section_text_view)
    TextView wallSectionNumber;
    @Bind(R.id.climb_update_fragment_last_updated_text_view)
    TextView lastUpdatedText;
    @Bind(R.id.climb_update_fragment_recycler_view)
    RecyclerView climbListRecyclerView;

    public ClimbUpdateFragment() {
        // Required empty public constructor
    }

    public static ClimbUpdateFragment newInstance(WallSection wallSection){
        Bundle args = new Bundle();
        ClimbUpdateFragment fragment = new ClimbUpdateFragment();
        args.putParcelable(WallSectionFragment.ARG_WALL_SECTION, wallSection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_climb_update, container, false);
        ButterKnife.bind(this, rootView);
        wallSection = getArguments().getParcelable(WallSectionFragment.ARG_WALL_SECTION);

        wallSectionNumber.setText(getContext().getString(R.string.wall_section_s, wallSection.getId()));
        lastUpdatedText.setText(getContext().getString(R.string.last_updated_s, wallSection.dateLastUpdated));

        climbs = new ArrayList<>();
        climbListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ClimbRecyclerViewAdapter(climbs, wallSection);
        climbListRecyclerView.setAdapter(adapter);

        return rootView;
    }

    /**
     * When the "View Climbs" button is selected - the getClimbsByWall network call is made to retrieve
     * all of the climbs on teh wall and then the recyclerview is marked as visible.
     */
    @OnClick(R.id.climb_update_fragment_view_climbs_button)
    public void onViewClimbsButton(){

        getClimbsByWall();
        climbListRecyclerView.setVisibility(View.VISIBLE);

    }


    /**
     * When the removall button is clicked - network call is made to change the removed column
     * for all of the climbs on the wall section to 1.
     */
    @OnClick(R.id.climb_update_fragment_remove_all_button)
    public void onRemoveAllButtonClicked(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            final Call<Boolean> removeAllCall = ApiManager.getClamberService().removeClimbsFromWallSection(wallSection.getMainWallId(), wallSection.getId());

            removeAllCall.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "Successfully removed climbs", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "non 200 response returned");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d(TAG, "Failed to remove climbs for wallSection");
                }
            });
        }
        else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();

        }

    /**
     * When the add climb button is clicked, the Add Climb dialog fragment is launched to collect
     * data on the new climb and send back for use in the onNewClimbRetrieved method.
     */
    @OnClick(R.id.climb_update_fragment_add_climb_button)
    public void onAddClimbButtonClicked(){
        AddClimbDialogFragment fragment = new AddClimbDialogFragment();
        fragment.setTargetFragment(this, 0);
        fragment.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    /**
     * Call to the server to retrieve all climbs on a given wall section. Climbs are stored in a list
     * and that is used in the recycler view adapter to display climb data.
     */
    public void getClimbsByWall(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            final Call<List<Climb>> getClimbsByWallCall = ApiManager.getClamberService().getClimbsByWallSection(wallSection.getMainWallId(), wallSection.getId());

            getClimbsByWallCall.enqueue(new Callback<List<Climb>>() {
                @Override
                public void onResponse(Call<List<Climb>> call, Response<List<Climb>> response) {

                    if (response.code() == 200){
                        List<Climb> climbList = response.body();
                        climbs.addAll(climbList);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Log.d(TAG, "Non 200 response received - check server");
                    }

                }

                @Override
                public void onFailure(Call<List<Climb>> call, Throwable t) {
                    Log.d(TAG, "Failed to get climbs - check server");
                }
            });

        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Called from the AddClimbDialogFragment - it takes in the user input rating, color and type
     * and makes a network call to add that as a new climb in the database.
     */
    public void onNewClimbRetrieved(int rating, String color, String type){
        Log.d(TAG, rating + " " + color + " " + type);
        String hexColor = convertColorToString(color);
        Log.d(TAG, hexColor);
        NewClimbRequest request = new NewClimbRequest();
        request.setRating(rating);
        request.setColor(hexColor);
        request.setType(type);
        request.setWallId(wallSection.getId());

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            final Call<Boolean> addClimbCall = ApiManager.getClamberService().addNewClimb(wallSection.getMainWallId(), wallSection.getId(), request);
            addClimbCall.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 200){
                        Log.d(TAG, "success");
                    } else {
                        Log.d(TAG, "non 200 response code received - check server");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d(TAG, "Failure check server");
                }
            });
        }
        else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Method takes in the name of a Color and returns the color as it is stored in the database.
     */
    public String convertColorToString(String color) {
        String hexColor = null;
        switch(color){
            case "Red": hexColor = "#BE0A16";
                break;
            case "Orange": hexColor = "#D45C01";
                break;
            case "Yellow": hexColor = "#FFEF0E";
                break;
            case "Green": hexColor = "#0DB910";
                break;
            case "Blue": hexColor = "#1E4CD9";
                break;
            case "Purple": hexColor = "#9D00BC";
                break;
            case "Pink": hexColor = "#FF6DB1";
                break;
            case "Grey": hexColor = "#6E6C6D";
                break;
            case "White": hexColor = "#FFFFFF";
                break;
            case "Black": hexColor = "#000000";
                break;
        }
        return hexColor;
    }


}
