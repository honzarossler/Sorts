# O MergeSort algoritmu

Tento algoritmus je stabilní typu *rozděl a panuj* se složitostí $$ O(n \cdot log n) $$.

MergeSort pracuje na bázi slévání již seřazených částí pole za pomoci dodatečného pole a byl vymyšlen v roce 1945 *Johnem von Neumannem*.

## Princip

MergeSort je komplexnější na úrovni práce s více částmi. Algoritmus si rekurzivně rozděluje pole na poloviny dokud nenarazí na smyšlené pole o velikosti jednoho prvku, 
které je už samo o sobě seřazené. Následně probíhá slévání prvků tak, že kontroluje hodnotu prvků v obou polovin a vytvoří setříděný celek.

Po slévání posledních dvou polovin vzniká plně seřazené pole od nejmenší po největší prvkem.