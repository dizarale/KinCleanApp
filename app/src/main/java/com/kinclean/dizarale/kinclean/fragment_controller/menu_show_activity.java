package com.kinclean.dizarale.kinclean.fragment_controller;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kinclean.dizarale.kinclean.R;
import com.kinclean.dizarale.kinclean.menu.menu_model;
import com.kinclean.dizarale.kinclean.service.APP_config;
import com.kinclean.dizarale.kinclean.service.HTTPReq;
import com.squareup.picasso.Picasso;
import android.widget.Toast;

import org.parceler.Parcels;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link menu_show_activity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link menu_show_activity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menu_show_activity extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    protected View view;
    private Context Context;
    private ProgressBar progressBar;
    RequestBody formBody;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected ImageView imageView;
    protected TextView menu_name;
    protected TextView menu_name_thai;
    protected TextView menu_des;
    protected TextView menu_price;

    protected Button num;
    protected Button submit;

    private int number =1;


    menu_model thisMenu;


    private OnFragmentInteractionListener mListener;

    public menu_show_activity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment menu_show.
     */
    // TODO: Rename and change types and number of parameters
    public static menu_show_activity newInstance(String param1, String param2) {
        menu_show_activity fragment = new menu_show_activity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisMenu = new menu_model();
        Bundle b = getArguments();
        thisMenu = Parcels.unwrap(b.getParcelable("menu_detail"));
        Log.v("agr ", thisMenu.toString());
        Context = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);



        imageView = (ImageView) view.findViewById(R.id.thumbnail);
        menu_name = (TextView) view.findViewById(R.id.menu_name);
        menu_name_thai = (TextView) view.findViewById(R.id.menu_name_thai);
        menu_price = (TextView) view.findViewById(R.id.menu_price);
        menu_des = (TextView) view.findViewById(R.id.menu_des);
        num = (Button) view.findViewById(R.id.number);
        submit = (Button) view.findViewById(R.id.submit);

        Picasso.with(getContext()).load(thisMenu.getThumbnail())
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(imageView);

        menu_name.setText(thisMenu.getTitle());
        menu_name_thai.setText(thisMenu.getTitle_thai());
        menu_price.setText(thisMenu.getPrice());
        menu_des.setText(thisMenu.getDes());

        num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShow();
                Log.v("onClick ", "numclick");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APP_config app_config = new APP_config();
                String menu_id = thisMenu.getid();
                String menu_num = number+"";

                formBody = new FormBody.Builder()
                        .add("cus_imei", app_config.imei)
                        .add("cus_tel", app_config.user_tel)
                        .add("menu_id", menu_id)
                        .add("menu_num", menu_num)
                        .add("menu_des", "...")
                        .build();

                String url = app_config.PostPreorder;
                new AsyncHttpTask().execute(url);
                progressBar.setVisibility(View.VISIBLE);
                Log.v("onClick ", "SubmitClick " + menu_id + " - " + menu_num + " - " + app_config.imei);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_show_activity, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void dialogShow(){
        final Dialog dialog = new Dialog(Context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.number_picker);
        NumberPicker np = (NumberPicker)dialog.findViewById(R.id.numberpicker1);

        np.setMinValue(1);
        np.setMaxValue(20);
        np.setValue(number);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                number = newVal;
            }
        });
        Button set_btn = (Button) dialog.findViewById(R.id.btnSet);
        Button close_btn = (Button) dialog.findViewById(R.id.btncancel);
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num.setText(number+"");
                dialog.dismiss();
            }
        });


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            String result = null;
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HTTPReq PostData = new HTTPReq();
            result = PostData.HTTPPOST(url, formBody);

            if(result == "Can not Get Data"){
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                Toast.makeText(getActivity(),"เพื่มรายการเรียบร้อย",Toast.LENGTH_LONG).show();
                Log.v("result", "succ");
                getActivity().onBackPressed();
            } else {
                Toast.makeText(getActivity(),"ตรวจสอบการเชื่อมต่อ",Toast.LENGTH_LONG).show();
            }
        }

        public void setProgressBarIndeterminateVisibility(boolean progressBarIndeterminateVisibility) {
            //this.progressBarIndeterminateVisibility = progressBarIndeterminateVisibility;
        }

    }
}
