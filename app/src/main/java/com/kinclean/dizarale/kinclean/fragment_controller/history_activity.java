package com.kinclean.dizarale.kinclean.fragment_controller;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.kinclean.dizarale.kinclean.histoty.History;
import com.kinclean.dizarale.kinclean.histoty.HistoryChild;
import com.kinclean.dizarale.kinclean.histoty.HistoryExpandableAdapter;
import com.kinclean.dizarale.kinclean.R;
import com.kinclean.dizarale.kinclean.service.APP_config;
import com.kinclean.dizarale.kinclean.service.HTTPReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link history_activity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link history_activity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class history_activity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentActivity Context;
    Context context;
    private ProgressBar progressBar;
    HistoryExpandableAdapter historyExpandableAdapter;

    List<ParentListItem> AllHistory;
    APP_config app_config = new APP_config();

    private RecyclerView HistoryRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public history_activity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment history_activity.
     */
    // TODO: Rename and change types and number of parameters
    public static history_activity newInstance(String param1, String param2) {
        history_activity fragment = new history_activity();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_activity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        HistoryRecyclerView = (RecyclerView) view.findViewById(R.id.history_recycler_view);
        HistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        HistoryRecyclerView.setAdapter(historyExpandableAdapter);
        final String url = app_config.GetOrderUer;
        new AsyncHttpTask().execute(url);

    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((HistoryExpandableAdapter) HistoryRecyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.Context = (FragmentActivity)context;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            HTTPReq getData = new HTTPReq();
            result = getData.HTTPGET(url);
            Log.v("result",result.toString());
            if(result == "Can not Get Data"){
                return 0;
            }
            parseResult(result);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                historyExpandableAdapter = new HistoryExpandableAdapter(getActivity(), AllHistory);
                HistoryRecyclerView.setAdapter(historyExpandableAdapter);
            } else {
                Toast.makeText(getActivity(), "กรุณาตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }

        public void setProgressBarIndeterminateVisibility(boolean progressBarIndeterminateVisibility) {
            //this.progressBarIndeterminateVisibility = progressBarIndeterminateVisibility;
        }

        private void parseResult(String result) {
            try {
                //JSONObject response = new JSONObject(result);
                //JSONArray posts = response.optJSONArray("posts");
                JSONArray posts = new JSONArray(result);
                AllHistory = new ArrayList<>();
                List<History> histories;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    History item = new History();
                    histories = new ArrayList<>();


                    item.setOrderID(post.optString("order_id"));
                    item.setOrderLocation(post.optString("order_local_detail"));
                    item.setOrderCost(post.optString("order_cost"));
                    Log.v("order_status" ,post.optString("order_status") );
                    int order_s = Integer.parseInt(post.optString("order_status"));
                    switch (order_s){
                        case 1 : item.setOrderStatus("รอการตอบรับ"); break;
                        case 2 : item.setOrderStatus("รอการจัดส่ง"); break;
                        case 3 : item.setOrderStatus("รอการจัดส่ง"); break;
                        case 5 : item.setOrderStatus("จัดส่งเรียบร้อย"); break;
                        case 9 : item.setOrderStatus("ไม่มีผู้รับ"); break;
                            default:item.setOrderStatus(post.optString("order_status"));
                    }
                    item.setOrderTime(post.optString("order_time_final"));
                    histories.add(item);
                    JSONArray menus  = new JSONArray(post.optString("menu"));
                    List<HistoryChild> childItemList = new ArrayList<>();
                    for (int j=0 ; j < menus.length()-3 ; j++){
                        JSONObject menu = menus.optJSONObject(j);
                        childItemList.add(new HistoryChild(menu.optString("menu_name")+"("+menu.optString("menu_num")+")", menu.optString("menu_cost")));
                        item.setChildItemList(childItemList);
                    }
                    AllHistory.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
