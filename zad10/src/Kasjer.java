import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

class Kasjer implements KasjerInterface {
    private List<Pieniadz> kasa = new ArrayList<>();
    private RozmieniaczInterface rozmieniacz;

    private int[] ileOtrz(List<Pieniadz> pieniadze) {
        int ileR = 0;
        int ileN = 0;
        int ileK = 0;
        for (Pieniadz p : pieniadze) {
            if (p.czyMozeBycRozmieniony()) {
                ileR += p.wartosc();
            } else {
                ileN += p.wartosc();
            }
        }
        for (Pieniadz p : kasa) {
            if (p.czyMozeBycRozmieniony()) {
                ileK += p.wartosc();
            }
        }

        return new int[]{ileR, ileN, ileK};
    }

    private List<Pieniadz> wydajR(int suma, List<Pieniadz> money) {
        List<Pieniadz> reszta = new ArrayList<>();
        ArrayList<Pieniadz> pieniadze = new ArrayList<>(money);
        while (suma != 0) {
            List<Pieniadz> roz = new ArrayList<>();
            for (Pieniadz p : pieniadze) {
                if (p.czyMozeBycRozmieniony()) {
                    if (p.wartosc() <= suma) {
                        reszta.add(p);
                        suma -= p.wartosc();
                    } else {
                        roz = rozmieniacz.rozmien(p);
                    }
                    pieniadze.remove(p);
                    break;
                }
            }
            for (Pieniadz r : roz) {
                pieniadze.add(0, r);
            }
        }

        for (Pieniadz p : pieniadze) {
            kasa.add(p);
        }

        return reszta;
    }

    private List<Pieniadz> wydajN(int cena, int otrz, List<Pieniadz> money) {
        List<Pieniadz> reszta = new ArrayList<>();
        List<Pieniadz> tmp = new ArrayList<>();
        ArrayList<Pieniadz> pieniadzeN = new ArrayList<>();

        for (Pieniadz m : money) {
            if (m.czyMozeBycRozmieniony()) {
                kasa.add(m);
            } else {
                pieniadzeN.add(m);
            }
        }

        pieniadzeN.sort(Comparator.comparing(Pieniadz::wartosc, Comparator.reverseOrder()));

        int ile = 0;

        for (Pieniadz p : pieniadzeN) {
            if (ile + p.wartosc() <= cena) {
                kasa.add(p);
                ile += p.wartosc();
            } else {
                reszta.add(p);
            }
        }

        ile = otrz - cena;
        int cos = ile;
        boolean is;

        while (ile != 0) {
            List<Pieniadz> roz = new ArrayList<>();
            is = true;
            for (Pieniadz p : kasa) {
                if (p.czyMozeBycRozmieniony()) {
                    if (p.wartosc() <= ile) {
                        tmp.add(p);
                        ile -= p.wartosc();
                    } else {
                        roz = rozmieniacz.rozmien(p);
                    }
                    kasa.remove(p);
                    is = false;
                    break;
                }
            }
            if (is) {
                kasa.sort(Comparator.comparing(Pieniadz::wartosc, Comparator.reverseOrder()));
                break;
            }
            for (Pieniadz r : roz) {
                kasa.add(0, r);
            }
        }

        do {
            for (Pieniadz p : kasa) {
                if (!p.czyMozeBycRozmieniony()) {
                    if (p.wartosc() <= ile) {
                        tmp.add(p);

                        ile -= p.wartosc();
                        kasa.remove(p);
                        break;
                    }
                }
            }
            if (ile == 0) {
                break;
            }
        } while (ile >=0);

        int x = 0;
        for (Pieniadz p : tmp) {
            x += p.wartosc();
        }

        while (x != cos) {
            Pieniadz a;
            a = tmp.get(0);
            if (a.czyMozeBycRozmieniony() && a.wartosc() != 1) {
                List<Pieniadz> roz = rozmieniacz.rozmien(a);
                for (Pieniadz r : roz) {
                    kasa.add(0, r);
                }
            } else {
                x -= a.wartosc();
                tmp.remove(a);
                kasa.add(a);
            }
        }

        for (Pieniadz p : tmp) {
            reszta.add(p);
        }
        return reszta;
    }

    @Override
    public List<Pieniadz> rozlicz(int cena, List<Pieniadz> pieniadze) {
        List<Pieniadz> reszta = new ArrayList<>();

        int[] otrzymane = ileOtrz(pieniadze);
        int ileWydac = otrzymane[0] + otrzymane[1] - cena;

        if (ileWydac == 0) {
            for (Pieniadz p : pieniadze) {
                kasa.add(p);
            }
            return reszta;
        }

        if (ileWydac > 0) {
            if (ileWydac <= otrzymane[0]) {
                reszta = wydajR(ileWydac, pieniadze);
            } else {
                reszta = wydajN(cena, otrzymane[0] + otrzymane[1], pieniadze);
            }
        }
        return reszta;
    }

    @Override
    public List<Pieniadz> stanKasy() {
        return kasa;
    }

    @Override
    public void dostępDoRozmieniacza(RozmieniaczInterface rozmieniacz) {
        this.rozmieniacz = rozmieniacz;
    }

    @Override
    public void dostępDoPoczątkowegoStanuKasy(Supplier<Pieniadz> dostawca) {
        while (true) {
            var tmp = dostawca.get();
            if (tmp == null) {
                break;
            } else {
                kasa.add(tmp);
            }
        }
    }
}
