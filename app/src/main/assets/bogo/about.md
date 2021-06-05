# About BogoSort

BogoSort (StupidSort, SlowSort) is an algorithm that points to the worst possible procedure for sorting elements. It has no practical use,
however, with a low number of elements (up to 5 elements) it can be fast. Otherwise, with 1000 numbers, you may have time to try the whole Netflix.

Thanks to its sorting system, the complexity of $$ O (n \cdot n!) $$ is connected to it, because there is $$ n! $$ possible field permutations.

## Principle

This algorithm checks at each step whether the field is sorted. If the field is not sorted, the whole field is shuffled and everything is done anew.

## Scheme

```
function bogosort(array)
    while !ordered(array)
        shuffle(array)
```