package com.merchez.socialrunning.socialrunning.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.merchez.socialrunning.socialrunning.R;

/**
 * Created by sebastien on 25/03/2017.
 */

public class NetworkDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_network_dialog_message);
        builder.setTitle(R.string.no_network_dialog_title);

        // Create the AlertDialog object and return it
        return builder.create();
    }


}
