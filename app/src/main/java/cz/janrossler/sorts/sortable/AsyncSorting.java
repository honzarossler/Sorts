package cz.janrossler.sorts.sortable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.NumberManager;
import cz.janrossler.sorts.SortingService;

public class AsyncSorting extends AsyncTask<String, String, String> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    public SortingService.Callbacks callbacks;
    public String sortAlgorithm = "";
    private Sortable sort;

    public AsyncSorting(Context context){
        this.context = context;
    }

    protected String doInBackground(@NonNull String... sessions) {
        switch(sortAlgorithm){
            case Sortable.BUBBLE:
                sort = new BubbleSort(context, sessions[0]);
                break;
            case Sortable.QUICK:
                sort = new QuickSort(context, sessions[0]);
                break;
            case Sortable.MERGE:
                sort = new MergeSort(context, sessions[0]);
                break;
            case Sortable.COUNTING:
                sort = new CountingSort(context, sessions[0]);
                break;
            case Sortable.RADIX:
                sort = new RadixSort(context, sessions[0]);
                break;
            case Sortable.BOGO:
                sort = new BogoSort(context, sessions[0]);
                break;
        }

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
                    publishProgress();
                }

                @Override
                public void onFailed(String _message) {
                    publishProgress();
                }
            });
            sort.start();
        }else{
            publishProgress();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(callbacks != null) callbacks.updateClient();
    }
}