package cz.janrossler.sorts.sortable;

import android.content.Context;

public class InsertionSort extends Sortable{
    public InsertionSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void sortNow() throws Exception {
        int size = numbers.size();
        for(int i = 1; i < size; i++){
            int j = i - 1;
            int key = numbers.get(i);
            while(j > -1 && key < numbers.get(j)){
                numbers.set(j + 1, numbers.get(j));
                j--;
            }
            numbers.set(j + 1, key);
        }
    }
}
