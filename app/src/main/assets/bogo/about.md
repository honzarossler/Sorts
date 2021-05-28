# O BogoSort algoritmu

BogoSort (StupidSort, SlowSort) je algoritmus, který poukazuje na nejhorší možný postup při řazení prvků. Nemá žádné praktické využití, 
avšak při nízkém počtu prvků (do 5 prvků) může být rychlý. V opačném případě při 1000 číslech stihnete možná zkouknout celou Ulici.

Díky svému systému řazení se k němu váže složitost $$ O(n \cdot n!) $$, protože zde existuje $$ n! $$ možných permutací pole.

## Princip

Tento algoritmus v každém svém kroku kontroluje, zda je pole seřazeno. Pokud pole seřazeno není, celé pole promíchá a probíhá vše na novo.

## Pseudokód

```
function bogosort(array)
    while !ordered(array)
        shuffle(array)
```