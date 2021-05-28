# O HeapSort algoritmu

HeapSort je jedním z nejvíce nejefektivnějších řadicích algoritmů se složitostí $$ O(n \cdot log n) $$ a může být i rychlejší oproti **QuickSortu**. 
HeapSort podle svého názvu *(heap -> halda)* vytváří haldy, se kterými následně pracuje.

## Princip

HeapSort vždy vytváří další pole, kterému říkáme halda a v tuto chvíli se chová jako prioritní pole pro řazení a z haldy postupně odebírané prvky rovnou řadíme. 
Celý postup se lépe vyjadřuje v následujících krocích:

1. Sestavení haldy ze zadaného pole
2. Utrhnutí vrcholu haldy (prvek s nejvyšší prioritou - nejmenší/největší prvek, záleží na způsobu řazení)
3. Proházení utrhnutého prvku s posledním prvkem haldy
4. Zmenšení haldy o 1
5. Oprava haldy do splňované formy
6. Pokud má halda prvky, vrátí se k bodu 2
7. Pole je seřazeno v opačném pořadí