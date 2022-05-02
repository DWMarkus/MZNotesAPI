package com.amgap.mznotesapi.activities;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.amgap.mznotesapi.R;
import com.amgap.mznotesapi.utils.ActivityHelper;
import com.amgap.mznotesapi.utils.BiMap;

import java.util.Locale;

// Sonarlint java:S110 - Sans ça, l'appli crash au démarrage
@SuppressWarnings("java:S110")
public class ChangeLangActivity extends LocalizationActivity {
    private static final BiMap<Integer, String> langCodes;

    static {
        langCodes = new BiMap<>();
        langCodes.put(R.id.radio_fr, "en");
        langCodes.put(R.id.radio_en, "fr");
    }

    private void checkDefaultLanguage(RadioGroup rg) {
        String defaultLangCode = Locale.getDefault().getLanguage();
        if(!langCodes.get().containsValue(defaultLangCode)) {
            // langage par defaut
            defaultLangCode = "en";
        }
        rg.check(langCodes.inverted().get(defaultLangCode));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        ActivityHelper.addBackButton(getSupportActionBar());

        final RadioGroup rg = findViewById(R.id.selected_lang);

        checkDefaultLanguage(rg);

        findViewById(R.id.btnChangeLangView).setOnClickListener(view -> {
            final String langCode = langCodes.get().get(rg.getCheckedRadioButtonId());
            setLanguage(langCode);
        });
    }
}
