package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class InsertionSort extends Sortable{
    public InsertionSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            int size = numbers.size();
            for(int i = 0; i < size; i++){
                int key = numbers.get(i);
                int j = i - 1;
                while(j >= 0 && numbers.get(j-1) > key){
                    numbers.set(j + 1, numbers.get(j));
                    j--;
                }
                numbers.set(j, key);
            }

            endTime = Calendar.getInstance();
            Log.d("InsertionSort", "Done!");

            if(sortingListener != null) {
                int time = (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
                sortingListener.onSuccessSort(time);
            }
        }catch (Exception e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.getMessage());
        }
    }
}
