package cz.janrossler.sorts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.janrossler.sorts.adapter.NodeAdapter;
import cz.janrossler.sorts.utils.BinarySearchTree;
import cz.janrossler.sorts.utils.Node;
import cz.janrossler.sorts.utils.NumberManager;
import cz.janrossler.sorts.utils.Session;
import cz.janrossler.sorts.utils.Template;
import cz.janrossler.sorts.utils.Utilities;

@SuppressWarnings("FieldCanBeLocal")
public class TreeViewActivity extends AppCompatActivity {
    private NumberManager numberManager;
    private Intent intent;
    private boolean usingSession;
    private boolean isSessionEditable = false;
    private boolean isSearchOpen = false;
    private boolean isAddOpen = false;
    private boolean isRemoveOpen = false;
    private Node node;
    private Session session;

    private RecyclerView tree;
    private FloatingActionButton tool_add;
    private FloatingActionButton tool_remove;
    private FloatingActionButton tool_search;
    private FloatingActionButton tool_save;

    private LinearLayout tree_empty;
    private ImageView image_animated;
    private LinearLayout floating_layout_search;
    private EditText edit_search;
    private FloatingActionButton float_search;
    private LinearLayout floating_layout_add;
    private EditText edit_add;
    private FloatingActionButton float_add;
    private LinearLayout floating_layout_remove;
    private EditText edit_remove;
    private FloatingActionButton float_remove;

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
        tree_empty = findViewById(R.id.tree_empty);
        image_animated = findViewById(R.id.image_animated);
        locked.setVisibility(View.GONE);

        floating_layout_search = findViewById(R.id.floating_layout_search);
        edit_search = findViewById(R.id.edit_search);
        float_search = findViewById(R.id.float_search);
        floating_layout_add = findViewById(R.id.floating_layout_add);
        edit_add = findViewById(R.id.edit_add);
        float_add = findViewById(R.id.float_add);
        floating_layout_remove = findViewById(R.id.floating_layout_remove);
        edit_remove = findViewById(R.id.edit_remove);
        float_remove = findViewById(R.id.float_remove);

        Glide.with(this)
                .load(R.drawable.ic_pencil)
                .placeholder(R.drawable.ic_pencil)
                .into(image_animated);

        if(usingSession){
            session = numberManager.getSession(intent.getStringExtra("session"));
            List<Integer> numbers = numberManager
                    .getUnsortedSessionList(intent.getStringExtra("session"), 0);

            node = BinarySearchTree.createFromList(numbers);

            try {
                isSessionEditable = session.isEditable();
            }catch (Exception e){
                isSessionEditable = false;
            }
            locked.setVisibility(!isSessionEditable ? View.VISIBLE : View.GONE);
        }else session = null;

        tool_add.setVisibility(usingSession && !isSessionEditable ? View.GONE : View.VISIBLE);
        tool_remove.setVisibility(usingSession && !isSessionEditable ? View.GONE : View.VISIBLE);
        tool_save.setVisibility(usingSession && !isSessionEditable ? View.GONE : View.VISIBLE);

        tool_add.setOnClickListener(v -> {
            isSearchOpen = false;
            isAddOpen = !isAddOpen;
            isRemoveOpen = false;
            updateActions();
        });

        tool_remove.setOnClickListener(v -> {
            isSearchOpen = false;
            isAddOpen = false;
            isRemoveOpen = !isRemoveOpen;
            updateActions();
        });

        tool_search.setOnClickListener(v -> {
            isSearchOpen = !isSearchOpen;
            isAddOpen = false;
            isRemoveOpen = false;
            updateActions();
        });

        tool_save.setOnClickListener(v -> {
            if(node != null){
                List<Integer> list = node.toList();
                String session;

                if(usingSession){
                    session = intent.getStringExtra("session");
                }else{
                    Calendar calendar = Calendar.getInstance();
                    int y = calendar.get(Calendar.YEAR);
                    int m = calendar.get(Calendar.MONTH) + 1;
                    int d = calendar.get(Calendar.DAY_OF_MONTH);
                    int h = calendar.get(Calendar.HOUR_OF_DAY);
                    int mi = calendar.get(Calendar.MINUTE);
                    session = y + "-" + m + "-" + d + " " + h + "-" + mi;
                }

                numberManager.pushList(session, list);
                Toast.makeText(this, "Uloženo.",Toast.LENGTH_LONG).show();
            }else Toast.makeText(this, "Nelze uložit prázdný strom.",Toast.LENGTH_LONG).show();
        });

        float_search.setOnClickListener(v -> {
            if(edit_search.getText().length() > 0){
                BinarySearchTree tree = new BinarySearchTree();
                BinarySearchTree.SearchResult res = tree.search(node, Integer.parseInt(edit_search.getText().toString()));

                DialogInterface dialog = Template.getDialogFor(this,
                        Template.DIALOG_SEARCH_RESULT, res);
                if(dialog instanceof AlertDialog){
                    ((AlertDialog) dialog).show();
                }else if(dialog instanceof BottomSheetDialog){
                    ((BottomSheetDialog) dialog).show();
                }else Toast.makeText(this,
                        getString(R.string.dialog_message_search_result_positive)
                                .replace("%num%", String.valueOf(res.value))
                                .replace("%amount%", String.valueOf(res.amount)),
                        Toast.LENGTH_LONG).show();
            }
        });

        float_add.setOnClickListener(v -> {
            if(edit_add.getText().length() > 0){
                BinarySearchTree tree = new BinarySearchTree();
                node = tree.insert(node, Integer.parseInt(edit_add.getText().toString()));
                update();
            }
        });

        float_remove.setOnClickListener(v -> {
            if(edit_remove.getText().length() > 0){
                BinarySearchTree tree = new BinarySearchTree();
                node = tree.remove(node, Integer.parseInt(edit_remove.getText().toString()));
                update();
            }
        });

        edit_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                Utilities.hideKeyboard(this);
                float_search.callOnClick();
                return true;
            }
            return false;
        });

        edit_add.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                Utilities.hideKeyboard(this);
                float_add.callOnClick();
                return true;
            }
            return false;
        });

        edit_remove.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                Utilities.hideKeyboard(this);
                float_remove.callOnClick();
                return true;
            }
            return false;
        });

        edit_search.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER){
                Utilities.hideKeyboard(this);
                float_search.callOnClick();
                return true;
            }
            return false;
        });

        edit_add.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER){
                Utilities.hideKeyboard(this);
                float_add.callOnClick();
                return true;
            }
            return false;
        });

        edit_remove.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER){
                Utilities.hideKeyboard(this);
                float_remove.callOnClick();
                return true;
            }
            return false;
        });

        locked.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Proč je tento strom uzamčený?");
            builder.setMessage("Tato instance byla vytvořena pomocí generátoru náhodných čísel a pro možné problémy s načítáním stromu jsou editační možnosti vypnuty. Pokud chcete strom upravit, vytvořte si nový strom a uložte jej.");
            builder.setPositiveButton(getString(R.string.action_ok), null);
            builder.show();
        });

        update();
    }

    private void update(){
        if(node != null){
            tree_empty.setVisibility(View.GONE);
            List<Node> nodes = new ArrayList<>();
            nodes.add(node);

            NodeAdapter adapter = new NodeAdapter(this, nodes);
            tree.setLayoutManager(new GridLayoutManager(this, 1));
            tree.setAdapter(adapter);
        }else tree_empty.setVisibility(View.VISIBLE);

        updateActions();
    }

    private void updateActions(){
        if(node != null){
            tree.setVisibility(View.VISIBLE);
            if(!usingSession || isSessionEditable){
                if(!tool_remove.isShown()) tool_remove.show();
                if(!tool_save.isShown()) tool_save.show();
                if(node.size() > Utilities.MAX_TREE_SIZE) tool_add.hide();
                else if(!tool_add.isShown()) tool_add.show();
                if(node.size() < 1) tool_search.hide();
                else if(!tool_search.isShown()) tool_search.show();
            }
        }else{
            tree.setVisibility(View.GONE);
            if(!usingSession || isSessionEditable){
                tool_remove.hide();
                tool_save.hide();
                tool_add.show();
                tool_search.hide();
            }

            isRemoveOpen = false;
        }

        if(isSearchOpen && tool_search.isShown()){
            if(tool_save.isShown()) tool_save.hide();
            if(tool_remove.isShown()) tool_remove.hide();
            if(tool_add.isShown()) tool_add.hide();

            edit_search.setText("");
            floating_layout_search.setVisibility(View.VISIBLE);
            tool_search.setImageResource(R.drawable.ic_baseline_close_24);
        }else{
            isSearchOpen = false;
            floating_layout_search.setVisibility(View.GONE);
            tool_search.setImageResource(R.drawable.ic_baseline_search_24);
        }

        if(isAddOpen && tool_add.isShown()){
            if(tool_save.isShown()) tool_save.hide();
            if(tool_remove.isShown()) tool_remove.hide();
            if(tool_search.isShown()) tool_search.hide();

            edit_add.setText("");
            floating_layout_add.setVisibility(View.VISIBLE);
            tool_add.setImageResource(R.drawable.ic_baseline_close_24);
        }else{
            isAddOpen = false;
            floating_layout_add.setVisibility(View.GONE);
            tool_add.setImageResource(R.drawable.ic_baseline_add_24);
        }

        if(isRemoveOpen && tool_remove.isShown()){
            if(tool_save.isShown()) tool_save.hide();
            if(tool_add.isShown()) tool_add.hide();
            if(tool_search.isShown()) tool_search.hide();

            edit_remove.setText("");
            floating_layout_remove.setVisibility(View.VISIBLE);
            tool_remove.setImageResource(R.drawable.ic_baseline_close_24);
        }else{
            isRemoveOpen = false;
            floating_layout_remove.setVisibility(View.GONE);
            tool_remove.setImageResource(R.drawable.ic_baseline_remove_24);
        }
    }

    @Override
    public void onBackPressed() {
        if(!usingSession && node != null){
            AlertDialog.Builder save = new AlertDialog.Builder(this);
            save.setTitle("Uložit strom");
            save.setMessage("Chcete uložit tento strom jako novou instanci?");
            save.setPositiveButton(getString(R.string.action_save), (dialog, which) -> {
                List<Integer> list = node.toList();

                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH) + 1;
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int mi = calendar.get(Calendar.MINUTE);
                String session = y + "-" + m + "-" + d + " " + h + "-" + mi;

                numberManager.pushList(session, list);
                super.onBackPressed();
            });
            save.setNegativeButton(getString(R.string.action_delete), (dialog, which) -> super.onBackPressed());
            save.setNeutralButton(getString(R.string.action_cancel), null);
            save.show();
        }else if(usingSession && isSessionEditable && node != null) {
            AlertDialog.Builder save = new AlertDialog.Builder(this);
            save.setTitle("Uložit strom");
            save.setMessage("Chcete uložit tento strom?");
            save.setPositiveButton(getString(R.string.action_save), (dialog, which) -> {
                List<Integer> list = node.toList();
                numberManager.pushList(intent.getStringExtra("session"), list);
                super.onBackPressed();
            });
            save.setNegativeButton(getString(R.string.action_delete), (dialog, which) -> super.onBackPressed());
            save.setNeutralButton(getString(R.string.action_cancel), null);
            save.show();
        }else super.onBackPressed();
    }
}