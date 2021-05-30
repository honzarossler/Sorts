package cz.janrossler.sorts;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.adapter.NodeAdapter;
import cz.janrossler.sorts.utils.BinarySearchTree;
import cz.janrossler.sorts.utils.Node;
import cz.janrossler.sorts.utils.NumberManager;

public class TreeViewActivity extends AppCompatActivity {
    private NumberManager numberManager;
    private Intent intent;
    private boolean usingSession;
    private Node node;

    private RecyclerView tree;
    private LinearLayout tool_layout;
    private FloatingActionButton tool_add;
    private FloatingActionButton tool_remove;
    private FloatingActionButton tool_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberManager = new NumberManager(this);

        intent = getIntent();
        if(intent == null) finish();

        usingSession = intent.hasExtra("session");

        setContentView(R.layout.activity_tree_view);
        tree = findViewById(R.id.tree);
        tool_layout = findViewById(R.id.tool_layout);
        tool_add = findViewById(R.id.tool_add);
        tool_remove = findViewById(R.id.tool_remove);
        tool_search = findViewById(R.id.tool_search);

        if(usingSession){
            List<Integer> numbers = numberManager
                    .getUnsortedSessionList(intent.getStringExtra("session"));

            node = BinarySearchTree.createFromList(numbers);
        }

        tool_layout.setVisibility(usingSession ? View.GONE : View.VISIBLE);

        tool_add.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Zadejte číslo pro přidání.");

            EditText edit = new EditText(this);
            edit.setHint("...");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setRawInputType(Configuration.KEYBOARD_12KEY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            edit.setLayoutParams(params);

            builder.setView(edit);
            builder.setPositiveButton("Přidat", (dialog, which) -> {
                BinarySearchTree tree = new BinarySearchTree();
                node = tree.insert(node, Integer.parseInt(edit.getText().toString()));
                update();
            });
            builder.setNegativeButton("Zrušit", null);
            builder.show();
        });

        tool_remove.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Zadejte číslo pro odebrání.");

            EditText edit = new EditText(this);
            edit.setHint("...");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setRawInputType(Configuration.KEYBOARD_12KEY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            edit.setLayoutParams(params);

            builder.setView(edit);
            builder.setPositiveButton("Odebrat", (dialog, which) -> {
                BinarySearchTree tree = new BinarySearchTree();
                node = tree.remove(node, Integer.parseInt(edit.getText().toString()));
                update();
            });
            builder.setNegativeButton("Zrušit", null);
            builder.show();
        });

        tool_search.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Zadejte číslo pro vyhledávání.");

            EditText edit = new EditText(this);
            edit.setHint("...");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setRawInputType(Configuration.KEYBOARD_12KEY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            edit.setLayoutParams(params);

            builder.setView(edit);
            builder.setPositiveButton("Hledat", (dialog, which) -> {
                BinarySearchTree tree = new BinarySearchTree();
                BinarySearchTree.SearchResult res = tree.search(node, Integer.parseInt(edit.getText().toString()));

                AlertDialog.Builder result = new AlertDialog.Builder(this);
                result.setTitle("Výsledek hledání");
                if(res.found){
                    result.setMessage("Číslo "+res.value+" bylo nalezeno "+ res.amount+"x.");
                }else{
                    result.setMessage("Číslo "+res.value+" Nebylo nalezeno.");
                }
                result.setPositiveButton("Ok", (dialog1, which1) -> {

                });
                result.show();
            });
            builder.setNegativeButton("Zrušit", null);
            builder.show();
        });

        update();
    }

    private void update(){
        if(node != null){
            List<Node> nodes = new ArrayList<>();
            nodes.add(node);

            NodeAdapter adapter = new NodeAdapter(this, nodes);
            tree.setLayoutManager(new GridLayoutManager(this, 1));
            tree.setAdapter(adapter);
            tree.setVisibility(View.VISIBLE);
        }else{
            tree.setVisibility(View.GONE);
        }
    }
}