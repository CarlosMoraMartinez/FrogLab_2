package com.cmora.froglab_2;

import android.os.Bundle;
import android.view.View;

import androidx.preference.PreferenceFragmentCompat;

public class PreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        view.setBackgroundColor(getResources().getColor(R.color.colorIndividualCard));
        super.onViewCreated(view, savedInstanceState);
    }


    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Get the Theme specific color
        val typedValue = TypedValue()
        val theme = requireContext().theme

    R.attr.colorBackgroundScreenBody is my own attr from attrs.xml file,
    you can directly use R.color.red - Or any color from your colors.xml
    file if you do not have multi-theme app.
        theme.resolveAttribute(R.attr.colorBackgroundScreenBody, typedValue, true)
        @ColorInt val color = typedValue.data

        view.setBackgroundColor(color)

        super.onViewCreated(view, savedInstanceState)
    }*/
}