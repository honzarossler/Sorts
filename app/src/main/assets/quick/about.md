# O QuickSort algoritmu

Jedná se o velmi rychlý, ale nestabilní řadící algoritmus na principu *Divide et Impera* (rozděl a panuj). 
Algoritmus byl vymyšlen v roce 1962 Sirem Charlesem Antonym Richard Hoarem.

## Princip

V tomto algoritmu volíme náhodný prvek, kterému říkáme **pivot**. Následně můžeme pole přeházet tak, 
aby prvky na jedné straně byly větší než pivot a na druhé menší než pivot.
Pivot samotný musí být umístěn přesně mezi tyto dvě části.

Tento postup vykonáváme postupně v těchto skupinách (bez pivota, ten je umístěn na svém místě). 
Postup opakujeme tak dlouho, dokud nevyřešíme všechny podproblémy. V tento okamžik je celé pole seřazeno od nejvyššího prvku.

## Složitost algoritmu

Výkon algoritmu je dána volbou pivota. Pokud jej dobře zvolíme, můžeme získat složitost $$ O(n \cdot log_{2}n) $$.
Pokud však zvolíme špatný pivot (tzn. nejmenší nebo největší prvek), nedojde k rozdělování podproblémů. 
V tomto případě může být složitost $$ O(n^2) $$.

## Volba pivota

Pro volbu pivota existuje spoustu možností. Jednou z často používaných možností je použít první nebo poslední prvek.