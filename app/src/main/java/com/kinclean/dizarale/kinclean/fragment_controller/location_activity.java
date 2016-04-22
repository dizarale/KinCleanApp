package com.kinclean.dizarale.kinclean.fragment_controller;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kinclean.dizarale.kinclean.AlertDialogRadio;
//import com.kinclean.dizarale.kinclean.AlertDialogRadio.AlertPositiveListener;
import com.kinclean.dizarale.kinclean.R;
import com.kinclean.dizarale.kinclean.cart_RecycleView_Adapter;
import com.kinclean.dizarale.kinclean.helper.AlertPositiveListener;
import com.kinclean.dizarale.kinclean.helper.SimpleItemTouchHelperCallback;
import com.kinclean.dizarale.kinclean.service.APP_config;
import com.kinclean.dizarale.kinclean.service.HTTPReq;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link location_activity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link location_activity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class location_activity extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RequestBody formBody;
    private Context Context;
    FragmentActivity activity_context;

    Button time_btn;
    Button location_btn;

    Button submit_btn;


    int HourOfDay = 0;
    int MinuteOfDay = 0;
    String Time_format;
    String location;
    String loc_detail;

    APP_config app_config = new APP_config();

    private OnFragmentInteractionListener mListener;

    public location_activity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment location_activity.
     */
    // TODO: Rename and change types and number of parameters
    public static location_activity newInstance(String param1, String param2) {
        location_activity fragment = new location_activity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText loc_edit = (EditText) view.findViewById(R.id.location_edit);

        time_btn = (Button) view.findViewById(R.id.time_btn);
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(Context, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        HourOfDay = hourOfDay;
                        MinuteOfDay = minute;
                        if(HourOfDay<8||HourOfDay>20){
                            time_btn.setText("กรุณาเลือกเวลาตามข้อตกลง");
                            time_btn.setTextColor(Color.RED);

                        }else{
                            time_btn.setText("รับอาหารเวลา " + hourOfDay + " โมง " + minute + " นาที");
                            time_btn.setTextColor(Color.BLACK);
                            time_btn.setError(null);
                        }
                    }
                }, HourOfDay, MinuteOfDay, true);

                timePickerDialog.setTitle("เวลารับอาหาร");
                timePickerDialog.show();
            }
        });
        location_btn = (Button) view.findViewById(R.id.location_btn);
        location_btn.setText("สถานที่รับ");
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogRadio dialog = new DialogRadio();

                FragmentManager manager = activity_context.getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(AlertDialogRadio.DATA, getItems());        // Require ArrayList
                bundle.putInt(AlertDialogRadio.SELECTED, 0);
                dialog.setArguments(bundle);
                dialog.show(manager, "Dialog");
            }
        });
        submit_btn = (Button) view.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean boo_time = true , boo_loc = true;
                if(HourOfDay<8||HourOfDay>20){
                    time_btn.setError("กรุณาเลือกเวลารับใหม่");
                    boo_time = false;
                }
                String locat = location_btn.getText().toString();
                if(locat == "สถานที่รับ"){
                    location_btn.setError("กรุณาเลือกสถานที่รับใหม่");
                    boo_time = false;
                }
                if(boo_loc&&boo_time){
                    if(MinuteOfDay<10){
                        Time_format = HourOfDay+":0"+MinuteOfDay;
                    }else{
                        Time_format = HourOfDay+":"+MinuteOfDay;
                    }
                    location = locat;
                    loc_detail = loc_edit.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Context);
                    builder
                            .setTitle("ยืนยันการสั่งออเดอร์")
                            .setMessage("กรุณาตรวจสอบให้แน่ใจ เพราะเมื่อท่านยืนยันแล้วจะไม่สามารถแก้ไขได้อีก")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    formBody = new FormBody.Builder()
                                            .add("cus_tel", app_config.user_tel)
                                            .add("order_lat", "13.869496803814199")
                                            .add("order_long", "100.55009834468365")
                                            .add("order_time_recive", Time_format)
                                            .add("order_location", location)
                                            .add("order_location_detail", loc_detail)
                                            .build();

                                    String url = app_config.PostcomfrimOrder;
                                    new AsyncHttpTask().execute(url);
                                }
                            })
                            .setNegativeButton("ยกเลิก", null)						//Do nothing on no
                            .show();
                }
            }
        });
    }
    private ArrayList<String> getItems()
    {
        ArrayList<String> ret_val = new ArrayList<String>(Arrays.asList(app_config.getLoc_arr()));
        return ret_val;
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
    @Override
    public void onAttach(Activity activity) {
        activity_context=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public void onAttach(Context context) {
        this.Context= context;
        super.onAttach(context);
    }

    public class DialogRadio extends DialogFragment {
        public static final String DATA = "items";


        public static final String SELECTED = "selected";
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
                location_btn.setError(null);
                dialog.dismiss();
            }
        }
        DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // process
                location_btn.setText(""+getItems().get(which));
                location_btn.setError(null);
                dialog.dismiss();
            }

        };
    }
    ////////////////////////////////////////////////////////
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
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
            else if (result == "Black List"){
                return -1;
            }
            return 1;
        }
        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Toast.makeText(getActivity(), "ส่งรายการอาหารเรียบร้อย", Toast.LENGTH_LONG).show();
                FragmentManager manager = activity_context.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                menu_activity menu_fragment = new menu_activity();
                transaction.replace(R.id.layout_fragment_container, menu_fragment,"menu_tag");
                transaction.commit();
                transaction.disallowAddToBackStack();
            }else if (result == -1) {
                Toast.makeText(getActivity(), "ผู้ใช้ถูก Black List หากต้องการปลดสถานะนี้ กรุณาติดต่อเรา", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(),"ตรวจสอบการเชื่อมต่อ",Toast.LENGTH_LONG).show();
            }
        }
    }
}
