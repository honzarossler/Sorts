package cz.janrossler.sorts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Locale;

import cz.janrossler.sorts.adapter.TheoryAdapter;
import cz.janrossler.sorts.utils.Theory;
import cz.janrossler.sorts.utils.TheoryReader;

public class TheoryActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private RecyclerView content;
    private Intent intent;

    private Theory theory;
    private TheoryReader reader;
    private String thisLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);
        content = findViewById(R.id.content);
        Log.d("Language", Locale.getDefault().toString());
        thisLang = Locale.getDefault().toString();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        intent = getIntent();

        if (intent == null)
            finish();

        if (!intent.hasExtra("theory"))
            finish();

        reader = new TheoryReader(this);
        theory = new Theory(this, intent.getStringExtra("theory"));
        actionBar.setTitle(theory.getTitleText());

        TheoryAdapter adapter = new TheoryAdapter(this, theory.getLocalizedContent());

        content.setLayoutManager(new LinearLayoutManager(this));
        content.setAdapter(adapter);
    }

    private void showLanguageOptions(){
        String[] langs = theory.getAllSupportedLanguages();
        CharSequence[] langsChar = new CharSequence[langs.length];
        int defIndex = Arrays.asList(langs).contains(thisLang) ? Arrays.asList(langs).indexOf(thisLang) : -1;

        for(int i = 0; i < langs.length; i++) {
            if(!langs[i].equals("_")){
                Locale loc = new Locale(langs[i]);
                langsChar[i] = loc.getDisplayLanguage(loc);
            }else {
                if(defIndex < 0)
                    defIndex = i;
                langsChar[i] = "Výchozí";
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vyberte jazyk");
        builder.setSingleChoiceItems(langsChar, defIndex, null);
        builder.setPositiveButton("Změnit", (dialog, which) -> {
            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
            TheoryAdapter adapter =
                    new TheoryAdapter(this, theory.getTranslatedContent(langs[selectedPosition]));

            content.setLayoutManager(new LinearLayoutManager(this));
            content.setAdapter(adapter);
        });
        builder.setNegativeButton("Zrušit", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.theory_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.language:
                showLanguageOptions();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}