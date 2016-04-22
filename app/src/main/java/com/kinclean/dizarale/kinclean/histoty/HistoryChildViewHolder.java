package com.kinclean.dizarale.kinclean.histoty;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.kinclean.dizarale.kinclean.R;

/**
 * Created by dizar on 2/26/2016.
 */
public class HistoryChildViewHolder extends ChildViewHolder {
    public TextView menu_name;
    public TextView menu_cost;

    public HistoryChildViewHolder(View itemView) {
        super(itemView);

        menu_name = (TextView) itemView.findViewById(R.id.menu_name);
        menu_cost = (TextView) itemView.findViewById(R.id.menu_cost);
    }
}
