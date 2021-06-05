# About RadixSort

It is a stable sorting algorithm, which is especially suitable for sorting fields with a high number of identical numbers.

## Principle

Unlike other algorithms, RadixSort works by using another stable sorting algorithm, most often CountingSort.
However, calling another algorithm is systematic. Sorting takes place according to the places of the numbers themselves (eg according to units, tens, hundreds, ...)
and then all numbers are sorted according to all places of the numbers.

## Time complexity

Here is the so-called asymptotic complexity. This can vary according to various criteria, often denoted by $$ O((n + b) \cdot log_b(k)) $$,
in general, it can also be written as $$ O(m \cdot C(n)) $$, where $$ C(n) $$ is the complexity of the algorithm used by RadixSort.