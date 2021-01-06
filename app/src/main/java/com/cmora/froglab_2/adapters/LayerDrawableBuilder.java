package com.cmora.froglab_2.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.cmora.froglab_2.R;
import com.cmora.froglab_2.genetics.Individual;

import java.util.ArrayList;
import java.util.List;

public class LayerDrawableBuilder {
    public static LayerDrawable getLayerDrawable(Context context, Individual individual) {
        Drawable f = AppCompatResources.getDrawable(context, R.drawable.ic_bodycolor_red);
        List<String> resources = individual.getResources();
        Resources sysres =context.getResources();
        List<Drawable> ldrawlist = new ArrayList<>();
        Drawable aux;
        int id;
        for(int res = 0; res < resources.size(); res++) {
            Log.d("LayerDrawableBuilder", resources.get(res));
            if (resources.get(res).equals("empty"))
                continue;
            id = context.getResources().getIdentifier(resources.get(res), "drawable", context.getPackageName());
            aux = AppCompatResources.getDrawable(context, id);
            ldrawlist.add(aux);
        }
        Drawable ldraw[] = new Drawable[ldrawlist.size()];
        for(int res = 0; res < ldrawlist.size(); res++) {
            ldraw[res] = ldrawlist.get(res);
        }
            /*try {
                aux = Drawable.createFromXml(sysres, sysres.getXml(id));
                ldraw[res] = aux;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }*/

        LayerDrawable frog = new LayerDrawable(ldraw);
        return frog;
    }
}
