package com.cmora.froglab_2.database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.R;
import com.cmora.froglab_2.adapters.ElementAdapter;
import com.cmora.froglab_2.adapters.IndividualAdapter;
import com.cmora.froglab_2.adapters.LayerDrawableBuilder;
import com.cmora.froglab_2.adapters.RecyclerItemClickListener;
import com.cmora.froglab_2.genetics.Individual;
import com.cmora.froglab_2.genetics.ProblemInstance;
import com.cmora.froglab_2.genetics.ProblemType;
import com.cmora.froglab_2.genetics.Sex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ElementListAdapterDatabase adaptador;
    private FrogDatabase frogdb;
    private ProblemType problem_type;
    ProblemInstance prob;
    List<ElementAdapter> elements = new ArrayList<>();
    List<Individual> individuals = new ArrayList<>();
    List<LayerDrawable> drawables = new ArrayList<>();
    private int male_selected = -1;
    private int female_selected = -1;
    private String genome_selected = "";
    private final String PROBLEM_TYPE = "problemtypemap";
    String DEFAULT_GENOME = "genome1";
    //Context context;
    SoundPool soundPool;
    int croack_male, croack_female;
    private Individual male_obj, female_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    setContentView(R.layout.database);
    Log.d("DATABASE activity", "onCreate 1");
    this.soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    croack_male = soundPool.load(this, R.raw.croack_male, 0);
    croack_female = soundPool.load(this, R.raw.croack_female, 0);
    frogdb = new FrogDatabase(this);

    recyclerView = findViewById(R.id.recyclerView);
    adaptador = this.getList();
    recyclerView.setAdapter(adaptador);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    Log.d("LISTENER DATABASE", "onItemClick");
                    ElementAdapter e = elements.get(position);
                    Toast.makeText(view.getContext(), "Selected " + e.getImageFooter(), Toast.LENGTH_LONG).show();
                    onIndividualSelected(e);
                }
                @Override public void onLongItemClick(View view, int position) {
                    Log.d("LISTENER DATABASE", "onLongItemClick");
                    ElementAdapter e = elements.get(position);
                    Log.d("LISTENER DATABASE", "onLongItemClick 2" + e.getImageFooter());
                    //Toast.makeText(view.getContext(), "TOUCHED LONG: " + e.getImageFooter() + e.getExtraInfo1(), Toast.LENGTH_LONG).show();
                }
            })
        );
        makeDatabaseStartAlert();
        Log.d("DATABASE activity", "onCreate 2");
    }

    public void onIndividualSelected(ElementAdapter e){
        Log.d("DATABASE", "onIndividualSelected start");
        Individual ind = (Individual)e.getObject();

        if(ind.getSex() == Sex.MALE){
            if(male_selected == ind.getId()){
                unselectIndividual(male_selected);
                male_selected = -1;
                return;
            }else if(male_selected != -1) {
                unselectIndividual(male_selected);
            }
            soundPool.play(croack_male, 1, 1, 1, 0, 1);
            male_selected = ind.getId();
            selectIndividual(male_selected);
            if(! ind.getGenomeName().equals(genome_selected)){
                if(female_selected != -1) {
                    unselectIndividual(female_selected);
                    female_selected = -1;
                }
                genome_selected = ind.getGenomeName();
            }else if(female_selected != -1){
                unselectIndividual(female_selected);
                unselectIndividual(male_selected);
                startGame();
                male_selected = -1;
                female_selected = -1;
                genome_selected = "";
            }
        }else{
            if(female_selected == ind.getId()){
                unselectIndividual(female_selected);
                female_selected = -1;
                return;
            }else if(female_selected != -1) {
                unselectIndividual(female_selected);
            }
            if(soundPool == null){
                Log.d("DATABASE", "sOUNDPOOL NULL");
            }else{
                Log.d("DATABASE", String.valueOf(croack_female));
            }

            soundPool.play(croack_female, 1, 1, 1, 0, 1);
            female_selected = ind.getId();
            selectIndividual(female_selected);
            if(! ind.getGenomeName().equals(genome_selected)){
                if(male_selected != -1) {
                    unselectIndividual(male_selected);
                    male_selected = -1;
                }
                genome_selected = ind.getGenomeName();
            }else if(male_selected != -1){
                unselectIndividual(female_selected);
                unselectIndividual(male_selected);
                startGame();
                male_selected = -1;
                female_selected = -1;
                genome_selected = "";
            }
        }
        Log.d("DATABASE", "onIndividualSelected end");
    }
    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("READING", e.getMessage());
        }
        return outputStream.toString();
    }
    public ElementListAdapterDatabase getList(){
        individuals = frogdb.getAllFrogs();
        elements = new ArrayList<>(individuals.size());
        drawables = new ArrayList<>(individuals.size());
        for(int i = 0; i < individuals.size(); i++){
            individuals.get(i).setId(i);
            elements.add(i, new IndividualAdapterDatabase(individuals.get(i), i));
            drawables.add(i, LayerDrawableBuilder.getLayerDrawable(this, individuals.get(i)));
        }
        return new ElementListAdapterDatabase(this, elements, drawables);
    }
    public int findAdapterElement(int id, int type) {
        for(int i = 0; i < elements.size(); i++) {
            if(elements.get(i).getId() == id && elements.get(i).getType() == type)
                return i;
        }
        return -1;
    }
    public void selectIndividual(int ind){
        int element_position = findAdapterElement(ind, IndividualAdapter.TYPE);
        elements.get(element_position).select();
        adaptador.notifyItemChanged(element_position);
    }
    public void unselectIndividual(int ind){
        int element_position = findAdapterElement(ind, IndividualAdapter.TYPE);
        elements.get(element_position).unselect();
        adaptador.notifyItemChanged(element_position);
    }

    public void startGame(){
        female_obj = this.individuals.get(female_selected);
        male_obj = this.individuals.get(male_selected);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("genome_name",female_obj.getGenomeName());
        resultIntent.putExtra("female_hap1", FrogDatabase.haplotypeToString(female_obj.getHaplotype1()));
        resultIntent.putExtra("female_hap2", FrogDatabase.haplotypeToString(female_obj.getHaplotype2()));
        resultIntent.putExtra("male_hap1", FrogDatabase.haplotypeToString(male_obj.getHaplotype1()));
        resultIntent.putExtra("male_hap2", FrogDatabase.haplotypeToString(male_obj.getHaplotype2()));
        setResult(1, resultIntent);
        makeStartGameAlert();
    }
    public void makeDatabaseStartAlert(){
        AlertDialog.Builder alertName = new AlertDialog.Builder(this);
        //final EditText editTextName = new EditText(this);

        Log.d("ElementListAdapter", "makeSaveAlert 1");
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.database_prompt1, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                               dialog.dismiss();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Log.d("ElementListAdapter", "makeSaveAlert 2");
    }
    public void makeStartGameAlert(){
        AlertDialog.Builder alertName = new AlertDialog.Builder(this);
        //final EditText editTextName = new EditText(this);

        Log.d("ElementListAdapter", "makeSaveAlert 1");
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.database_prompt1, null);
        TextView tx = promptsView.findViewById(R.id.textView_dbprompt);
        tx.setText(R.string.database_prompt2);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                                finish();
                            }
                        }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Log.d("ElementListAdapter", "makeSaveAlert 2");
    }

}
