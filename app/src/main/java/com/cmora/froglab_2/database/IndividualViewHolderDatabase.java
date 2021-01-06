package com.cmora.froglab_2.database;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.R;
import com.cmora.froglab_2.adapters.IndividualAdapter;


public class IndividualViewHolderDatabase extends RecyclerView.ViewHolder{
    public final int CARD_ELEVATION = 8;
    public final int SELECTED_CARD_ELEVATION = 20;
    public TextView titulo, subtitulo;
    public ImageView icon;
    public CardView card;
    public int type = IndividualAdapter.TYPE;
    public int id;
    ImageButton save_image_button;
    ImageButton save_frog_button;
    IndividualViewHolderDatabase(View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.titulo);
        subtitulo = itemView.findViewById(R.id.subtitulo);
        icon = itemView.findViewById(R.id.icono);
        card = (CardView)itemView;
        card.setCardElevation(CARD_ELEVATION);
    }
    public void setTags(){
        Log.d("IndividualViewHolder: ", "setTags from ElementListAdapter, id:" + id);
        save_image_button.setTag(Integer.toString(id));
        save_frog_button.setTag(Integer.toString(id));
    }
}