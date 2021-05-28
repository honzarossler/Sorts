package cz.janrossler.sorts.sortable;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import cz.janrossler.sorts.NumberManager;

public abstract class Sortable {
    public static final String BUBBLE = "BubbleSort";
    public static final String COUNTING = "CountingSort";
    public static final String QUICK = "QuickSort";
    public static final String MERGE = "MergeSort";
    public static final String BOGO = "BogoSort";
    public static final String RADIX = "RadixSort";
    public static final String HEAP = "HeapSort";
    public static final String SELECTION = "SelectionSort";

    protected boolean isSorting = false;
    protected String session;
    protected NumberManager numberManager;
    protected List<Integer> numbers;
    protected SortingListener sortingListener;

    protected Calendar startTime;
    protected Calendar endTime;

    public Sortable(Context context, String session){
        this.session = session;
        numberManager = new NumberManager(context);

        numbers = numberManager.getUnsortedSessionList(session);
    }

    public void setSortingListener(SortingListener listener){
        this.sortingListener = listener;
    }

    @SuppressWarnings("unused")
    public SortingListener getSortingListener(){
        return this.sortingListener;
    }

    public abstract void start();

    public List<Integer> getSortedList() throws Exception {
        if(isSorting) throw new Exception("The numbers are still being sorted.");
        return numbers;
    }

    public interface SortingListener {
        void onSuccessSort(int seconds);
        void onFailed(String _message);
    }
}
