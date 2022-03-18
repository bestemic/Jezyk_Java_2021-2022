import java.util.HashMap;
import java.util.Map;

class Shop implements ShopInterface {
    private final Map<String, Integer> towar = new HashMap<>();
    private final Map<String, Object> semafory = new HashMap<>();

    @Override
    public void delivery(Map<String, Integer> goods) {
        for (String name : goods.keySet()) {
            synchronized (towar) {
                if (towar.containsKey(name)) {
                    towar.put(name, towar.get(name) + goods.get(name));
                } else {
                    towar.put(name, goods.get(name));
                }
            }
            if (semafory.containsKey(name)) {
                synchronized (semafory.get(name)) {
                    semafory.get(name).notifyAll();
                }
            }
        }
    }

    @Override
    public boolean purchase(String productName, int quantity) {
        if (!semafory.containsKey(productName)) {
            semafory.put(productName, new Object());
        }

        if (towar.containsKey(productName)) {
            synchronized (semafory.get(productName)) {
                if (towar.get(productName) >= quantity) {
                    towar.put(productName, towar.get(productName) - quantity);
                    return true;
                }
            }
        }
        synchronized (semafory.get(productName)) {
            try {
                semafory.get(productName).wait();
            } catch (InterruptedException e) {
            }

            if (towar.get(productName) >= quantity) {
                towar.put(productName, towar.get(productName) - quantity);
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Integer> stock() {
        return towar;
    }
}