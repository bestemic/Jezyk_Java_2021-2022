class Decode extends DecoderInterface {

    private int liczba = 0;
    private int X = 0;
    private boolean isX = false;
    private String wynik = "";

    private void isXKnown() {
        if (isX == false) {
            X = liczba;
            isX = true;
        }
    }

    @Override
    public void input(int bit) {
        if (bit == 1) {
            liczba++;
        } else if (liczba != 0) {
            isXKnown();
            wynik = wynik + ((liczba / X) - 1);
            liczba = 0;
        }
    }

    @Override
    public String output() {
        return wynik;
    }

    @Override
    public void reset() {
        liczba = 0;
        X = 0;
        isX = false;
        wynik = "";
    }
}