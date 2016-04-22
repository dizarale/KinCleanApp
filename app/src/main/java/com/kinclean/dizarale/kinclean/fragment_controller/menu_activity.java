package com.kinclean.dizarale.kinclean.fragment_controller;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kinclean.dizarale.kinclean.R;
import com.kinclean.dizarale.kinclean.menu.menu_RecycleView_Adapter;
import com.kinclean.dizarale.kinclean.menu.menu_model;
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
 * {@link menu_activity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link menu_activity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menu_activity extends Fragment {
    private APP_config config = new APP_config();
    private static final String TAG = "menu_fragment";
    private RecyclerView menu_RecyclerView;
    private List<menu_model> menu_model_List;
    private menu_RecycleView_Adapter adapter;
    private ProgressBar progressBar;
    private FragmentActivity  context;
    private Context Context;
    private View view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public menu_activity() {
    }
    public static menu_activity newInstance(String param1, String param2) {
        menu_activity fragment = new menu_activity();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        menu_RecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        menu_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String url = config.GetAllMenu;
        new AsyncHttpTask().execute(url);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        context=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public void onAttach(Context context) {
        Context= context;
        super.onAttach(Context);
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
                adapter = new menu_RecycleView_Adapter(getActivity().getApplication(), menu_model_List , context.getSupportFragmentManager());
                menu_RecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "กรุณาตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }

        public void setProgressBarIndeterminateVisibility(boolean progressBarIndeterminateVisibility) {
            //this.progressBarIndeterminateVisibility = progressBarIndeterminateVisibility;
        }

    }

    private void parseResult(String result) {
        try {
            //JSONObject response = new JSONObject(result);
            //JSONArray posts = response.optJSONArray("posts");
            JSONArray posts = new JSONArray(result);
            menu_model_List = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                menu_model item = new menu_model();
                item.setTitle(post.optString("menu_name"));
                item.setTitle_thai(post.optString("menu_name_thai"));
                item.setPrice(post.optString("menu_cost"));
                item.setThumbnail(post.optString("menu_pic"));
                item.setid(post.optString("menu_id"));
                item.setDes(post.optString("menu_detail"));

                menu_model_List.add(item);
                //Log.v("res_4",feedsList.toString() + "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
