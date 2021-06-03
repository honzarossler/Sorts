package cz.janrossler.sorts.sortable;

import android.content.Context;

public class HeapSort extends Sortable{
    public HeapSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() throws Exception {
        int heapEnd = numbers.size();

        for (int i = heapEnd / 2 - 1; i >= 0; i--)
            heapify(heapEnd, i);

        for (int i = heapEnd - 1; i > 0; i--) {
            swap(i, 0);
            heapify(i, 0);
        }
    }

    private void heapify(int n, int i)
    {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && numbers.get(l) > numbers.get(largest))
            largest = l;

        if (r < n && numbers.get(r) > numbers.get(largest))
            largest = r;

        if (largest != i) {
            swap(largest, i);
            heapify(n, largest);
        }
    }
}
