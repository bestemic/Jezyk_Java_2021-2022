import java.util.LinkedList;
import java.util.List;


class ParallelSearcher implements ParallelSearcherInterface {

    class MyThread implements Runnable {
        @Override
        public void run() {
            HidingPlaceSupplier.HidingPlace skrytka = dostawa.get();
            while (skrytka != null) {
                if (skrytka.isPresent()) {
                    synchronized (semafor) {
                        wynik += skrytka.openAndGetValue();
                    }
                }
                skrytka = dostawa.get();
            }
        }
    }

    private HidingPlaceSupplierSupplier dostawca;
    private HidingPlaceSupplier dostawa;
    private List<Thread> branches;
    private double wynik = 0;
    private final String semafor = "";

    @Override
    public void set(HidingPlaceSupplierSupplier supplier) {
        dostawca = supplier;
        dostawa = dostawca.get(0);
        while (dostawa != null) {
            branches = new LinkedList<>();

            for (int i = 0; i < dostawa.threads(); i++) {
                Thread tmp = new Thread(new MyThread());
                tmp.start();
                branches.add(tmp);
            }

            for (Thread branch : branches) {
                try {
                    branch.join();
                } catch (InterruptedException e) {
                }
            }
            dostawa = dostawca.get(wynik);
            wynik = 0;
        }
    }
}