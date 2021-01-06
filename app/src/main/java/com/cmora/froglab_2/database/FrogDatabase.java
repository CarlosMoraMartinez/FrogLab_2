package com.cmora.froglab_2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cmora.froglab_2.Juego;
import com.cmora.froglab_2.R;
import com.cmora.froglab_2.genetics.BaseGenotype;
import com.cmora.froglab_2.genetics.GenomeInitializer;
import com.cmora.froglab_2.genetics.GenomeModel;
import com.cmora.froglab_2.genetics.Individual;
import com.cmora.froglab_2.genetics.Sex;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FrogDatabase extends SQLiteOpenHelper {
    String DEFAULT_GENOME = "genome1";
    Context context;
    public FrogDatabase(Context context) {
        super(context, "frogdb", null, 1);
        this.context = context;
    }
    //Métodos de SQLiteOpenHelper
    @Override public void onCreate(SQLiteDatabase db) {
        Log.d("DATABASE", "Creating database 1");
        db.execSQL("CREATE TABLE individuals ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "genome TEXT, " +
                "name TEXT, " +
                "haplotype1 TEXT, " +
                "haplotype2 TEXT, " +
                "sex INTEGER, " +
                "date BIGINT)");
        Log.d("DATABASE", "Creating database 2");
        db.execSQL("INSERT INTO individuals VALUES ( null, "+
                 "'genome1'" +
                ", '" + "Homozygous 1Ma" +
                "', '" + "1,1,1,1,1,1,1,1,1,-1" +
                "', '" + "1,1,1,1,1,1,1,1,1,-1" +
                "', " + 0 +
                ", " + System.currentTimeMillis() +")");
        Log.d("DATABASE", "Creating database 3");
        db.execSQL("INSERT INTO individuals VALUES ( null, "+
                "'genome1'" +
                ", '" + "Homozygous 2MA" +
                "', '" + "0,0,0,0,0,0,0,0,0,-1" +
                "', '" + "0,0,0,0,0,0,0,0,0,-1" +
                "', " + 0 +
                ", " + System.currentTimeMillis() +")");
        db.execSQL("INSERT INTO individuals VALUES ( null, "+
                "'genome1'" +
                ", '" + "Heterozygous M" +
                "', '" + "0,0,0,0,0,0,0,0,0,-1" +
                "', '" + "1,1,1,1,1,1,1,1,1,-1" +
                "', " + 0 +
                ", " + System.currentTimeMillis() +")");
        Log.d("DATABASE", "Creating database 4");
        db.execSQL("INSERT INTO individuals VALUES ( null, "+
                "'genome1'" +
                ", '" + "Homozygous 1Fa" +
                "', '" + "1,1,1,1,1,1,1,1,1,1" +
                "', '" + "1,1,1,1,1,1,-1,-1,-1,-1" +
                "', " + 1 +
                ", " + System.currentTimeMillis() +")");
        Log.d("DATABASE", "Creating database 5");
        db.execSQL("INSERT INTO individuals VALUES ( null, "+
                "'genome1'" +
                ", '" + "Homozygous 2FA" +
                "', '" + "0,0,0,0,0,0,0,0,0,0" +
                "', '" + "0,0,0,0,0,0,-1,-1,-1,-1" +
                "', " + 1 +
                ", " + System.currentTimeMillis() +")");
        db.execSQL("INSERT INTO individuals VALUES ( null, "+
                "'genome1'" +
                ", '" + "Heterozygous F" +
                "', '" + "0,0,0,0,0,0,0,0,0,0" +
                "', '" + "1,1,1,1,1,1,-1,-1,-1,-1" +
                "', " + 1 +
                ", " + System.currentTimeMillis() +")");
        Log.d("DATABASE", "Creating database 6");
        //db.close();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
    }

    public void saveFrog(Individual frog, String nombre) {
        Log.d("DATABASE", "Save frog 1, sex: " + frog.getSex().ordinal() );
        SQLiteDatabase db = getWritableDatabase();
        List<Integer> hap1 = frog.getBase_genotype().getHaplotype1();
        List<Integer> hap2 = frog.getBase_genotype().getHaplotype2();
        List<Integer> ed_genes = frog.getBase_genotype().getEditable_genes();
        int aux;
        for(int i = 0; i < ed_genes.size(); i++){
            aux = ed_genes.get(i);
            hap1.set(aux, frog.getHaplotype1().get(i));
            hap2.set(aux, frog.getHaplotype2().get(i));
        }
        db.execSQL("INSERT INTO individuals VALUES ( null, "+
                "'" +  frog.getBase_genotype().getGenome().getName() +
                "', '" + nombre +
                "', '" + haplotypeToString(hap1) +
                "', '" + haplotypeToString(hap2) +
                "', " + frog.getSex().ordinal() +
                ", " + System.currentTimeMillis() +")");

        //db.close();
        Log.d("DATABASE", "Save frog 2");
    }

    public List<Individual> getAllFrogs() {
        Log.d("DATABASE", "Getting cursor 1");
        String[] columns = new String[] { "genome", "name", "haplotype1", "haplotype2", "sex", "date" };
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("individuals", columns, null,
                null, null, null, null);
        List<Individual> result = new ArrayList<>();
        String genome_name = DEFAULT_GENOME, ind_name;
        Sex sex_arr[] = Sex.values();;
        ArrayList<Integer> hap1, hap2;
        /*byte[] hap1_byte;
        byte[] hap2_byte;
        ByteBuffer wrapped;*/
        GenomeModel genome = readGenome(DEFAULT_GENOME);
        BaseGenotype base_genotype = null;
        Individual ind;
        long date;
        int id = 0;
        int sex_aux;

        while (cursor.moveToNext()){
            genome_name = cursor.getString(0);
            if(!genome_name.equals(genome.getName()) ){
                genome = readGenome(genome_name);
                Log.d("DATABASE", "genome read: " + genome_name);
                base_genotype = getBaseGenotype(genome);
            }

            ind_name = cursor.getString(1);
            hap1 = stringToHaplotype(cursor.getString(2));
            hap2 = stringToHaplotype(cursor.getString(3));
            Log.d("DATABASE", "got haplotypes: " + hap1.toString() + ", " + hap2.toString());
            sex_aux = cursor.getInt(4);

            Log.d("DATABASE", "got Sex: " + Integer.toString(sex_aux));

            date = cursor.getLong(5);
            Log.d("DATABASE", "got Date");

            ind = new Individual(id, -2, base_genotype,hap1, hap2, sex_arr[sex_aux]);
            Log.d("DATABASE", "Initialized individual");
            ind.setUser_name(ind_name);
            ind.setDate(date);
            Log.d("DATABASE", "Set extra fields to individual");
            result.add(id, ind);
            id++;
        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        Log.d("DATABASE", "Returning");
        return result;
    }
    public GenomeModel readGenome(String genome_name){
        Log.d("DATABASE", "Reading genome");
        if(context == null){
            Log.d("DATABASE", "Null context");
        }
        int id = R.raw.genome1;//context.getResources().getIdentifier(genome_name,  "raw", context.getPackageName());
        InputStream JSONFileInputStream = context.getResources().openRawResource(id);
        GenomeModel genome = GenomeInitializer.Companion.buildGenomeFromJSON(Juego.readTextFile(JSONFileInputStream));
        return genome;
    }
    public BaseGenotype getBaseGenotype(GenomeModel genome){
        Log.d("DATABASE", "Creating BaseGenotype");
        List<Integer> hap1 = new ArrayList<>();
        List<Integer> hap2 = new ArrayList<>();
        List<Integer> edgenes = new ArrayList<>();
        for(int i = 0; i < genome.getNum_genes(); i++){
            hap1.add(0);
            hap2.add(0);
            edgenes.add(i);
        }
        Log.d("DATABASE", "Returning BaseGenotype");
        return new BaseGenotype(hap1, hap2, edgenes, genome);
    }
    public static String haplotypeToString(List<Integer> hap){
        String s = "";
        for(Integer i : hap)
            s += Integer.toString((i)) + ",";
        return s.substring(0, s.length() - 1);
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