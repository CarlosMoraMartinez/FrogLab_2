package com.cmora.froglab_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.adapters.ElementAdapter;
import com.cmora.froglab_2.adapters.ElementListAdapter;
import com.cmora.froglab_2.adapters.FamilyAdapter;
import com.cmora.froglab_2.adapters.IndividualAdapter;
import com.cmora.froglab_2.adapters.LayerDrawableBuilder;
import com.cmora.froglab_2.adapters.RecyclerItemClickListener;
import com.cmora.froglab_2.genetics.Family;
import com.cmora.froglab_2.genetics.GenomeInitializer;
import com.cmora.froglab_2.genetics.GenomeModel;
import com.cmora.froglab_2.genetics.Individual;
import com.cmora.froglab_2.genetics.ProblemInstance;
import com.cmora.froglab_2.genetics.ProblemType;
import com.cmora.froglab_2.genetics.ProblemTypeInitializer;
import com.cmora.froglab_2.genetics.Sex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import kotlin.random.Random;

public class Juego extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ElementListAdapter adaptador;
    private GenomeModel genome;
    //private List<ProblemType> problems;
    private ProblemType problem_type;
    ProblemInstance prob;
    List<ElementAdapter> elements = new ArrayList<>();
    List<LayerDrawable> drawables = new ArrayList<>();
    private int last_family_shown = 0;
    private int male_selected = -1;
    private int female_selected = -1;
    private int family_selected = -1;
    private final String[] all_problemtypes = {"problemtype1","problemtype2","problemtype3","problemtype4","problemtype5", "problemtype6","problemtypemap"};
    private final String[] level1_problemtypes = {"problemtype1","problemtype2","problemtype3"};
    private final String[] level2_problemtypes = {"problemtype1","problemtype2","problemtype3","problemtype4","problemtype5"};
    private final String[] level3_problemtypes = {"problemtype6"};
    private final String[] level4_problemtypes = {"problemtypemap"};
    private final String DEFAULT_LEVEL = "3";
    private ArrayList<Integer> db_female_hap1, db_female_hap2, db_male_hap1, db_male_hap2;
    private String db_genome_name;
    private String current_level;
    SoundPool soundPool;
    int croack_male, croack_female;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.juego);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        /*if (!sharedPreferences.getBoolean(
                MyOnboardingSupportFragment.COMPLETED_ONBOARDING_PREF_NAME, false)) {
            // The user hasn't seen the OnboardingSupportFragment yet, so show it
            startActivity(new Intent(this, OnboardingActivity.class));
        }*/

        soundPool = new SoundPool( 5, AudioManager.STREAM_MUSIC , 0);
        croack_male = soundPool.load(this, R.raw.croack_male, 0);
        croack_female = soundPool.load(this, R.raw.croack_female, 0);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("JUEGO", "getting current level");
        current_level = pref.getString("level", DEFAULT_LEVEL);
        Log.d("JUEGO", "Current Level: " + current_level);

        if(intent.getBooleanExtra("FROM_DATABASE", false)){
            Log.d("JUEGO", "Current Level: " + current_level);
            this.db_female_hap1 = stringToHaplotype(intent.getStringExtra("female_hap1"));
            this.db_female_hap2 = stringToHaplotype(intent.getStringExtra("female_hap2"));
            this.db_male_hap1  = stringToHaplotype(intent.getStringExtra("male_hap1"));
            this.db_male_hap2  = stringToHaplotype(intent.getStringExtra("male_hap2"));
            this.db_genome_name = intent.getStringExtra("genome_name");
            InputStream JSONFileInputStream = getResources().openRawResource(R.raw.genome1);
            this.genome = GenomeInitializer.Companion.buildGenomeFromJSON(this.readTextFile(JSONFileInputStream));
            startGameFromDatabase();
        }else {
            InputStream JSONFileInputStream2 = getResources().openRawResource(R.raw.genome1);
            this.genome = GenomeInitializer.Companion.buildGenomeFromJSON(this.readTextFile(JSONFileInputStream2));
            startGame();
        }
    }
    public void onIndividualSelected(ElementAdapter e){
        Log.d("JUEGO", "onIndividualSelected start");
        Individual ind = prob.getIndividuals().get(e.getId());

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
            if(ind.getFamily() != family_selected){
                if(female_selected != -1) {
                    unselectIndividual(female_selected);
                    female_selected = -1;
                }
                family_selected = ind.getFamily();
            }else if(female_selected != -1){
                unselectIndividual(female_selected);
                unselectIndividual(male_selected);
                prob.addGeneration(female_selected, male_selected);
                male_selected = -1;
                female_selected = -1;
                family_selected = -1;
                addFamilyToView(prob, adaptador);
            }
        }else{
            if(female_selected == ind.getId()){
                unselectIndividual(female_selected);
                female_selected = -1;
                return;
            }else if(female_selected != -1) {
                unselectIndividual(female_selected);
            }
            soundPool.play(croack_female, 1, 1, 1, 0, 1);
            female_selected = ind.getId();
            selectIndividual(female_selected);
            if(ind.getFamily() != family_selected){
                if(male_selected != -1) {
                    unselectIndividual(male_selected);
                    male_selected = -1;
                }
                family_selected = ind.getFamily();
            }else if(male_selected != -1){
                unselectIndividual(female_selected);
                unselectIndividual(male_selected);
                prob.addGeneration(female_selected, male_selected);
                male_selected = -1;
                female_selected = -1;
                family_selected = -1;
                addFamilyToView(prob, adaptador);
            }
        }
        Log.d("JUEGO", "onIndividualSelected end");
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
    public ElementListAdapter getList(ProblemInstance prob){
        elements = new ArrayList<>(prob.getFamilies().size() + prob.getIndividuals().size());
        drawables = new ArrayList<>(prob.getIndividuals().size());
        int i = 2;
        elements.add(0, new IndividualAdapter(prob.getFamilies().get(0).getFemale_parent(), prob.getFamilies().get(0).getFemaleId()));
        elements.add(1, new IndividualAdapter(prob.getFamilies().get(0).getMale_parent(), prob.getFamilies().get(0).getMaleId()));

        for(Family f : prob.getFamilies()){
            elements.add(i, new FamilyAdapter(f, f.getFemaleId(), f.getMaleId()));
            i++;
            for(Individual ind : f.getOffspring()){
                elements.add(i, new IndividualAdapter(ind, ind.getId()));
                i++;
            }
            last_family_shown = f.getId();
        }
        for(Individual ind: prob.getIndividuals()){
            drawables.add(ind.getId(), LayerDrawableBuilder.getLayerDrawable(this, ind));
        }
        return new ElementListAdapter(this, elements, drawables);
    }
    public void addFamilyToView(ProblemInstance prob, ElementListAdapter adapter){
        last_family_shown++;
        Log.d("JUEGO", "addFamilyToView - BEFORE: " + Integer.toString(this.elements.size()) + ", "+
                Integer.toString(adapter.getItemCount() ));
        //Get last family (already added)
        Family f = prob.getFamilies().get(last_family_shown);
        int last = this.elements.size();
        // Add family to element adapter list
        this.elements.add(new FamilyAdapter(f, f.getFemaleId(), f.getMaleId()));
        // Add offspring of family to adapter element list
        for(Individual i: f.getOffspring()){
            this.drawables.add(LayerDrawableBuilder.getLayerDrawable(this, i));
            elements.add(new IndividualAdapter(i, i.getId()));
        }
        //change view
        adapter.notifyDataSetChanged();
        Log.d("JUEGO", "addFamilyToView - AFTER: " + Integer.toString(this.elements.size()) + ", "+
                Integer.toString(adapter.getItemCount() ));
        //Collapse other families to simplify view
        for(int i = last - 1; i > 1; i--){
            if(elements.get(i).getType() == FamilyAdapter.TYPE && elements.get(i).isExpanded())
                collapseFamily(i, adapter);
        }
        //Get position of last family and move view to that position
        for(int i = 0; i < elements.size(); i++)
            if(elements.get(i).getType() == FamilyAdapter.TYPE){
                last = i;
            }
        recyclerView.scrollToPosition(last);
        Toast.makeText(this, "New Family added: " + f.getName(), Toast.LENGTH_LONG ).show();


    }
    public void expandFamily(int element_position, ElementListAdapter adapter){
        Log.d("JUEGO", "expandFamily - BEFORE: ");
        if(male_selected != -1){
            unselectIndividual(male_selected);
            male_selected = -1;
        }
        if(female_selected != -1){
            unselectIndividual(female_selected);
            female_selected = -1;
        }
        family_selected = -1;
        Family f = prob.getFamilies().get(elements.get(element_position).getId());
        ArrayList<IndividualAdapter> addoff = new ArrayList<>();
        for(Individual i: f.getOffspring()){
            addoff.add(new IndividualAdapter(i, i.getId()));
        }
        elements.addAll(element_position + 1, addoff);
        adapter.notifyDataSetChanged();
        elements.get(element_position).setExpanded();
        recyclerView.scrollToPosition(element_position);
        adapter.notifyItemChanged(element_position);
    }
    public void collapseFamily(int element_position, ElementListAdapter adapter){
        ArrayList<Integer> remove = new ArrayList();
        if(male_selected != -1){
            unselectIndividual(male_selected);
            male_selected = -1;
        }
        if(female_selected != -1){
            unselectIndividual(female_selected);
            female_selected = -1;
        }
        family_selected = -1;
        int i = element_position + 1;
        while(elements.get(i).getType() != FamilyAdapter.TYPE){
            elements.remove(i);
            adapter.notifyItemRemoved(i);
            if(i >= elements.size())
                break;
        }
        elements.get(element_position).setExpanded();
        adapter.notifyItemChanged(element_position);
    }
    public void getGenerationWithLastIndividuals(){
        int test_f = -1, test_m = -1;
        for(Individual f1: prob.getIndividuals()){
            if(f1.getId() < 2)
                continue;
            if(f1.getSex() == Sex.FEMALE)
                test_f = f1.getId();
            else if(f1.getSex() == Sex.MALE)
                test_m = f1.getId();
        }
        prob.addGeneration(test_f, test_m);
    }
    public void startGame(){
        final String[] this_level_problems;
        Log.d("JUEGO", "StartGame 1");
        switch (current_level){
            case "0":
                this_level_problems = level1_problemtypes;
                break;
            case "1":
                this_level_problems = level2_problemtypes;
                break;
            case "2":
                this_level_problems = level3_problemtypes;
                break;
            case "3":
                Log.d("JUEGO", "StartGame 2");
                this_level_problems = level4_problemtypes;
                Log.d("JUEGO", "StartGame 3");
                break;
            default:
                this_level_problems = all_problemtypes;
        }
        Log.d("JUEGO", "StartGame 4");
        int id = Random.Default.nextInt(0, this_level_problems.length);
        Log.d("JUEGO", "Current Problem: " + Integer.toString(id));
        Toast.makeText(this, "Problem selected: " + this_level_problems[id], Toast.LENGTH_LONG );
        id = getResources().getIdentifier(this_level_problems[id], "raw", getPackageName());
        InputStream JSONFileInputStream = getResources().openRawResource(id);
        problem_type = ProblemTypeInitializer.Companion.buildProblemTypeFromJSON(this.readTextFile(JSONFileInputStream), this.genome);
        prob = problem_type.getProblemInstance();
        Log.d("problem instance", prob.toString());

        Log.d("GENERATION", prob.toString());
        for(Family f: prob.getFamilies())
            Log.d("FAMILIES", f.toString());
        for(Individual i: prob.getIndividuals())
            Log.d("INDIVIDUALS", i.toString());

        recyclerView = findViewById(R.id.recyclerView);
        adaptador = this.getList(prob);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d("LISTENER", "Juego - onItemClick");
                        ElementAdapter e = elements.get(position);
                        if(e.getType() == IndividualAdapter.TYPE){
                            //Individual ind = prob.getIndividuals().get(e.getId());
                            Toast.makeText(view.getContext(), "Selected " + e.getImageFooter(), Toast.LENGTH_LONG).show();
                            onIndividualSelected(e);
                        }else{
                            if(e.isExpanded()){

                                collapseFamily(position, adaptador);
                            }else{
                                expandFamily(position, adaptador);
                            }

                        }
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        Log.d("LISTENER", "Juego - onLongItemClick");
                        ElementAdapter e = elements.get(position);
                        Log.d("LISTENER", "Juego - onLongItemClick 2" + e.getImageFooter() );
                        //Toast.makeText(view.getContext(), "TOUCHED LONG: " + e.getImageFooter() + e.getExtraInfo1(), Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    public void startGameFromDatabase(){
        Log.d("JUEGO", "StartGame from DB 1: " + db_female_hap1 + db_female_hap2 + db_male_hap1 + db_male_hap2);
        //int genome_id = R.raw.genome1;//getResources().getIdentifier(db_genome_name, "raw", getPackageName());
        //InputStream JSONFileInputStream = getResources().openRawResource(genome_id);
        //this.genome = GenomeInitializer.Companion.buildGenomeFromJSON(this.readTextFile(JSONFileInputStream));
        InputStream JSONFileInputStream = getResources().openRawResource(R.raw.problemtypemap);
        problem_type = ProblemTypeInitializer.Companion.buildProblemTypeFromJSON(this.readTextFile(JSONFileInputStream), genome);
        Log.d("JUEGO", "Print problemtype 1" + problem_type.toString());
        problem_type.setFather_hap1(db_male_hap1);
        problem_type.setFather_hap2(db_male_hap2);
        problem_type.setMother_hap1(db_female_hap1);
        problem_type.setMother_hap2(db_female_hap2);
        Log.d("JUEGO", "Print problemtype 2" + problem_type.toString());
        Log.d("JUEGO", "StartGame from DB 2");
        prob = problem_type.getProblemInstance();
        Log.d("JUEGO", "From DB, problem instance: " + prob.toString());
        Log.d("JUEGO", "StartGame from DB 3");
        Log.d("GENERATION", prob.toString());
        for(Family f: prob.getFamilies())
            Log.d("FAMILIES", f.toString());
        for(Individual i: prob.getIndividuals())
            Log.d("INDIVIDUALS", i.toString());
        Log.d("JUEGO", "StartGame from DB 4");
        recyclerView = findViewById(R.id.recyclerView);
        adaptador = this.getList(prob);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d("LISTENER", "Juego - onItemClick");
                        ElementAdapter e = elements.get(position);
                        if(e.getType() == IndividualAdapter.TYPE){
                            //Individual ind = prob.getIndividuals().get(e.getId());
                            Toast.makeText(view.getContext(), "Selected " + e.getImageFooter(), Toast.LENGTH_LONG).show();
                            onIndividualSelected(e);
                        }else{
                            if(e.isExpanded()){

                                collapseFamily(position, adaptador);
                            }else{
                                expandFamily(position, adaptador);
                            }

                        }
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        Log.d("LISTENER", "Juego - onLongItemClick");
                        ElementAdapter e = elements.get(position);
                        Log.d("LISTENER", "Juego - onLongItemClick 2" + e.getImageFooter() );
                        //Toast.makeText(view.getContext(), "TOUCHED LONG: " + e.getImageFooter() + e.getExtraInfo1(), Toast.LENGTH_LONG).show();
                    }
                })
        );
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
    public static ArrayList<Integer> stringToHaplotype(String s){
        ArrayList<Integer> hap = new ArrayList<>();
        String sarr[] = s.split(",");
        for(int i = 0; i < sarr.length; i++){
            hap.add(i, Integer.valueOf(sarr[i]));
        }
        return hap;
    }
}