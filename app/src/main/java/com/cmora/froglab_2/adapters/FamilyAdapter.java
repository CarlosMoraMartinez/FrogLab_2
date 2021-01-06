package com.cmora.froglab_2.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.genetics.Family;

public class FamilyAdapter implements ElementAdapter {
    public static final int TYPE = 2;
    protected Family object;
    protected static final int NUM_DRAWABLES = 2;
    protected int drawables_released = 0;
    protected int names_released = 0;
    protected int female_representation, male_representation;
    protected boolean expanded;
    public FamilyAdapter(Family family, int female_repr, int male_repr) {
        this.object = family;
        this.female_representation = female_repr;
        this.male_representation = male_repr;
        expanded = true;
    }

    @Override
    public String getImageFooter() {
        if(this.names_released == 0) {
            names_released++;
            return this.object.getMaleNameAsString();
        }
        names_released = 0;
        return this.object.getFemaleNameAsString();
    }

    @Override
    public boolean isExpanded(){
        return this.expanded;
    }

    @Override
    public void setExpanded(){
        this.expanded = !expanded;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return null;
    }

    public String getExtraInfo() {
        return this.object.getNameAsString();
    }

    @Override
    public int getLayerDrawableInd() {
        if(this.drawables_released == 0){
            drawables_released +=1;
            return this.male_representation;
        }
        drawables_released = 0;
        return this.female_representation;
    }

    @Override
    public String getExtraInfo1() {
        return getExtraInfo();
    }

    @Override
    public String getExtraInfo2() {
        return getExtraInfo();
    }

    @Override
    public int getId() {
        return this.object.getId();
    }

    @Override
    public int getType() {
        return this.TYPE;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void select() {

    }

    @Override
    public void unselect() {

    }

    @Override
    public boolean hasNext() {
        if(this.drawables_released < 2)
            return true;
        return false;
    }

    @Override
    public Family getObject() {
        return this.object;
    }
}
