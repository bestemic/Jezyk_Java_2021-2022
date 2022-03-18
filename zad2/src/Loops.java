import java.util.ArrayList;
import java.util.List;

class Loops implements GeneralLoops {

    private List<Integer> down;
    private List<Integer> up;
    private List<List<Integer>> result;

    public Loops() {
        down = new ArrayList<>();
        down.add(0);
        up = new ArrayList<>();
        up.add(0);
    }

    @Override
    public void setLowerLimits(List<Integer> limits) {
        down = limits;
    }

    @Override
    public void setUpperLimits(List<Integer> limits) {
        up = limits;
    }

    @Override
    public List<List<Integer>> getResult() {
        while (down.size() < up.size()) down.add(0);
        while (down.size() > up.size()) up.add(0);

        result = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>(down);

        rec(down, up, 0, tmp);

        return result;
    }

    private void rec(List<Integer> down, List<Integer> up, int depth, List<Integer> tmp) {

        if (depth == up.size()) {
            return;
        }
        if (depth == up.size() - 1) {
            for (int i = down.get(depth); i < up.get(depth); i++) {
                rec(down, up, depth + 1, tmp);

                result.add(new ArrayList<>(tmp));
                tmp.set(depth, tmp.get(depth) + 1);
            }
            result.add(new ArrayList<>(tmp));

        } else {
            for (int i = down.get(depth); i <= up.get(depth); i++) {
                rec(down, up, depth + 1, tmp);
                tmp.set(depth, tmp.get(depth) + 1);
            }
        }

        tmp.set(depth, down.get(depth));
    }
}