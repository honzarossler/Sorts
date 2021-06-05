# O RadixSort algoritmu

Jedná se o stabilní řadicí algoritmus, který je vhodný především pro řazení polí s vysokým množstvím totožných čísel.

## Princip

RadixSort funguje narozdíl od ostatních algoritmů tak, že sám využívá jiný stabilní třídicí algoritmus, nejčastěji se jedná o CountingSort.
Volání jiného algoritmu je však systématické. Seřazování probíhá dle míst samotných čísel (např. podle jednotek, desítek, stovek, ...)
a následně jsou všechna čísla seřazená dle všech míst čísel.

## Složitost algoritmu

Zde se nachází tzv. asymptotická složitost. Ta se může lišit dle různých kritérií, často se značí $$ O((n+b) \cdot log_b(k)) $$,
obecně ji lze zapsat i jako $$ O(m \cdot C(n)) $$, kde $$ C(n) $$ je složitost algoritmu, který RadixSort používá.