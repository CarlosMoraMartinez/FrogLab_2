package com.cmora.froglab_2.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cmora.froglab_2.R;
import com.cmora.froglab_2.database.FrogDatabase;
import com.cmora.froglab_2.genetics.Individual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ElementListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected LayoutInflater inflador;
    protected List<ElementAdapter> lista;
    protected List<LayerDrawable> drawables;
    protected Context context;
    private Bitmap bm;
    public ElementListAdapter(Context context, List<ElementAdapter> lista, List<LayerDrawable> drawables) {
        this.context = context;
        this.lista = lista;
        this.drawables = drawables;
        inflador =(LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getItemViewType(int position){
        return lista.get(position).getType();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("ELEMENT_LIST_ADAPTER", "onCreateViewHolder");
        switch(viewType) {
            case FamilyAdapter.TYPE:
                View v1 = inflador.inflate(R.layout.family_cardview, parent, false);
                return new FamilyViewHolder(v1);
            case IndividualAdapter.TYPE:
                View v2 = inflador.inflate(R.layout.individual_cardview_icons, parent, false);
                return new IndividualViewHolder(v2);
            default:
                View v3 = inflador.inflate(R.layout.individual_cardview_icons, parent, false);
                return new IndividualViewHolder(v3);
        }
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {
        Log.d("ELEMENT_LIST_ADAPTER", "onBindViewHolder");
        ElementAdapter el = lista.get(i);
        int type = el.getType();
        switch(type) {
            case FamilyAdapter.TYPE:
                FamilyViewHolder vh = (FamilyViewHolder)holder;
                vh.fam_name.setText(context.getString(R.string.family) + " " +el.getExtraInfo1());
                vh.icon.setImageDrawable(this.drawables.get(el.getLayerDrawableInd()));
                //vh.frog1_name.setText(context.getString(R.string.individual) + " " + el.getImageFooter());
                vh.frog1_name.setText(el.getImageFooter());
                vh.icon2.setImageDrawable(this.drawables.get(el.getLayerDrawableInd()));
                //vh.frog2_name.setText(context.getString(R.string.individual) + " " + el.getImageFooter());
                vh.frog2_name.setText(el.getImageFooter());
                int exp;
                if(el.isExpanded()) {
                    exp = R.drawable.ic_expand_less_24px;
                } else {
                    exp = R.drawable.ic_expand_more_24px;
                }
                vh.expanded_state.setImageDrawable(context.getResources().getDrawable(exp));
                vh.id = el.getId();
                break;
            case IndividualAdapter.TYPE:
                IndividualViewHolder vh2 = (IndividualViewHolder)holder;
                vh2.titulo.setText(context.getString(R.string.individual) + " " + el.getImageFooter());
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.context);
                if(pref.getBoolean("cheating", false)){
                    vh2.subtitulo.setText(Html.fromHtml("<b>" +
                            context.getString(R.string.phenotype) + "</b> " +
                            el.getExtraInfo1() +
                            "<br><b>" + context.getString(R.string.genotype) + "</b> " +
                            el.getExtraInfo2()));
                }else {
                    vh2.subtitulo.setText(Html.fromHtml("<b>" + context.getString(R.string.phenotype) +
                            "</b> " + el.getExtraInfo1()));
                }
                vh2.icon.setImageDrawable(this.drawables.get(el.getLayerDrawableInd()));
                vh2.id = el.getId();
                if(el.isSelected()) {
                    vh2.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorIndividualCardSelected));
                    vh2.card.setCardElevation(20);
                }
                else {
                    vh2.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorIndividualCard));
                    vh2.card.setCardElevation(8);
                }
                Log.d("ElementListAdapter", "Setting button listener 1");
                vh2.save_image_button.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View view) {
                        saveImage(view);
                        return false;
                    }
                    });
                Log.d("ElementListAdapter", "Setting button listener 2");
                vh2.save_frog_button.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View view) {
                        saveFrog(view);
                        return false;
                    }
                });
                vh2.setTags();
                break;
        }
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void saveImage(View view){
        String id = (String) view.getTag();
        for(ElementAdapter el : lista){
            if(el.getType() == IndividualAdapter.TYPE && Integer.toString(el.getId()).equals(id)){
                Individual ind = (Individual)el.getObject();
                Log.d("ElementListAdapter", "Saving Image, ItemFound: " + ind.toString());
                Drawable d = drawables.get(ind.getId());
                bm = drawableToBitmap(d);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    makeSaveImageAlert();
                    //Toast.makeText(context, "Saving Image"+ id, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Permission required. Grant access to storage to save images.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    public void saveFrog(View view){
        String id = (String) view.getTag();
        for(ElementAdapter el : lista){
            if(el.getType() == IndividualAdapter.TYPE && Integer.toString(el.getId()).equals(id)){
                Individual ind = (Individual)el.getObject();
                Log.d("ElementListAdapter", "SaveFrog, ItemFound: " + ind.toString());
                FrogDatabase db = new FrogDatabase(context);
                makeSaveAlert(ind, db);
                //db.saveFrog(ind, name);
                Log.d("ElementListAdapter", "SaveFrog, End");
                break;
            }
        }
    }
    public void makeSaveAlert(final Individual ind, final FrogDatabase db){
        AlertDialog.Builder alertName = new AlertDialog.Builder(context);
        final EditText editTextName1 = new EditText(context);

        Log.d("ElementListAdapter", "makeSaveAlert 1");
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.save_frog_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String name = userInput.getText().toString().replaceAll("[\\n\\t]", " ");
                                db.saveFrog(ind, name);
                                Toast.makeText(context, "Saving Frog to Database: " + name, Toast.LENGTH_SHORT).show();
                                Log.d("ElementListAdapter", "makeSaveAlert: Got Result from User");
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Toast.makeText(context, "Saving Frog Canceled", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Log.d("ElementListAdapter", "makeSaveAlert 2");
    }

    public void makeSaveImageAlert(){

        AlertDialog.Builder alertName = new AlertDialog.Builder(context);
        final EditText editTextName1 = new EditText(context);

        Log.d("ElementListAdapter", "makeSaveImageAlert 1");
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.save_frog_prompt, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        TextView message = (TextView) promptsView.findViewById(R.id.textView1);
        message.setText(R.string.save_frog_message2);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //String extStorageDirectory = Environment.getExternalStorageDirectory();
                                //File file = new File(extStorageDirectory, "ic_launcher.PNG");
                                String stadoSD = Environment.getExternalStorageState();
                                if (!stadoSD.equals(Environment.MEDIA_MOUNTED)) {
                                    Log.d("ElementListAdapter", "Error writing to memory");
                                    Toast.makeText(context, "Unable to write in external memory",
                                            Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                                String name = userInput.getText().toString().replaceAll("[\\n\\t]", " ");
                                String filename = Environment.getExternalStorageDirectory() + name + ".png";
                                File file = new File(Environment.getExternalStorageDirectory(), name + ".png");
                                Log.d("ElementListAdapter", "makeSaveImageAlert: Writting image to " + filename);

                                //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "DemoFrogImage.jpg");
                                FileOutputStream outStream = null;
                                try {
                                    outStream = new FileOutputStream(file);
                                    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                                    try {
                                        outStream.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.d("ElementListAdapter", "Error flush to file");
                                    }
                                    try {
                                        outStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.d("ElementListAdapter", "Error closing file");
                                    }
                                } catch (FileNotFoundException e) {
                                    Log.d("ElementListAdapter", "Error opening file");
                                    e.printStackTrace();
                                }
                                Toast.makeText(context, "Saving Frog Image: " + filename, Toast.LENGTH_LONG).show();
                                Log.d("ElementListAdapter", "makeSaveImageAlert: Got Result from User");
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Toast.makeText(context, "Saving Frog Image Canceled", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Log.d("ElementListAdapter", "makeSaveImageAlert 2");
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}