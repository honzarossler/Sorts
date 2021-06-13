```java
import androidx.annotation.NonNull;

class BogoSort {
    public BogoSort() {
        // Vytvoříme náhodné pole čísel
        int[] a = {3, 2, 5, 1, 0, 4};

        // Definujeme třídu BogoSort
        BogoSort ob = new BogoSort();

        // Spustíme třídění
        ob.bogoSort(a);

        // Vypíšeme seřazená čísla
        System.out.print("Seřazené pole: ");
        ob.printArray(a);
    }

    void bogoSort(int[] a) {
        // Neustále opakujeme cyklus dokud nebude pole seřazeno
        while (!isSorted(a))
            shuffle(a);
    }

    void shuffle(int[] a) {
        // Zamícháme čísla v poli
        for (int i = 1; i <= n; i++)
            swap(a, i, (int) (Math.random() * i));
    }

    void swap(@NonNull int[] a, int i, int j) {
        // Prohodíme 2 čísla na indexích
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    boolean isSorted(@NonNull int[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i] < a[i - 1])
                return false;
        return true;
    }

    void printArray(@NonNull int[] arr) {
        for (int j : arr)
            System.out.print(j + " ");
        System.out.println();
    }
}
```