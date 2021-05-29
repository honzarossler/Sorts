package cz.janrossler.sorts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.adapter.NodeAdapter;
import cz.janrossler.sorts.utils.BinarySearchTree;
import cz.janrossler.sorts.utils.Node;
import cz.janrossler.sorts.utils.NumberManager;
import cz.janrossler.sorts.utils.Utilities;

public class TreeViewActivity extends AppCompatActivity {
    private NumberManager numberManager;
    private Intent intent;
    private JSONArray unsorted = new JSONArray();

    private RecyclerView tree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberManager = new NumberManager(this);

        intent = getIntent();
        if(intent == null || !intent.hasExtra("session")) finish();

        List<Integer> list_unsorted = numberManager.getUnsortedSessionList(intent.getStringExtra("session"));

        for(int i = 0; i < list_unsorted.size(); i++)
            unsorted.put(list_unsorted.get(i));

        List<Node> nodes = new ArrayList<>();

        if(unsorted.length() <= Utilities.MAX_TREE_SIZE){
            BinarySearchTree.Recursive tree = new BinarySearchTree.Recursive();
            Node node = null;
            for(int i = 0; i < unsorted.length(); i++){
                try {
                    node = tree.insert(node, unsorted.getInt(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(node != null){
                nodes.add(node);
            }else{
                Toast.makeText(this, "Nelze vytvořit strom.", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Nelze sestavit strom z vysokého počtu čísel.", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_tree_view);
        tree = findViewById(R.id.tree);

        NodeAdapter adapter = new NodeAdapter(this, nodes);
        tree.setLayoutManager(new GridLayoutManager(this, 1));
        tree.setAdapter(adapter);
    }
}