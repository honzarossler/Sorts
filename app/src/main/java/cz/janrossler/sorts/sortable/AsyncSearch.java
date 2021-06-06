package cz.janrossler.sorts.sortable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.janrossler.sorts.utils.BinarySearchTree;
import cz.janrossler.sorts.utils.Node;
import cz.janrossler.sorts.utils.NumberManager;

public class AsyncSearch extends AsyncTask<Integer, String, String> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private Result result;
    private String session;
    private BinarySearchTree.SearchResult res = new BinarySearchTree.SearchResult();

    public AsyncSearch(Context context, String session, @Nullable Result result){
        this.context = context;
        this.result = result;
        this.session = session;
    }

    protected String doInBackground(@NonNull Integer... search) {
        BinarySearchTree tree = new BinarySearchTree();
        NumberManager numberManager = new NumberManager(context);
        Node node = BinarySearchTree.createFromList(numberManager.getUnsortedSessionList(session));
        res = tree.search(node, search[0]);

        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(result != null) result.done(res);
    }

    public interface Result {
        void done(BinarySearchTree.SearchResult sResult);
    }
}
