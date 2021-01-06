package com.cmora.froglab_2.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.R;
import com.cmora.froglab_2.adapters.ElementAdapter;
import com.cmora.froglab_2.adapters.ElementListAdapter;
import com.cmora.froglab_2.adapters.IndividualAdapter;
import com.cmora.froglab_2.genetics.Individual;

import java.util.List;

public class ElementListAdapterDatabase extends
        ElementListAdapter {
    public ElementListAdapterDatabase(Context context, List<ElementAdapter> lista, List<LayerDrawable> drawables) {
            super(context, lista, drawables);
        }

        public int getItemViewType(int position){
            return lista.get(position).getType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("ELEMENT_LIST_ADAPTER", "onCreateViewHolder");
            View v3 = inflador.inflate(R.layout.individual_cardview, parent, false);
            return new IndividualViewHolderDatabase(v3);
        }
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {
            Log.d("ELEMENT_LIST_ADAPTER", "onBindViewHolder");
            ElementAdapter el = lista.get(i);
            int type = el.getType();
            IndividualViewHolderDatabase vh2 = (IndividualViewHolderDatabase)holder;
            vh2.titulo.setText(context.getString(R.string.individual_database) + " " + el.getImageFooter());
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.context);
            if(pref.getBoolean("cheating", false)){
                vh2.subtitulo.setText(context.getString(R.string.phenotype) + el.getExtraInfo1() + "\n" +
                        context.getString(R.string.genotype) + el.getExtraInfo2());
            }else {
                vh2.subtitulo.setText(context.getString(R.string.phenotype) + el.getExtraInfo1());
            }
            vh2.icon.setImageDrawable(this.drawables.get(el.getLayerDrawableInd()));
            vh2.id = el.getId();
            if(el.isSelected()) {
                vh2.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorIndividualCardSelected));
                vh2.card.setCardElevation(20);
            }
            else {
                vh2.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorIndividualCardDatabase));
                vh2.card.setCardElevation(8);
            }
        }

        @Override
        public void saveImage(View view){
            String id = (String) view.getTag();
            for(ElementAdapter el : lista){
                if(el.getType() == IndividualAdapter.TYPE && Integer.toString(el.getId()).equals(id)){
                    Individual ind = (Individual)el.getObject();
                    Log.d("ElementListAdapter", "Saving Image, ItemFound: " + ind.toString());
                }
            }
            Toast.makeText(context, "Saving Image"+ id, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void saveFrog(View view){
            //This is not allowed here
        }

    }
