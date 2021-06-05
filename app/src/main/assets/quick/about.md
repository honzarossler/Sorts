# About QuickSort

It is a very fast but unstable sorting algorithm based on the principle *Divide et Impera* (divide and conquer).
The algorithm was invented in 1962 by Sir Charles Antony Richard Hoar.

## Principle

In this algorithm, we choose a random element called **pivot**. Subsequently, we can switch the field so that
that the elements are larger than the pivot on one side and smaller than the pivot on the other.
The pivot itself must be located exactly between these two parts.

We perform this procedure gradually in these groups (without a pivot, it is placed in its place).
We repeat the procedure until we solve all subproblems. At this point, the entire field is sorted from the highest element.

## Time complexity

The performance of the algorithm is determined by the choice of pivot. If we choose it well, we can get the complexity $$ O(n \cdot log_{2} n) $$.
However, if we choose the wrong pivot (ie the smallest or largest element), there will be no division of subproblems.
In this case, the complexity may be $$ O(n^2) $$.

## Pivot choice

There are many options for choosing a pivot. One of the frequently used options is to use the first or last element.