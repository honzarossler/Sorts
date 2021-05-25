package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class BogoSort extends Sortable{
    public BogoSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            while(!isSorted()){
                for (int i=1; i < numbers.size(); i++){
                    int j = (int)(Math.random() * i);
                    int temp = numbers.get(i);
                    numbers.set(i, numbers.get(j));
                    numbers.set(j, temp);
                }
            }

            endTime = Calendar.getInstance();
            Log.d("BogoSort", "Done!");

            if(sortingListener != null) {
                int time = (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
                sortingListener.onSuccessSort(time);
            }
        }catch (Exception e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.getMessage());
        }
    }

    private boolean isSorted() {
        for (int i = 1; i < numbers.size(); i++)
            if (numbers.get(i) < numbers.get(i - 1))
                return false;
        return true;
    }
}
