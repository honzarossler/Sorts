# Aplikace Tříditelna

Tato aplikace vznikla jako práce ke zkoušce z Algoritmů a datových struktur, přičemž může být inspirací pro další studenty, kteří by chtěli vytvořit podobný projekt nebo tento použít jako šablonu.

Aplikace je veřejná a vydána jako open-source v jazyce [Java](https://www.java.com/en/), ale mám v plánu udělat i větev v Kotlinu. Byla napsána v [Android Studio Arctic Fox | 2020.3.1 Beta 3](https://developer.android.com/studio/preview) a může být psána v [Jetpack Compose](https://developer.android.com/jetpack/compose).

## Obsah aplikace

### Třídicí algoritmy

Aplikace obsahuje celkem 13 třídicích algoritmů:

* BogoSort
* BubbleSort
* BucketSort
* CountingSort
* GravitySort
* HeapSort
* InsertionSort
* MergeSort
* QuickSort
* RadixSort
* SelectionSort
* ShakerSort
* ShellSort

### Teorie algoritmů

Protože jsem věnoval aplikaci třídicím algoritmům, rozhodl jsem se přidat teorii inspirovanou stránkami [Algoritmy.net](https://algoritmy.net/), [GeeksForGeeks](https://www.geeksforgeeks.org/fundamentals-of-algorithms/?ref=shm) a [Wikipedia](https://cs.wikipedia.org).

Teorie podporuje vícejazyčných verzí a je čtena z Assets složky v projektu aplikace. Bohužel teorie je k dispozici pouze od přibližně 10 algoritmů a jen v jazycích čeština a angličtina.

### Binární vyhledávací strom

Pro vyhledávání a vykreslení jsem vytvořil tuto strukturu dle výukových materiálů z hodin Algoritmů a datových struktur. Vyhledávání funguje na velmi vysoká čísla, ale pro vykreslení je potřeba velmi mnoho paměti a lze tak vypsat jen příbližně 1500 čísel.

## Porozumění projektu

Aplikace obsahuje spoustu částí, které jsem buď vytvořil sám nebo za pomoci fór na internetu (např. [StackOverFlow](https://stackoverflow.com), [Quora](https://www.quora.com/), ...). Je proto důležité, pokud budete chtít stavět na tomto projektu, nebo jen pomoct s vylepšením tohoto projektu, abyste lépe pochopili jeho části.

### Systém ukládání algoritmů

Aplikace používá pro správu instance čísel, kterých je nyní možné vytvořit nekonečně mnoho, ale jste limitováni velikostí instancí na 2 000 000 čísel. Tyto instance zpracovává třída `cz.janrossler.sorts.utils.NumberManager`, která obsahuje detailně popsané fungování této třídy a jeho metod.

#### Seznam algoritmů a pochopení jeho obsahu

Všechny algoritmy jsou uloženy v `app/src/assets/sorts.json`, jedná se o seznam s pravidly pro použití algoritmů v aplikaci. Každý algoritmus má takto podobnou strukturu (Vysvětlivky níže): 

```json
{
  "name": "TridiciAlgoritmus",
  "theory": "theory.algoritmus.json",
  "enabled": true,
  "recommended_length": {
    "max": 100000,
    "max_alloc": 100000000
  },
  "hints": {
    "_": "English hint",
    "cs_CZ": "Česká nápověda"
  }
}
```

Každé z těchto klíčů má jasné poselství:
* ``name`` - Název třídy v `cz.janrossler.sorts.sortable` rozšířenou o `cz.janrossler.sorts.sortable.Sortable`, kterou aplikace definuje pro třídění a zobrazovací název v seznamu algoritmů.
* ``theory``
  * Odkazuje na JSON soubor ve stejné složce, který obsahuje informace pro teorii v různých jazycích.
  * Tento klíč je nepovinný, nebo je ignorován, pokud odkazovaný soubor neexistuje.
* ``enabled`` - Určuje, zda bude tento algoritmus ignorován, neplatí pro teorie. Tento klíč je nepovinný.
* ``recommended_length`` - Obsahuje kritické podmínky
  * ``max`` - Maximální počet čísel v seznamu. Po překročení této hodnoty bude algoritmus ignorován.
  * ``max_alloc`` - Některé algoritmy způsobují přetečení zásobníku paměti a vyžadují maximální povolenou alokovanou velikost seznamu. Po překročení této hodnoty bude algoritmus ignorován.
* ``hints`` - Obsahuje klíče jazyků, pro které se tato informace objeví v nabídce teorií.
  * Klíč `_` - Tento klíč je volán, když neexistuje jazyková mutace. Doporučuji zde aplikovat anglickou nápovědu.

#### Proces třídění

Samotný výběr a třídění pomocí algoritmů neimplementuje žádné konkrétní třídy a nefixuje určitý seznam algoritmů. Pro volání potřebné třídy je voláno jméno algoritmu z předchozí části a to za pomoci `cz.janrossler.sorts.sortable.Sort.getByName()`. Samotný postup je popsán zde:

1. `cz.janrossler.sorts.SessionActivity.update()` odešle požadavek s názvem instance (`session`) a zvoleného algoritmu (`use-sort`) do `cz.janrossler.sorts.utils.SortingService`
2. `cz.janrossler.sorts.utils.SortingService` založí asynchronní volání `cz.janrossler.sorts.sortable.AsyncSorting` a předá mu komunikaci s aktivitou, instanci a algoritmus
3. `cz.janrossler.sorts.sortable.AsyncSorting` definuje pomocí `cz.janrossler.sorts.sortable.Sort.getByName()` danou třídu rozšiřující `cz.janrossler.sorts.sortable.Sortable`
4. `cz.janrossler.sorts.sortable.AsyncSorting` spustí algoritmus příkazem `Sortable.start()`, která obsahuje podchycení výjimek, přetečení zásobníku, časovač a potřebné metody pro třídění
5. Třída rozšířena o `cz.janrossler.sorts.sortable.Sortable` vykoná třídění, vrátí odpověď a `cz.janrossler.sorts.sortable.AsyncSorting` uloží seřazená čísla a oznámí aktivitě, že bylo dokončeno třídění.

### Systém zobrazování teorií

Aplikace v základu čte a zpracovává JSON soubory teorií z `app/src/assets/sorts.json` v klíči `theory`, kde se nachází detailnější údaje pro vykreslení v `cz.janrossler.sorts.TheoryActivity`. Struktura souborů JSON by měla být následující:

```json
{
  "head": {
    "title": {
      "text": "Třídicí Algoritmus"
    },
    "folder": "algoritmus"
  },
  "body": {
    "_": [
      {
        "text": "%folder%/about.md",
        "image": "%folder%/some_image.gif"
      }
    ],
    "cs_CZ": [
      {
        "text": "%folder%/about_csCZ.md",
        "image": "%folder%/some_image.gif"
      }
    ]
  }
}
```

Kde se nachází spoustu klíčů, které by měly být správně pochopeny:
* ``head`` - Obsahuje klíčové prvky pro správnou funkčnost během zpracovávání klíče ``body``.
  * ``title`` - Obsahuje nastavení spojené se záhlavím na stránce s teorií, do budoucna by zde mohly být další parametry, např. tématická barva, podnadpis, ...
  * ``folder`` - jedná se o root složku s daty teorie, např. jazykové mutace, obrázky, ...
* ``body`` - Obsahuje klíče nazvané dle kódu jazyka a klíč ``_``, který bude vždy vyvolán, když bude chybět jazyková mutace. Všechny tyto klíče obsahují seznam
  * ``text`` - Obsahuje odkaz na Markdown soubor s teorií
  * ``image`` - Obsahuje odkaz na obrázky

#### Zobrazení seznamu teorií

Všechny teorie se vypisují pouze v `cz.janrossler.sorts.ui.TheoryFragment` za pomoci `cz.janrossler.sorts.utils.TheoryReader`, který přečte `app/src/assets/sorts.json` a rozezná, zda existuje klíč ``theory`` a zda existuje soubor, na který tento klíč odkazuje. Po jeho úspěšném nalezení vybere jazykovou mutaci a přizpůsobí nápovědu pro výpis. Nakonec získaná data jsou otevřeny za pomoci ``cz.janrossler.sorts.adapter.TheoryListAdapter`` v ``RecyclerView``.

#### Zobrazení jedné teorie

Všechny teorie se otevírají přes `cz.janrossler.sorts.TheoryActivity`, kterou je nutné volat přes `Intent` s extra klíčem `theory`, který obsahuje hlavní soubor teorie. Následně proběhne získání teorie v jazykové mutaci za pomoci `cz.janrossler.sorts.utils.Theory`, který si sám načte data přes `cz.janrossler.sorts.utils.TheoryReader`.

### Další užitečné informace

Aplikace je stále ve vývoji a některé části kódu mohou být razantně změněny, odstraněny některé části, přidány další podmínky, vylepšení struktur JSON souborů, nebo budou vyžadovat zvláštní přístup.

Dále je v plánu změnit přístup k datům a udělat efektivnější zpracovávání velkým dat. Aktuálně je omezen seznam jen na 2 000 000 čísel a určitě by se tento limit měl navýšit na minimálně 10 000 000 čísel aniž by došlo na selhání při přetečení zásobníku.

Vykreslování stromu je omezeno na 1 500 čísel z důvodu dlouhého zpracovávání v `RecyclerView`. V plánu je optimalizace a efektivnější načítání položek a dosažení bezproblémového načítání až 100 000 čísel.

Budu rád, pokud narazíte na chybu, abyste vytvořili issue nebo chybu sami opravili a nominovali na sloučení s projektem. Je mi přednější opravit chyby způsobující nefunkčnost aplikace.
