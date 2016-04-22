package com.kinclean.dizarale.kinclean.histoty;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;
import java.util.UUID;

/**
 * Created by dizar on 2/26/2016.
 */
public class History implements ParentListItem {

    private UUID mId;
    private String order_id;
    private String order_cost;
    private String order_location;
    private String order_status;
    private String order_time;

    public History() {
        mId = UUID.randomUUID();
    }
    public UUID getId() {
        return mId;
    }
    public String getOrderID() {
        return order_id;
    }
    public String getOrderCost() {
        return order_cost;
    }
    public String getOrderLocation() {
        return order_location;
    }
    public String getOrderStatus() {
        return order_status;
    }
    public String getOrderTime() {
        return order_time;
    }

    public void setOrderID(String order_id) {
       this.order_id = order_id;
    }
    public void setOrderCost(String order_cost) {
        this.order_cost = order_cost;
    }
    public void setOrderLocation(String order_location) {
        this.order_location = order_location;
    }
    public void setOrderStatus(String order_status) {
        this.order_status = order_status;
    }
    public void setOrderTime(String order_time) {
        this.order_time = order_time;
    }

    private List<HistoryChild> mChildItemList;

    @Override
    public List<?> getChildItemList() {
        return mChildItemList;
    }
    public void setChildItemList(List<HistoryChild> list) {
        mChildItemList = list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
