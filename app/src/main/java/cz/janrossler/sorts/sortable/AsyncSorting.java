package cz.janrossler.sorts.sortable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import cz.janrossler.sorts.utils.NumberManager;
import cz.janrossler.sorts.utils.SortingService;

@SuppressWarnings("deprecation")
public class AsyncSorting extends AsyncTask<String, String, String> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    public SortingService.Callbacks callbacks;
    public String sortAlgorithm = "";
    private Sortable sort;

    private final HashMap<String, String> answers = new HashMap<>();

    public AsyncSorting(Context context){
        this.context = context;
    }

    protected String doInBackground(@NonNull String... sessions) {
        try {
            // Získání třídicího algoritmu podle názvu ze JSON souboru
            sort = Sort.getByName(context, sortAlgorithm, sessions[0]);

            if (!sortAlgorithm.equals("")) {
                sort.setSortingListener(new Sortable.SortingListener() {
                    @Override
                    public void onChunkSorted(int milliseconds, int[] index) {
                        try {
                            List<Integer> list = sort.getSortedList();
                            JSONArray array = new JSONArray();
                            list.forEach(array::put);

                            NumberManager numberManager = new NumberManager(context);
                            numberManager.saveMergedChunks(
                                    sessions[0], sortAlgorithm, milliseconds, array, index);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSorted() {
                        publishProgress();
                    }

                    @Override
                    public void onFailed(String _message) {
                        answers.put(sortAlgorithm, _message);
                        publishProgress();
                    }
                });
                sort.start();
            } else {
                publishProgress();
            }
        }catch (Exception e){
            e.printStackTrace();
            answers.put(sortAlgorithm, "Class not exists.");
            publishProgress();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(callbacks != null) {
            if(answers.containsKey(sortAlgorithm)){
                callbacks.sortFailed(answers.get(sortAlgorithm));
            }else callbacks.updateClient();
        }
    }
}
