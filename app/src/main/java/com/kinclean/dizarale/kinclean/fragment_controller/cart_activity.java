package com.kinclean.dizarale.kinclean.fragment_controller;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kinclean.dizarale.kinclean.R;
import com.kinclean.dizarale.kinclean.cart_RecycleView_Adapter;
import com.kinclean.dizarale.kinclean.helper.SimpleItemTouchHelperCallback;
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
 * {@link cart_activity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link cart_activity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cart_activity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private APP_config config = new APP_config();
    private static final String TAG = "cart_fragment";
    private RecyclerView cart_RecyclerView;
    private List<menu_model> menu_model_List;
    private cart_RecycleView_Adapter adapter;
    private ProgressBar progressBar;
    private FragmentActivity context;
    private Context Context;

    private FragmentManager manager;
    private int totle_cost =0;

    protected Button submit;




    private ItemTouchHelper.Callback callback;

    private ItemTouchHelper itemTouchHelper;

    public cart_activity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cart_activity.
     */
    // TODO: Rename and change types and number of parameters
    public static cart_activity newInstance(String param1, String param2) {
        cart_activity fragment = new cart_activity();
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
        return inflater.inflate(R.layout.fragment_cart_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        cart_RecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        cart_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final String url = config.GetpPreorder;
        new AsyncHttpTask().execute(url);

        submit = (Button) view.findViewById(R.id.send_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_activity location_Fragment = new location_activity();

                Slide slideTransition = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    slideTransition = new Slide(Gravity.RIGHT);
                    slideTransition.setDuration(500);
                }
                location_Fragment.setEnterTransition(slideTransition);
                manager = context.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.layout_fragment_container, location_Fragment, "location");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Snackbar.make(view, "เลื่อนไปด้านข้างเพื่อลบรายการอาหาร", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

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
        super.onAttach(context);
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
            Log.v("resilt:",result);
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
                adapter = new cart_RecycleView_Adapter(getActivity().getApplication(), menu_model_List , context.getSupportFragmentManager());
                callback = new SimpleItemTouchHelperCallback(adapter);
                itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(cart_RecyclerView);
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);

                       submit.setText("รวม ฿" + cal_cost(menu_model_List));
                    }
                });
                cart_RecyclerView.setAdapter(adapter);
                submit.setText("รวม ฿" + totle_cost);
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
                item.setDes(post.optString("menu_description"));
                item.setNum(post.optString("menu_num"));
                totle_cost += Integer.parseInt(post.optString("menu_num"))*Integer.parseInt(post.optString("menu_cost"));
                menu_model_List.add(item);
                Log.v("res_4", post.optString("menu_num"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private int cal_cost(List<menu_model> menu_model_List){
        int cost=0;
        for(int i=0;i<menu_model_List.size();i++){
            cost += Integer.parseInt(menu_model_List.get(i).getPrice())*Integer.parseInt(menu_model_List.get(i).getNum());
        }
        return cost;
    }
}
