package com.rage.clamberadmin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Holds 4 main wall images and launches appropriate Wall Section Data when one is selected
 */
public class WallsFragment extends Fragment {

    public static final String ARG_WALL_ID = "Wall ID";
    @Bind(R.id.wall_page_wall_1_image)
    protected ImageView wall1Image;
    @Bind(R.id.wall_page_wall_2_image)
    protected ImageView wall2Image;
    @Bind(R.id.wall_page_wall_3_image)
    protected ImageView wall3Image;
    @Bind(R.id.wall_page_wall_4_image)
    protected ImageView wall4Image;

    public WallsFragment() {
        // Required empty public constructor
    }

    public static WallsFragment newInstance(){
        return new WallsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_walls, container, false);
        ButterKnife.bind(this, rootView);

        Picasso.with(getActivity()).load(R.drawable.wall_1_p).fit().centerCrop().into(wall1Image);
        Picasso.with(getActivity()).load(R.drawable.wall_2_p).fit().centerCrop().into(wall2Image);
        Picasso.with(getActivity()).load(R.drawable.wall_3_p).fit().centerCrop().into(wall3Image);
        Picasso.with(getActivity()).load(R.drawable.wall_4_p).fit().centerCrop().into(wall4Image);

        return rootView;
    }

    /**
     * OnClick listeners for each of the walls. When selected, the logged in user and wall number
     * are passed through to the WallSectionFragment to display the appropriate WallSection list.
     */
    @OnClick({R.id.wall_page_wall_1_image, R.id.wall_page_wall_2_image, R.id.wall_page_wall_3_image, R.id.wall_page_wall_4_image})
    public void onWallButtonClicked(ImageView button) {
        int wallNum = Integer.parseInt(button.getTag().toString());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.app_activity_frame_layout, WallSectionFragment.newInstance(wallNum));
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
