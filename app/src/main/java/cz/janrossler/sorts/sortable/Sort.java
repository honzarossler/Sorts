package cz.janrossler.sorts.sortable;

import android.content.Context;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;

public class Sort {
    /**
     * <p>
     *     Zjistí, která zděděná třída může být použita.
     * </p>
     * @param context Předání kontextu aktivity třídě pro správné zpracovávání.
     * @param algorithm Název algoritmu, potažmo třídy, která je vyhledávána.
     * @param session Název instance, kterou třída bude zpracovávát
     * @return Vrátí třídu třídicího algoritmu rozšiřující {@link Sortable}.
     */
    @NonNull
    public static Sortable getByName(
            @NonNull Context context,
            @NonNull String algorithm,
            @NonNull String session)
            throws Exception{
                // Otevření třídy dle String návzu algoritmu
                Class<?> clazz = Class.forName("cz.janrossler.sorts.sortable." + algorithm);
                // Vytvoření konstruktoru pro tuto třídu
                Constructor<?> constructor = clazz.getConstructor(Context.class, String.class);
                // Návrat této třídy jako instanci Sortable
                return (Sortable) constructor.newInstance(context, session);
    }
}
