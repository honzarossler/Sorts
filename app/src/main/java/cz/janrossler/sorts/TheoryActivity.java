package cz.janrossler.sorts;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.commonmark.node.Heading;

import java.util.Arrays;
import java.util.Locale;

import cz.janrossler.sorts.adapter.TheoryAdapter;
import cz.janrossler.sorts.utils.Language;
import cz.janrossler.sorts.utils.Theory;
import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonSpansFactory;
import io.noties.markwon.SpanFactory;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.ext.latex.JLatexMathPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;

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
            if(selectedPosition >= 0) {
                TheoryAdapter adapter =
                        new TheoryAdapter(this, theory.getTranslatedContent(langs[selectedPosition]));

                thisLang = langs[selectedPosition];
                content.setLayoutManager(new LinearLayoutManager(this));
                content.setAdapter(adapter);
            }
        });
        builder.setNegativeButton(getString(R.string.action_cancel), null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.theory_menu, menu);
        menu.findItem(R.id.source).setVisible(theory.sourceEnabled());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.language){
            showLanguageOptions();
            return true;
        }else if(item.getItemId() == R.id.source){
            openSource();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void openSource(){
        Markwon markwon = Markwon.builder(this)
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureTheme(@NonNull MarkwonTheme.Builder builder) {
                        builder.headingBreakColor(getColor(R.color.pink_700));
                        builder.build();
                    }
                    @Override
                    public void configureSpansFactory(@NonNull MarkwonSpansFactory.Builder builder) {
                        final SpanFactory origin = builder.requireFactory(Heading.class);

                        builder.setFactory(Heading.class, (configuration, props) -> new Object[]{
                                origin.getSpans(configuration, props),
                                new ForegroundColorSpan(getColor(R.color.pink_700))
                        });
                    }
                })
                .usePlugin(MarkwonInlineParserPlugin.create())
                .usePlugin(JLatexMathPlugin.create(16 * getResources().getDisplayMetrics().density, builder -> {
                    builder.inlinesEnabled(true);
                    builder.build();
                }))
                .build();

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = View.inflate(this, R.layout.dialog_source, null);
        TextView textView = view.findViewById(R.id.textView);
        markwon.setMarkdown(textView, theory.getTranslatedSource(thisLang));

        dialog.setContentView(view);
        dialog.show();
    }
}