package cz.janrossler.sorts.utils;

import androidx.annotation.NonNull;

public class Complexity {
    public static final String COMPLEXITY_O_1 = "O(1)";
    public static final String COMPLEXITY_O_N_1_EMARK = "O((N+1)!)";
    public static final String COMPLEXITY_O_N_2 = "O(N^2)";
    public static final String COMPLEXITY_O_N = "O(N)";
    public static final String COMPLEXITY_O_N_LOG_N = "O(N log(N))";
    public static final String COMPLEXITY_O_N_B_LOGB_K = "O((N+B) * logB(k))";

    private int size;

    public Complexity(int size){
        this.size = size;
    }

    public double getTimeComplexity(@NonNull String compl){
        switch (compl){
            case COMPLEXITY_O_N:
                return size;
            case COMPLEXITY_O_N_2:
                return size * size;
            case COMPLEXITY_O_N_LOG_N:
                return size * Math.log(size);
            case COMPLEXITY_O_N_B_LOGB_K:
                return (size + 10) * Math.log(size);
            case COMPLEXITY_O_N_1_EMARK:
                int fact = 1;
                for(int i = 1; i <= size; i++)
                    fact *= i;

                return fact;
            case COMPLEXITY_O_1:
            default:
                return 1.;
        }
    }
}
