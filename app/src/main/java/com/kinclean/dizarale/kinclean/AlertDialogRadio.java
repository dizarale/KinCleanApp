package com.kinclean.dizarale.kinclean;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

import com.kinclean.dizarale.kinclean.helper.AlertPositiveListener;

import java.util.List;

/**
 * Created by dizar on 2/10/2016.
 */
public class AlertDialogRadio extends DialogFragment {
    public static final String DATA = "items";

    public static final String SELECTED = "selected";

    private AlertPositiveListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            this.listener = (AlertPositiveListener)activity.getFragmentManager();
        }
        catch ( ClassCastException oops )
        {
            oops.printStackTrace();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            this.listener = (AlertPositiveListener)context;
        }
        catch ( ClassCastException oops )
        {
            oops.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Please Select");
        dialog.setPositiveButton("Cancel", new PositiveButtonClickListener());

        List<String> list = (List<String>)bundle.get(DATA);
        int position = bundle.getInt(SELECTED);

        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
        dialog.setSingleChoiceItems(cs, position, selectItemListener);

        return dialog.create();
    }
    class PositiveButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            dialog.dismiss();
        }
    }
    OnClickListener selectItemListener = new OnClickListener()
    {

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            // process

            if ( listener != null )
            {
                listener.onPositiveClick(which);
            }
            dialog.dismiss();
        }

    };

}
