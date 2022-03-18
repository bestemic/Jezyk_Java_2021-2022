public abstract class DecoderInterface {
    /**
     * Metoda pozwala na dostarczanie danych do zdekodowania. Pojedyncze wywoĹanie
     * metody dostarcza jeden bit.
     *
     * @param bit Argumentem wywoĹania jest dekodowany bit. Argument moĹźe przybraÄ
     *            wartoĹci wyĹÄcznie 0 i 1.
     */
    public abstract void input(int bit);

    /**
     * Metoda zwraca odkodowane dane. Metoda nigdy nie zwraca null. JeĹli jeszcze
     * Ĺźadna liczba nie zostaĹa odkodowana metoda zwraca "" (pusty ciÄg znakĂłw,
     * czyli ciÄg znakĂłw o dĹugoĹci rĂłwnej 0).
     *
     * @return CiÄg znakĂłw reprezentujÄcy sekwencjÄ odkodowanych danych.
     */
    public abstract String output();

    /**
     * Metoda przywraca stan poczÄtkowy. Proces odkodowywania danych zaczyna siÄ od
     * poczÄtku.
     */
    public abstract void reset();
}