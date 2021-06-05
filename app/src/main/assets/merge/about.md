# About MergeSort

This algorithm is of stable type *divide and conquer* with complexity $$ O(n \cdot log n) $$.

MergeSort works by merging already arranged parts of a field with the help of an additional field and was invented in 1945 by *John von Neumann*.

## Principle

MergeSort is more complex at the multi-part level. The algorithm recursively splits the field in half until it encounters a fictional field the size of one element,
which is already sorted by itself. Subsequently, the elements are merged by checking the value of the elements in both halves and forming a sorted unit.

After merging the last two halves, a fully aligned field is created from the smallest to the largest element.