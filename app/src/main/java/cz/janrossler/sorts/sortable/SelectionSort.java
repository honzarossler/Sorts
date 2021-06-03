package cz.janrossler.sorts.sortable;

import android.content.Context;

public class SelectionSort extends Sortable{
    public SelectionSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() throws Exception {
        int size = numbers.size();

        for (int i = 0; i < size - 1; i++) {
            int min_index = i;
            for (int j = i + 1; j < size; j++) {
                if (numbers.get(j) < numbers.get(min_index))
                    min_index = j;
            }

            swap(i, min_index);
        }
    }
}
