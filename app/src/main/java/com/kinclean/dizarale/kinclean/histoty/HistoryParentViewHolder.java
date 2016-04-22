package com.kinclean.dizarale.kinclean.histoty;

import android.view.View;

import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.kinclean.dizarale.kinclean.R;

/**
 * Created by dizar on 2/26/2016.
 */
public class HistoryParentViewHolder extends ParentViewHolder {


    public TextView order_id;
    public TextView location;
    public TextView order_cost;
    public TextView order_status;
    public TextView order_time;
    
    public HistoryParentViewHolder(View itemView) {
        super(itemView);
        order_id = (TextView) itemView.findViewById(R.id.order_id);
        location = (TextView) itemView.findViewById(R.id.order_loc);
        order_cost = (TextView) itemView.findViewById(R.id.order_cost);
        order_status = (TextView) itemView.findViewById(R.id.order_status);
        order_time = (TextView) itemView.findViewById(R.id.order_time);
    }
}
