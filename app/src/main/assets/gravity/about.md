# About GravitySort

Also called BeadSort, it is a natural sorting algorithm. It was inspired by natural phenomena and was designed
so that he can think of objects as beads that are affected by gravity.

## Principle

This algorithm interprets numbers as beads that are placed on strings and operates a similar phenomenon as
gravitation.

This behavior is achieved by dividing the numbers into a sum one by one, which arranges them in a row and then shifts these bits
to the last possible position in the columns. Subsequently, these units are summed and the sorted list is returned.

## Time complexity

It is a natural algorithm that reacts in the real world with the complexity of **O(1)**, but in programming it is necessary
introduce a way to arrange the units in rows, and the time complexity increases to $$ O(n^2) $$.