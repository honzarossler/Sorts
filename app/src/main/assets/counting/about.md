# About CountingSort

This algorithm is very powerful and stable with the complexity $$ O(n + k) $$ invented by *Harold Seward* in 1954.

Unlike BubbleSort or QuickSort, CountingSort does not work on the principle of comparison, but on the basis of calculating their occurrences.

## Principle

Knowledge of the smallest and highest sorted values is used, thanks to which the algorithm can create an array of frequencies of values
and then recalculate this field to the last index field. Sorting is done by going from right to left and saving the values to the right place in the sorted field.

## Advantages and disadvantages

A huge advantage of the algorithm is its complexity $$ O(n + k) $$, which ensures a really smooth sorting for huge lists.

The disadvantage is the need to create an additional array of indexes and frequencies, or the ability to sort only discrete values.