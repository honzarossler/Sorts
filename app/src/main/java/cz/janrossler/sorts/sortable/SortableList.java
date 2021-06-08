package cz.janrossler.sorts.sortable;

public interface SortableList {
    /**
     * <p>
     *     Metoda swap() prohodí v listu, seznamu nebo poli položky na indexech i, j.
     * </p>
     * @param i První index listu
     * @param j Druhý index listu
     */
    void swap(int i, int j);

    /**
     * <p>
     *     Zkontroluje, zda je seznam správně seřazen.
     * </p>
     * @return Vrátí, zda je seznam seřazen.
     */
    boolean isSorted();

    /**
     * <p>Metoda, která by měla zachycovat chyby a volat sortNow().</p>
     */
    void start();

    /**
     * <p>
     *     Tato metoda obsahuje samotný proces třídění. Nedoporučuje se používat tuto metodu samostatně.
     * </p>
     * @throws Exception Pokud se něco pokazí, např. chyby s indexy, nebo nedostatečná paměť
     */
    void sortNow() throws Exception;
}
