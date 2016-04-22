package com.kinclean.dizarale.kinclean;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kinclean.dizarale.kinclean.helper.ItemTouchHelperAdapter;
import com.kinclean.dizarale.kinclean.menu.menu_model;
import com.kinclean.dizarale.kinclean.service.APP_config;
import com.kinclean.dizarale.kinclean.service.HTTPReq;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by dizar on 2/1/2016.
 */
public class cart_RecycleView_Adapter extends RecyclerView.Adapter<cart_RecycleView_Adapter.cart_cards> implements ItemTouchHelperAdapter {
    private List<menu_model> menu_modelList;
    private Context Context;
    FragmentManager fragmentManager;

    private APP_config app_config =new APP_config();
    RequestBody formBody;


    public cart_RecycleView_Adapter(Context context, List<menu_model> menu_modelList, FragmentManager fragmentManager){
        this.menu_modelList = menu_modelList;
        this.Context = context;
        this.fragmentManager = fragmentManager;
    }
    @Override
    public cart_cards onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_cart, null);

        cart_cards viewHolder = new cart_cards(Context,view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(cart_cards holder, int position) {
        menu_model feedItem = menu_modelList.get(position);
        Picasso.with(Context).load(feedItem.getThumbnail())
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.imageView);

        int num = Integer.parseInt(feedItem.getNum());
        int pricr = Integer.parseInt(feedItem.getPrice());
        int totle = num*pricr;
        holder.menu_name.setText(feedItem.getTitle());
        holder.menu_price.setText("฿" + feedItem.getPrice());
        holder.menu_num.setText("จำนวน:"+feedItem.getNum());
        holder.menu_totle.setText("รวม:" + totle);


        holder.menu_name.setTag(holder);
        holder.menu_price.setTag(holder);
        holder.imageView.setTag(holder);
        holder.menu_num.setTag(holder);
        holder.menu_totle.setTag(holder);

    }

    @Override
    public int getItemCount() {
        return (null != menu_modelList ? menu_modelList.size() : 0);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {
        String url = app_config.DelectPreorder;
        formBody = new FormBody.Builder()
                .add("cus_tel", app_config.user_tel)
                .add("menu_id", menu_modelList.get(position).getid())
                .build();
        new AsyncHttpTask().execute(url);
        menu_modelList.remove(position);
        notifyItemRemoved(position);
    }


    public class cart_cards extends RecyclerView.ViewHolder{
        protected ImageView imageView;
        protected TextView menu_name;
        protected TextView menu_price;
        protected TextView menu_num;
        protected TextView menu_totle;
        private Context context;
        private View view;

        public cart_cards(Context context,View view) {
            super(view);
            this.context = context;
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.menu_name = (TextView) view.findViewById(R.id.menu_name);
            this.menu_price = (TextView) view.findViewById(R.id.menu_price);
            this.menu_num = (TextView) view.findViewById(R.id.menu_num);
            this.menu_totle = (TextView) view.findViewById(R.id.menu_totle);
        }
    }

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
            return 1;
        }
        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Toast.makeText(Context, "ลบรายการเรียบร้อย", Toast.LENGTH_LONG).show();
                Log.v("result", "succ");
            } else {
                Toast.makeText(Context,"กรุณาตรวจสอบการเชื่อมต่อ",Toast.LENGTH_LONG).show();
            }
        }
    }
}
