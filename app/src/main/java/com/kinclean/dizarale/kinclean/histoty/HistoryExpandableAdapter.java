package com.kinclean.dizarale.kinclean.histoty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.kinclean.dizarale.kinclean.R;

import java.util.List;

/**
 * Created by dizar on 2/26/2016.
 */
public class HistoryExpandableAdapter extends ExpandableRecyclerAdapter<HistoryParentViewHolder, HistoryChildViewHolder> {

    private LayoutInflater  mInflater;
    public HistoryExpandableAdapter(Context context, List<ParentListItem> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {

        View view = mInflater.inflate(R.layout.card_history, parentViewGroup, false);
        return new HistoryParentViewHolder(view);
    }

    @Override
    public HistoryChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflater.inflate(R.layout.card_history_expand, childViewGroup, false);
        return new HistoryChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(HistoryParentViewHolder ParentViewHolder, int i, ParentListItem parentListItem) {
        History history = (History) parentListItem;
        ParentViewHolder.order_id.setText(history.getOrderID());
        ParentViewHolder.order_cost.setText(history.getOrderCost());
        ParentViewHolder.location.setText(history.getOrderLocation());
        ParentViewHolder.order_status.setText(history.getOrderStatus());
        ParentViewHolder.order_time.setText(history.getOrderTime());

    }

    @Override
    public void onBindChildViewHolder(HistoryChildViewHolder childViewHolder, int position, Object childListItem) {
        HistoryChild historyChild = (HistoryChild) childListItem;
        childViewHolder.menu_name.setText(historyChild.getMenuName().toString());
        childViewHolder.menu_cost.setText(historyChild.getMenuCost().toString());
    }
}
