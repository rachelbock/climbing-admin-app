package com.rage.clamberadmin;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Recycler View to display climbs on a given wall section.
 */
public class ClimbRecyclerViewAdapter extends RecyclerView.Adapter<ClimbRecyclerViewAdapter.ClimbsViewHolder> {

    protected List<Climb> climbs;
    protected WallSection wallSection;
    public static final String TAG = ClimbRecyclerViewAdapter.class.getSimpleName();

    public ClimbRecyclerViewAdapter(List<Climb> climbs, WallSection wallSection){
        this.climbs = climbs;
        this.wallSection = wallSection;
    }

    @Override
    public ClimbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View climbView = inflater.inflate(R.layout.climb_row, parent, false);
        return new ClimbsViewHolder(climbView);
    }

    /**
     * From the list of climbs supplied, sets up the climb row with each climb's data. On Click
     * Listener set up for the remove checkbox. When the checkbox is clicked, the climb is
     * removed from the database.
     */
    @Override
    public void onBindViewHolder(final ClimbsViewHolder holder, int position) {
        Climb climb = climbs.get(position);
        String color = climb.getTapeColor();
        holder.colorTextView.setBackgroundColor(Color.parseColor(color));
        holder.gradeTextView.setText(String.valueOf(climb.getGymRating()));
        holder.typeTextView.setText(climb.getType());

        holder.removeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Climb oneClimb = climbs.get(holder.getAdapterPosition());

                final Call<Boolean> removeSingleClimbCall = ApiManager.getClamberService().removeSingleClimb(wallSection.getMainWallId(), wallSection.getId(),oneClimb.getClimbId());
                removeSingleClimbCall.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200){
                            Log.d(TAG, "success");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d(TAG, "Failed - check server");
                    }
                });

            }
        });

    }

    /**
     * Returns the list of the climbs array.
     */
    @Override
    public int getItemCount() {
        return climbs.size();
    }

    /**
     * View Holder class to set up the items on the climb row.
     */
    public static class ClimbsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.climb_row_color_image_view)
        protected ImageView colorTextView;

        @Bind(R.id.climb_row_grade_text_view)
        protected TextView gradeTextView;

        @Bind(R.id.climb_row_type_text_view)
        protected TextView typeTextView;

        @Bind(R.id.climb_row_remove_checkbox)
        protected CheckBox removeCheckBox;

        protected View fullView;

        public ClimbsViewHolder(View itemView) {
            super(itemView);
            fullView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
