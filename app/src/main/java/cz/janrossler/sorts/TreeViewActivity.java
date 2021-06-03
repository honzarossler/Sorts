package cz.janrossler.sorts;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.janrossler.sorts.adapter.NodeAdapter;
import cz.janrossler.sorts.utils.BinarySearchTree;
import cz.janrossler.sorts.utils.Node;
import cz.janrossler.sorts.utils.NumberManager;

public class TreeViewActivity extends AppCompatActivity {
    private NumberManager numberManager;
    private Intent intent;
    private boolean usingSession;
    private boolean isSessionEditable = false;
    private Node node;
    private JSONObject session;

    private RecyclerView tree;
    private FloatingActionButton tool_add;
    private FloatingActionButton tool_remove;
    private FloatingActionButton tool_search;
    private FloatingActionButton tool_save;
    private ImageView locked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberManager = new NumberManager(this);

        intent = getIntent();
        if(intent == null) finish();

        usingSession = intent.hasExtra("session");

        setContentView(R.layout.activity_tree_view);
        tree = findViewById(R.id.tree);
        locked = findViewById(R.id.locked);
        tool_add = findViewById(R.id.tool_add);
        tool_remove = findViewById(R.id.tool_remove);
        tool_search = findViewById(R.id.tool_search);
        tool_save = findViewById(R.id.tool_save);
        locked.setVisibility(View.GONE);

        if(usingSession){
            session = numberManager.getSession(intent.getStringExtra("session"));
            List<Integer> numbers = numberManager
                    .getUnsortedSessionList(intent.getStringExtra("session"));

            node = BinarySearchTree.createFromList(numbers);

            try {
                isSessionEditable = session.has("editable") && session.getBoolean("editable");
            }catch (Exception e){
                isSessionEditable = false;
            }
            locked.setVisibility(!isSessionEditable ? View.VISIBLE : View.GONE);
        }else session = new JSONObject();

        tool_add.setVisibility(usingSession && !isSessionEditable ? View.GONE : View.VISIBLE);
        tool_remove.setVisibility(usingSession && !isSessionEditable ? View.GONE : View.VISIBLE);
        tool_save.setVisibility(usingSession && !isSessionEditable ? View.GONE : View.VISIBLE);

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

        tool_save.setOnClickListener(v -> {
            List<Integer> list = node.toList();
            numberManager.pushList(intent.getStringExtra("session"), list);
            Toast.makeText(this, "Uloženo.",Toast.LENGTH_LONG).show();
        });

        locked.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Proč je tento strom uzamčený?");
            builder.setMessage("Tato instance byla vytvořena pomocí generátoru náhodných čísel a pro možné problémy s načítáním stromu jsou editační možnosti vypnuty. Pokud chcete strom upravit, vytvořte si nový strom a uložte jej.");
            builder.setPositiveButton("Ok", null);
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

    @Override
    public void onBackPressed() {
        if(!usingSession && node != null){
            AlertDialog.Builder save = new AlertDialog.Builder(this);
            save.setTitle("Uložit strom");
            save.setMessage("Chcete uložit tento strom jako novou instanci?");
            save.setPositiveButton("Uložit", (dialog, which) -> {
                List<Integer> list = node.toList();

                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH) + 1;
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int mi = calendar.get(Calendar.MINUTE);
                String session = y + "-" + m + "-" + d + " " + h + "-" + mi;

                numberManager.createSession(session, true);
                numberManager.pushList(session, list);
                super.onBackPressed();
            });
            save.setNegativeButton("Smazat", (dialog, which) -> super.onBackPressed());
            save.setNeutralButton("Zachovat", null);
            save.show();
        }else if(usingSession && isSessionEditable && node != null) {
            AlertDialog.Builder save = new AlertDialog.Builder(this);
            save.setTitle("Uložit strom");
            save.setMessage("Chcete uložit tento strom?");
            save.setPositiveButton("Uložit", (dialog, which) -> {
                List<Integer> list = node.toList();
                numberManager.pushList(intent.getStringExtra("session"), list);
                super.onBackPressed();
            });
            save.setNegativeButton("Zahodit", (dialog, which) -> super.onBackPressed());
            save.setNeutralButton("Zachovat", null);
            save.show();
        }else super.onBackPressed();
    }
}