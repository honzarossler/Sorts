# O CountingSort algoritmu

Tento algoritmus je velmi výkonný a stabilní se složitostí $$ O(n + k) $$, který vymyslel *Harold Seward* v roce 1954.

CountingSort nepracuje na rozdíl od BubbleSortu nebo QuickSortu na principu porovnávání, ale na bázi výpočtu jejich výskytů.

## Princip

Využívá se znalosti nejmenší a nejvyšší řazené hodnoty, diky které může algoritmus vytvořit pole četností hodnot
a toto pole posléze přepočítat na pole posledních indexů. Řazení probíhá průchodem zprava doleva a ukládání hodnot na správné místo v seřazeném poli.

## Výhody a nevýhody

Obří výhodou algoritmu je jeho složitost $$ O(n + k) $$, která zajistí opravdu plynulé řazení pro obrovské seznamy.

Nevýhodou je zase potřeba vytvoření dodatečkého pole indexů a četností, nebo schopnost řadit pouze diskrétní hodnoty.