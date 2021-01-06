package com.cmora.froglab_2.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.genetics.Individual;

public class IndividualAdapter implements ElementAdapter {
    public static final int TYPE = 1;
    protected Individual object;
    protected int representation;
    protected boolean selected = false;
    public IndividualAdapter(Individual individual, int repr){
        this.object = individual;
        this.representation = repr;
    }

    @Override
    public String getImageFooter() {
        return this.object.getNameAsString2();
    }

    @Override
    public String getExtraInfo1() {
        return this.object.getAllPhenotypes();
    }
    @Override
    public String getExtraInfo2(){
        return this.object.getAllGenotypes();
    }
    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return null;
    }

    @Override
    public int getType() {
        return this.TYPE;
    }
    @Override
    public int getId() {
        return this.object.getId();
    }

    @Override
    public int getLayerDrawableInd() {
        return this.representation;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean isExpanded(){
        return true;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public void unselect() {
        selected = false;
    }

    @Override
    public void setExpanded(){
    }
    @Override
    public Individual getObject() {
        return this.object;
    }
}

