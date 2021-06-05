# About InsertionSort

It is a stable sorting algorithm that works on the principle of comparing sorted values with the complexity $$ O(n^2) $$.

## Principle

This algorithm divides the list into 2 parts, sorted and unsorted, where the first element from the unordered part always sorts correctly
to an already sorted section, and as soon as no unsorted element remains, the list is sorted.

## Time complexity

Although the time complexity $$ O(n^2) $$ is given, it ceases to apply with an almost sorted sheet, where the complexity $$ O(n) $$.