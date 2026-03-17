package zad6;

class Licznik {
    private long wartosc = 0;

    public long get() {
        return wartosc;
    }

    public void incNiesynch() {
        wartosc++;
    }
}