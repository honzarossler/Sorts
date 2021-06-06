package cz.janrossler.sorts.sortable;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

import cz.janrossler.sorts.utils.NumberManager;

public abstract class Sortable {
    public static final String BUBBLE = "BubbleSort";
    public static final String COUNTING = "CountingSort";
    public static final String QUICK = "QuickSort";
    public static final String MERGE = "MergeSort";
    public static final String BOGO = "BogoSort";
    public static final String RADIX = "RadixSort";
    public static final String HEAP = "HeapSort";
    public static final String SELECTION = "SelectionSort";
    public static final String INSERTION = "InsertionSort";
    public static final String SHAKER = "ShakerSort";
    public static final String GRAVITY = "GravitySort";
    public static final String BUCKET = "BucketSort";

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

    protected abstract void start() throws Exception;

    public void doSort(){
        startTime = Calendar.getInstance();
        try{
            start();
            endTime = Calendar.getInstance();

            if(sortingListener != null) {
                if(isSorted())
                    sortingListener.onSuccessSort(getTime());
                else
                    throw new SortException("Seznam nebyl úspěšně seřazen.");
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            if (sortingListener != null) sortingListener.onFailed("Třídění proběhlo mimo seznam.");
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            if (sortingListener != null) sortingListener.onFailed("Seznam je příliž velký na třídění.");
        }catch (SortException e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.message);
        }catch (Exception e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed("Stala se neočekávaná chyba.");
        }
    }

    public static Sortable getSort(Context context, @NonNull String algorithm, String session){
        Sortable sort = null;

        switch(algorithm){
            case Sortable.BUBBLE:
                sort = new BubbleSort(context, session);
                break;
            case Sortable.QUICK:
                sort = new QuickSort(context, session);
                break;
            case Sortable.MERGE:
                sort = new MergeSort(context, session);
                break;
            case Sortable.COUNTING:
                sort = new CountingSort(context, session);
                break;
            case Sortable.RADIX:
                sort = new RadixSort(context, session);
                break;
            case Sortable.BOGO:
                sort = new BogoSort(context, session);
                break;
            case Sortable.HEAP:
                sort = new HeapSort(context, session);
                break;
            case Sortable.SELECTION:
                sort = new SelectionSort(context, session);
                break;
            case Sortable.INSERTION:
                sort = new InsertionSort(context, session);
                break;
            case Sortable.SHAKER:
                sort = new ShakerSort(context, session);
                break;
            case Sortable.GRAVITY:
                sort = new GravitySort(context, session);
                break;
            case Sortable.BUCKET:
                sort = new BucketSort(context, session);
                break;
        }

        return sort;
    }

    public int getTime(){
        return (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
    }

    public List<Integer> getSortedList() throws Exception {
        if(isSorting) throw new Exception("The numbers are still being sorted.");
        return numbers;
    }

    protected void swap(int i, int j){
        int s = numbers.get(i);
        numbers.set(i, numbers.get(j));
        numbers.set(j, s);
    }

    public boolean isSorted() {
        for (int i = 1; i < numbers.size(); i++)
            if (numbers.get(i) < numbers.get(i - 1))
                return false;
        return true;
    }

    public interface SortingListener {
        void onSuccessSort(int seconds);
        void onFailed(String _message);
    }

    public static class SortException extends Exception{
        String message;
        SortException(String error){
            message = error;
        }
    }
}
