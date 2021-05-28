# O BubbleSort algoritmu

Jedná se o jednoduchý a stabilní řadící algoritmus.

## Princip

Svůj název získal díky způsobu řazení, který se dá přirovnat k bublinkám, které pokud jsou lehčí, ve vodě stoupají rychleji.
Porovnává postupně dvě čísla vedle sebe a menší vždy prohodí tak, aby byl blíže ke konci seznamu a se stejnou logikou pokračuje na dalších indexech.
Na konci iterace je tímto způsobem na konci seznamu seřazené nejmenší číslo (tedy na svém místě) a začíná nová iterace bez této poslední iterace.

## Složitost algoritmu

BubbleSort patří do skupiny složitosti $$ O(n^2) $$, přičemž se jedná o časově velmi náročný algoritmus.
Doba řazení na 1 000 000 čísel se odhaduje od 45 minut po 2,5 hodiny závisle na výkonu zařízení.