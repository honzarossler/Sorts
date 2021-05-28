package cz.janrossler.sorts;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import cz.janrossler.sorts.adapter.TheoryAdapter;
import cz.janrossler.sorts.utils.TheoryReader;

public class TheoryActivity extends AppCompatActivity {
    private RecyclerView content;
    private Intent intent;

    private JSONObject theory;
    private TheoryReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);
        content = findViewById(R.id.content);

        intent = getIntent();
        theory = new JSONObject();

        if (intent == null)
            finish();

        if (!intent.hasExtra("theory"))
            finish();

        reader = new TheoryReader(this);
        theory = reader.getTheoryData(intent.getStringExtra("theory"));

        try {
            TheoryAdapter adapter = new TheoryAdapter(this, theory.getJSONObject("body").getJSONArray("_"));

            content.setLayoutManager(new LinearLayoutManager(this));
            content.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}