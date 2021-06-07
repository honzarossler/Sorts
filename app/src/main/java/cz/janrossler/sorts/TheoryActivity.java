package cz.janrossler.sorts;

import android.annotation.SuppressLint;
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
import cz.janrossler.sorts.utils.Language;
import cz.janrossler.sorts.utils.Theory;

public class TheoryActivity extends AppCompatActivity {
    private RecyclerView content;

    private Theory theory;
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
        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();

        if (intent == null)
            finish();

        assert intent != null;
        if (!intent.hasExtra("theory"))
            finish();

        theory = new Theory(this, intent.getStringExtra("theory"));
        if (actionBar != null) {
            actionBar.setTitle(theory.getTitleText());
        }

        TheoryAdapter adapter = new TheoryAdapter(this, theory.getLocalizedContent());

        content.setLayoutManager(new LinearLayoutManager(this));
        content.setAdapter(adapter);
    }

    private void showLanguageOptions(){
        String[] langs = theory.getAllSupportedLanguages();
        CharSequence[] langsChar = new CharSequence[langs.length];
        int defIndex = Arrays.asList(langs).contains(thisLang) ? Arrays.asList(langs).indexOf(thisLang) : -1;

        for(int i = 0; i < langs.length; i++) {
            langsChar[i] = Language.getInstance(this).getLanguage(langs[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_message_select_language));
        builder.setSingleChoiceItems(langsChar, defIndex, null);
        builder.setPositiveButton(getString(R.string.action_change), (dialog, which) -> {
            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
            TheoryAdapter adapter =
                    new TheoryAdapter(this, theory.getTranslatedContent(langs[selectedPosition]));

            content.setLayoutManager(new LinearLayoutManager(this));
            content.setAdapter(adapter);
        });
        builder.setNegativeButton(getString(R.string.action_cancel), null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.theory_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @SuppressLint("NonConstantResourceId")
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