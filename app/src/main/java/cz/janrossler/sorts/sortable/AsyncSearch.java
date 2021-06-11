package cz.janrossler.sorts.sortable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cz.janrossler.sorts.utils.BinarySearchTree;
import cz.janrossler.sorts.utils.Node;
import cz.janrossler.sorts.utils.NumberManager;
import cz.janrossler.sorts.utils.Session;

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
        Session session = numberManager.getSession(this.session);
        List<String> chunks = session.getChunks();
        res = new BinarySearchTree.SearchResult();
        res.value = search[0];

        for(int i = 0; i < chunks.size(); i++){
            Node node = BinarySearchTree.createFromList(numberManager.getUnsortedSessionList(this.session, i));
            BinarySearchTree.SearchResult pres = tree.search(node, search[0]);
            if(pres.found) {
                res.amount += pres.amount;
                res.found = true;
            }
        }

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
