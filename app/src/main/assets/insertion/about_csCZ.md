# O InsertionSort algoritmu

Jedná se o stabilní řadicí algoritmus, který funguje na principu porovnávání řazených hodnot se složitostí $$ O(n^2) $$.

## Princip

Tento algoritmus dělí seznam na 2 části, seřazenou a neseřazenou, kde vždycky první prvek z neseřazené časti správně zařadí
do již seřazené části a jakmile nezůstane žádný neseřazený prvek, seznam je seřazen.

## Časová složitost

Ačkoli se uvádí časová složitost $$ O(n^2) $$, přestává to platit s téměř seřazeným listem, kde je složitost $$ O(n) $$.