package com.cmora.froglab_2.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.R;

public class FamilyViewHolder extends RecyclerView.ViewHolder {
    public final int CARD_ELEVATION = 8;
    public final int SELECTED_CARD_ELEVATION = 20;
    public TextView fam_name, frog1_name, frog2_name;
    public ImageView icon, icon2, expanded_state;
    public int type = FamilyAdapter.TYPE;
    public CardView card;
    public int id;
    FamilyViewHolder(View itemView) {
        super(itemView);
        fam_name = itemView.findViewById(R.id.family_name);
        frog1_name = itemView.findViewById(R.id.frog1_name);
        frog2_name = itemView.findViewById(R.id.frog2_name);
        icon = itemView.findViewById(R.id.frog1_image);
        icon2 = itemView.findViewById(R.id.frog2_image);
        expanded_state = itemView.findViewById(R.id.expand_icon);
        card = (CardView)itemView;
        card.setCardElevation(CARD_ELEVATION);
    }
}
