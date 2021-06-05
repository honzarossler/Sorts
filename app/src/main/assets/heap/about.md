# About HeapSort

HeapSort is one of the most efficient sorting algorithms with $$ O(n \cdot log n) $$ complexity and can be even faster than **QuickSort**.
HeapSort creates heaps, which it then works with and sorts.

## Principle

HeapSort always creates another field, which we call a heap, and at the moment it acts as a priority field for sorting, and we sort the elements gradually removed from the heap immediately.
The whole process is better expressed in the following steps:

1. Build a heap from the specified field
2. Heap top tear (highest priority element - smallest/largest element, depends on sorting method)
3. Move the torn element with the last element of the heap
4. Heap reduction by 1
5. Repair of the heap into a mold
6. If the heap has elements, it returns to point 2
7. The fields are arranged in reverse order