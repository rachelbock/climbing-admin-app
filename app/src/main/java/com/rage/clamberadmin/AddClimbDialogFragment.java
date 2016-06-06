package com.rage.clamberadmin;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Dialog Fragment to collect data on a climb.
 */
public class AddClimbDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.add_climb_gym_rating)
    protected EditText gymRatingEditText;
    @Bind(R.id.add_climb_color_spinner)
    protected Spinner colorSpinner;
    @Bind(R.id.add_climb_type_spinner)
    protected Spinner typeSpinner;
    protected String color;
    protected String type;

    public AddClimbDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Dialog allows user to input the grade of the climb, select the color and type. The Climb
     * Update Fragment is set as the target fragment. Once OK is selected, the information collected
     * is sent back to the fragment to be added to the database.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_climb_dialog, null);
        ButterKnife.bind(this, rootView);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.tape_color, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.climb_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(this);
        colorSpinner.setOnItemSelectedListener(this);

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getActivity()).setView(rootView)
                .setTitle("Add New Climb")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int gymRating = Integer.parseInt(gymRatingEditText.getText().toString());
                        ((ClimbUpdateFragment)getTargetFragment()).onNewClimbRetrieved(gymRating, color, type);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .create();
        return dialog;
    }

    /**
     * Collects the selected spinner item and stores it as an instance variable to pass back to the climb
     * update fragment and add to the database.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        color = colorSpinner.getSelectedItem().toString();
        type = typeSpinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}
