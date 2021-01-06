package com.cmora.froglab_2.adapters;

import androidx.recyclerview.widget.RecyclerView;

public interface ElementAdapter {
    String getImageFooter();
    String getExtraInfo1();
    String getExtraInfo2();
    RecyclerView.ViewHolder getViewHolder();
    int getLayerDrawableInd();
    boolean hasNext();
    int getId();
    int getType();
    boolean isExpanded();
    void setExpanded();
    boolean isSelected();
    void select();
    void unselect();
    Object getObject();
}
