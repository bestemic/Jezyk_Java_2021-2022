import java.util.*;

class Compression implements CompressionInterface {

    private List<String> words = new ArrayList<>();
    private List<String> wynik = new ArrayList<>();
    private Map<String, String> header = new HashMap<>();

    private int len(List<String> words) {
        int wynik = 0;
        for (String word : words) {
            wynik += word.length();
        }
        return wynik;
    }

    private int headlen(Map<String, String> header) {
        int wynik = 0;
        for (String head : header.keySet()) {
            wynik = wynik + head.length() + header.get(head).length();
        }
        return wynik;
    }

    @Override
    public void addWord(String word) {
        words.add(word);
    }

    @Override
    public void compress() {
        Map<String, Integer> ile = new LinkedHashMap<>();
        Map<String, Integer> ilesorted = new LinkedHashMap<>();
        for (String word : words) {
            if (ile.containsKey(word)) {
                ile.put(word, ile.get(word) + 1);
            } else {
                ile.put(word, 1);
            }
        }

        ile.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> ilesorted.put(x.getKey(), x.getValue()));

        List<String> del = new ArrayList<>();
        for (String i : ilesorted.keySet()) {
            if (ilesorted.get(i) == 1) {
                del.add(i);
            }
        }

        for (String d : del) {
            ilesorted.remove(d);
        }

        List<String> spr = new ArrayList<>();
        Map<String, String> inheader = new HashMap<>();
        int check = len(words);
        while (true) {

            int wielkosc = ilesorted.size();
            if (wielkosc == 0) {
                break;
            }
            String size = Integer.toBinaryString(wielkosc - 1);
            int tmp = 0;
            for (String i : ilesorted.keySet()) {
                if (wielkosc == 1) {
                    inheader.put(i, "0");
                } else {
                    String bin = Integer.toBinaryString(tmp);
                    tmp++;
                    if (size.length() - bin.length() == 0) {
                        inheader.put(i, "0" + bin);
                    } else {
                        String s = "0".repeat(size.length() - bin.length());
                        inheader.put(i, "0" + s + bin);
                    }
                }
            }

            for (String word : words) {
                if (inheader.containsKey(word)) {
                    spr.add(inheader.get(word));
                } else {
                    spr.add("1" + word);
                }
            }

            if (len(spr) + headlen(inheader) >= len(words)) {
                if (check > len(spr) + headlen(inheader))
                    check = len(spr) + headlen(inheader);
            } else if (check > len(spr) + headlen(inheader)) {
                check = len(spr) + headlen(inheader);
                wynik = new ArrayList<>(spr);
                header = new HashMap<>(inheader);
            }
            inheader = new HashMap<>();
            int x = 1;
            for (String i : ilesorted.keySet()) {
                if (x == wielkosc) {
                    ilesorted.remove(i);
                }
                x++;
            }
            spr = new ArrayList<>();
        }

        if (wynik.size() == 0) {
            wynik = new ArrayList<>(words);
        }
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header1 = new HashMap<>();
        for (String head : header.keySet()) {
            header1.put(header.get(head), head);
        }
        return header1;
    }

    @Override
    public String getWord() {
        return wynik.remove(0);
    }
}
