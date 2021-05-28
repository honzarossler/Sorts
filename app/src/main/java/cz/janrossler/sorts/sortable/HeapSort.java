package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class HeapSort extends Sortable{
    public HeapSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            int heapEnd = numbers.size();

            for (int i = heapEnd / 2 - 1; i >= 0; i--)
                heapify(heapEnd, i);

            for (int i = heapEnd - 1; i > 0; i--) {
                int nSwipe = numbers.get(0);
                numbers.set(0, numbers.get(i));
                numbers.set(i, nSwipe);

                heapify(i, 0);
            }

            endTime = Calendar.getInstance();
            Log.d("HeapSort", "Done!");

            if(sortingListener != null) {
                int time = (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
                sortingListener.onSuccessSort(time);
            }
        }catch (Exception e){
            e.fillInStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.getMessage());
        }
    }

    private void heapify(int n, int i)
    {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && numbers.get(l) > numbers.get(largest))
            largest = l;

        if (r < n && numbers.get(r) > numbers.get(l))
            largest = r;

        if (largest != i) {
            int nSwipe = numbers.get(i);
            numbers.set(i, numbers.get(largest));
            numbers.set(largest, nSwipe);

            heapify(n, largest);
        }
    }
}
