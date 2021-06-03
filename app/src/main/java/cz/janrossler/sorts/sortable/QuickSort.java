package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class QuickSort extends Sortable{
    public QuickSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            int left = 0;
            int right = numbers.size() - 1;
            sort(left, right);

            endTime = Calendar.getInstance();
            Log.d("QuickSort", "Done!");

            if(sortingListener != null) {
                int time = (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
                sortingListener.onSuccessSort(time);
            }
        }catch (Exception e){
            e.fillInStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.getMessage());
        }
    }

    private void sort(int left, int right){
        if(right >= left){
            int new_pivot = divide(left, right, left);
            sort(left, new_pivot - 1);
            sort(new_pivot + 1, right);
        }
    }

    private int divide(int left, int right, int pivot){
        int temp = numbers.get(pivot);
        numbers.set(pivot, numbers.get(right));
        numbers.set(right, temp);
        int i = left;
        for (int j = left; j < right; j++) {
            if (numbers.get(j) < numbers.get(right)) {
                swap(i, j);
                i++;
            }
        }
        swap(i, right);
        return i;
    }
}
