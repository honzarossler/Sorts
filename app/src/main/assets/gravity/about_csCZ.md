# O GravitySort algoritmu

Také bývá nazývaný jako BeadSort, je přirozený třídicí algoritmus. Byl inspirován přírodními jevy a byl navržen
tak, aby pamatoval na objekty jako na korálky, na které spadá vliv gravitace.

## Princip

Tento algoritmus si vykládá čísla jako korálky, které jsou umístěny na provázkách a provozuje podobný jev jako
gravitace.

Toto chování je docíleno rozdělením čísel na součet po jedné, které rozloží v řadě a následně tyto bity posouvá
na poslední možné pozice ve sloupcích. Následně dojde k součtu těchto jednotek a vrátí setříděný seznam.

## Časová složitost

Jedná se o přirozený algoritmus, který v reálném světě reaguje složitostí **O(1)**, avšak v programování je potřeba
zavést způsob k seřazení jednotek v řadách a časová složitost stoupá k $$ O(n^2) $$.