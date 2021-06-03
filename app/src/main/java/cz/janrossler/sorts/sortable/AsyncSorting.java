package cz.janrossler.sorts.sortable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;
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
        sort = Sortable.getSort(context, sortAlgorithm, sessions[0]);

        if(!sortAlgorithm.equals("") && sort != null){
            sort.setSortingListener(new Sortable.SortingListener() {
                @Override
                public void onSuccessSort(int seconds) {
                    List<Integer> list = new ArrayList<>();
                    try {
                        list = sort.getSortedList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray array = new JSONArray();

                    for(int i = 0; i < list.size(); i++)
                        array.put(list.get(i));

                    NumberManager numberManager = new NumberManager(context);
                    try {
                        numberManager.saveSortingToSession(sessions[0], sortAlgorithm, seconds, array);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    answers.remove(sortAlgorithm);
                    publishProgress();
                }

                @Override
                public void onFailed(String _message) {
                    answers.put(sortAlgorithm, _message);
                    publishProgress();
                }
            });
            sort.doSort();
        }else{
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
