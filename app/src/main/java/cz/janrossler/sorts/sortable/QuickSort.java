package cz.janrossler.sorts.sortable;

import android.content.Context;

public class QuickSort extends Sortable{
    public QuickSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() throws Exception {
        int left = 0;
        int right = numbers.size() - 1;
        sort(left, right);
    }

    private void sort(int left, int right){
        if(right >= left){
            int new_pivot = divide(left, right, left);
            sort(left, new_pivot - 1);
            sort(new_pivot + 1, right);
        }
    }

    private int divide(int left, int right, int pivot){
        swap(pivot, right);
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
