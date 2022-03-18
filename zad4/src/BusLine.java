import java.util.*;

class BusLine implements BusLineInterface {


    private Map<String, List<Position>> borders = new HashMap<>();
    private Map<String, Set<LineSegment>> segments = new HashMap<>();

    private Map<String, List<Position>> lines = new HashMap<>();
    private Map<String, List<Position>> interPos = new HashMap<>();
    private Map<String, List<String>> interNames = new HashMap<>();
    private Map<BusLineInterface.LinesPair, Set<Position>> interPair = new HashMap<>();


    static class LinesPair implements BusLineInterface.LinesPair {

        private String first;
        private String second;

        public LinesPair(String first, String second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String getFirstLineName() {
            return first;
        }

        @Override
        public String getSecondLineName() {
            return second;
        }
    }

    private List<Position> pointsInLine(LineSegment vector) {
        List<Position> points = new ArrayList<>();
        Position first = vector.getFirstPosition();
        Position last = vector.getLastPosition();
        int firstC = first.getCol();
        int firstR = first.getRow();
        int lastC = last.getCol();
        int lastR = last.getRow();

        if (first.equals(last))
            points.add(first);
        else {
            if (firstC > lastC || firstR > lastR) {
                if (firstC == lastC) {
                    int n = firstR - lastR;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC, firstR - i));
                    }
                } else if (firstR == lastR) {
                    int n = firstC - lastC;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC - i, firstR));
                    }
                } else if (firstR < lastR && firstC > lastC) {
                    int n = lastR - firstR;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC - i, firstR + i));
                    }
                } else if (firstR > lastR && firstC < lastC) {
                    int n = firstR - lastR;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC + i, firstR - i));
                    }
                } else {
                    int n = firstR - lastR;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC - i, firstR - i));
                    }
                }
            } else {
                if (firstC == lastC) {
                    int n = lastR - firstR;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC, firstR + i));
                    }
                } else if (firstR == lastR) {
                    int n = lastC - firstC;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC + i, firstR));
                    }
                } else {
                    int n = lastR - firstR;
                    for (int i = 0; i <= n; i++) {
                        points.add(new Position2D(firstC + i, firstR + i));
                    }
                }
            }
        }

        return points;
    }

    private Map<String, List<Position>> addPoints() {
        Map<String, List<Position>> lines = new HashMap<>();
        for (String line : borders.keySet()) {
            List<Position> border = new ArrayList<>(this.borders.get(line));
            Set<LineSegment> segment = segments.get(line);
            int size = segment.size();
            border.remove(1);
            while (size != 0) {
                for (LineSegment odc : segment) {
                    if (border.get(border.size() - 1).equals(odc.getFirstPosition())) {
                        border.remove(border.size() - 1);
                        List<Position> points = pointsInLine(odc);
                        border.addAll(points);
                        size--;
                    }
                }
            }
            lines.put(line, border);
        }
        return lines;
    }

    private void checkOther(String line1, List<Position> points1, int i, Position before, Position after) {
        for (String line2 : lines.keySet()) {
            List<Position> points2 = lines.get(line2);

            List<Position> sublist = new ArrayList<>();
            sublist.add(before);
            sublist.add(points1.get(i));
            sublist.add(after);
            if (points2.containsAll(sublist)) {

                List<Position> rsublist = new ArrayList<>();
                rsublist.add(after);
                rsublist.add(points1.get(i));
                rsublist.add(before);

                if ((Collections.indexOfSubList(points2, sublist) != -1) || (Collections.indexOfSubList(points2, rsublist) != -1)) {
                    if (interPos.containsKey(line1)) {
                        interPos.get(line1).add(points1.get(i));
                        interNames.get(line1).add(line2);
                    } else {
                        List<Position> point = new ArrayList<>();
                        point.add(points1.get(i));
                        interPos.put(line1, point);

                        List<String> neighbor = new ArrayList<>();
                        neighbor.add(line2);
                        interNames.put(line1, neighbor);
                    }

                    boolean yes = false;
                    for (BusLineInterface.LinesPair pair : interPair.keySet()) {
                        if (pair.getFirstLineName().equals(line1) && pair.getSecondLineName().equals(line2)) {
                            interPair.get(pair).add(points1.get(i));
                            yes = true;
                            break;

                        }
                    }
                    if (!yes) {
                        Set<Position> set = new HashSet<>();
                        set.add(points1.get(i));
                        interPair.put(new LinesPair(line1, line2), set);
                    }
                }

            } else {
                boolean yes = false;
                for (BusLineInterface.LinesPair pair : interPair.keySet()) {
                    if (pair.getFirstLineName().equals(line1) && pair.getSecondLineName().equals(line2)) {
                        yes = true;
                        break;
                    }
                }
                if (!yes) {
                    Set<Position> set = new HashSet<>();
                    interPair.put(new LinesPair(line1, line2), set);
                }
            }
        }
    }

    @Override
    public void addBusLine(String busLineName, Position firstPoint, Position lastPoint) {
        List<Position> border = new ArrayList<>();
        border.add(firstPoint);
        border.add(lastPoint);
        borders.put(busLineName, border);
    }

    @Override
    public void addLineSegment(String busLineName, LineSegment lineSegment) {
        Set<LineSegment> segment = segments.get(busLineName);
        if (segment == null) {
            Set<LineSegment> line = new HashSet<>();
            line.add(lineSegment);
            segments.put(busLineName, line);
        } else {
            segment.add(lineSegment);
            segments.put(busLineName, segment);
        }
    }

    @Override
    public void findIntersections() {
        lines = addPoints();

        for (String line1 : lines.keySet()) {
            List<Position> points = lines.get(line1);
            for (int i = 1; i < points.size() - 1; i++) {

                if (points.get(i).getRow() == points.get(i - 1).getRow() && points.get(i).getRow() == points.get(i + 1).getRow()) {
                    Position before = new Position2D(points.get(i).getCol(), points.get(i).getRow() - 1);
                    Position after = new Position2D(points.get(i).getCol(), points.get(i).getRow() + 1);
                    checkOther(line1, points, i, before, after);
                } else if (points.get(i).getCol() == points.get(i - 1).getCol() && points.get(i).getCol() == points.get(i + 1).getCol()) {
                    Position before = new Position2D(points.get(i).getCol() - 1, points.get(i).getRow());
                    Position after = new Position2D(points.get(i).getCol() + 1, points.get(i).getRow());
                    checkOther(line1, points, i, before, after);
                } else if (points.get(i).getCol() == points.get(i - 1).getCol() + 1 && points.get(i).getCol() == points.get(i + 1).getCol() - 1
                        && points.get(i).getRow() == points.get(i - 1).getRow() - 1 && points.get(i).getRow() == points.get(i + 1).getRow() + 1) {
                    Position before = new Position2D(points.get(i).getCol() - 1, points.get(i).getRow() - 1);
                    Position after = new Position2D(points.get(i).getCol() + 1, points.get(i).getRow() + 1);
                    checkOther(line1, points, i, before, after);
                } else if (points.get(i).getCol() == points.get(i - 1).getCol() - 1 && points.get(i).getCol() == points.get(i + 1).getCol() + 1
                        && points.get(i).getRow() == points.get(i - 1).getRow() + 1 && points.get(i).getRow() == points.get(i + 1).getRow() - 1) {
                    Position before = new Position2D(points.get(i).getCol() - 1, points.get(i).getRow() - 1);
                    Position after = new Position2D(points.get(i).getCol() + 1, points.get(i).getRow() + 1);
                    checkOther(line1, points, i, before, after);
                } else if (points.get(i).getCol() == points.get(i - 1).getCol() + 1 && points.get(i).getCol() == points.get(i + 1).getCol() - 1
                        && points.get(i).getRow() == points.get(i - 1).getRow() + 1 && points.get(i).getRow() == points.get(i + 1).getRow() - 1) {
                    Position before = new Position2D(points.get(i).getCol() - 1, points.get(i).getRow() + 1);
                    Position after = new Position2D(points.get(i).getCol() + 1, points.get(i).getRow() - 1);
                    checkOther(line1, points, i, before, after);
                } else if (points.get(i).getCol() == points.get(i - 1).getCol() - 1 && points.get(i).getCol() == points.get(i + 1).getCol() + 1
                        && points.get(i).getRow() == points.get(i - 1).getRow() - 1 && points.get(i).getRow() == points.get(i + 1).getRow() + 1) {
                    Position before = new Position2D(points.get(i).getCol() - 1, points.get(i).getRow() + 1);
                    Position after = new Position2D(points.get(i).getCol() + 1, points.get(i).getRow() - 1);
                    checkOther(line1, points, i, before, after);
                }
            }
        }

        Map<String, List<Position>> road = new HashMap<>();
        for (String line : lines.keySet()) {
            if (interPos.containsKey(line)) {
                road.put(line, lines.get(line));
            }
        }
        lines = road;
    }

    @Override
    public Map<String, List<Position>> getLines() {
        return lines;
    }

    @Override
    public Map<String, List<Position>> getIntersectionPositions() {
        return interPos;
    }

    @Override
    public Map<String, List<String>> getIntersectionsWithLines() {
        return interNames;
    }

    @Override
    public Map<BusLineInterface.LinesPair, Set<Position>> getIntersectionOfLinesPair() {
        return interPair;
    }
}