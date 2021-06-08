package cz.janrossler.sorts.sortable;

import android.content.Context;

public class BogoSort extends Sortable{
    public BogoSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void sortNow() throws Exception {
        while(!isSorted()){
            for (int i=1; i < numbers.size(); i++){
                int j = (int)(Math.random() * i);
                swap(i,j);
            }
        }
    }
}
