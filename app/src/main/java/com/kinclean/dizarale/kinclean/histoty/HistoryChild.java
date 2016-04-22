package com.kinclean.dizarale.kinclean.histoty;

/**
 * Created by dizar on 2/26/2016.
 */
public class HistoryChild {
    private String menu_name;
    private String menu_cost;

    public HistoryChild(String menu_name, String menu_cost) {
        this.menu_name = menu_name;
        this.menu_cost = menu_cost;
    }

    public String getMenuName() {
        return menu_name;
    }

    public void setMenuName(String menu_name) {
        this.menu_name = menu_name;
    }
    public String getMenuCost() {
        return menu_cost;
    }

    public void setMenuCost(String menu_cost) {
        this.menu_cost = menu_cost;
    }


}
