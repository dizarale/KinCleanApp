package com.kinclean.dizarale.kinclean.menu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.kinclean.dizarale.kinclean.R;
import com.kinclean.dizarale.kinclean.fragment_controller.menu_show_activity;
import com.kinclean.dizarale.kinclean.service.APP_config;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by dizar on 2/1/2016.
 */
public class menu_RecycleView_Adapter extends RecyclerView.Adapter<menu_RecycleView_Adapter.menu_cards> {
    private List<menu_model> menu_modelList;
    private Context Context;
    FragmentManager fragmentManager ;
    Button submit;
    View cartView;
    public menu_RecycleView_Adapter(Context context, List<menu_model> menu_modelList, FragmentManager fragmentManager){
        this.menu_modelList = menu_modelList;
        this.Context = context;
        this.fragmentManager = fragmentManager;
    }
    @Override
    public menu_cards onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_menu, null);
        menu_cards viewHolder = new menu_cards(Context,view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(menu_cards holder, int position) {
        menu_model feedItem = menu_modelList.get(position);

        Picasso.with(Context).load(feedItem.getThumbnail())
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.imageView);

        holder.menu_name.setText(feedItem.getTitle());
        holder.menu_price.setText("฿"+feedItem.getPrice());


        holder.menu_name.setTag(holder);
        holder.menu_price.setTag(holder);
        holder.imageView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return (null != menu_modelList ? menu_modelList.size() : 0);
    }



    public class menu_cards extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView imageView;
        protected TextView menu_name;
        protected TextView menu_price;
        private Context context;

        public menu_cards(Context context,View view) {
            super(view);
            this.context = context;
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.menu_name = (TextView) view.findViewById(R.id.title);
            this.menu_price = (TextView) view.findViewById(R.id.price);
            view.setOnClickListener(this);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            menu_model menu_detail =  menu_modelList.get(position);
            Log.v("data",menu_detail.toString());
            //menu_detail = menu_modelList.get(position);

            Parcelable wrapped = Parcels.wrap(menu_detail);

            Bundle bundle = new Bundle();
            bundle.putParcelable("menu_detail", wrapped);

            menu_show_activity menu_detail_Fragment = new menu_show_activity();
            menu_detail_Fragment.setArguments(bundle);


            if(new APP_config().getSdkVersion() > 20){
                Slide slideTransition = new Slide(Gravity.RIGHT);
                slideTransition.setDuration(500);
                menu_detail_Fragment.setEnterTransition(slideTransition);
            }


            FragmentManager manager = fragmentManager;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.layout_fragment_container, menu_detail_Fragment, "menu_detail");
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

}
