import java.util.*;

public class Test04 {
    static int testCount = 0;
    static int passedTestCount = 0;

    static void doTest(List<String> lineNames, List<Position> startPoints, List<Position> endPoints, List<List<LineSegment>> segments,
                       Map<String, List<Position>> expLines, Map<String, List<Position>> expPositions, Map<String, List<String>> expInter, Map<BusLineInterface.LinesPair, Set<Position>> expPair) {
        int errorCount = 0;
        ++testCount;
        var b = new BusLine();
        for (int i = 0; i < lineNames.size(); ++i) {
            b.addBusLine(lineNames.get(i), startPoints.get(i), endPoints.get(i));
            for (int j = 0; j < segments.get(i).size(); ++j) {
                b.addLineSegment(lineNames.get(i), segments.get(i).get(j));
            }
        }
        b.findIntersections();
        var resLines = b.getLines();
        var resPositions = b.getIntersectionPositions();
        var resInter = b.getIntersectionsWithLines();
        var resPair = b.getIntersectionOfLinesPair();
        if (!expLines.equals(resLines)) {
            System.err.println("error in test " + testCount + ": getLines invalid");
            System.out.println("expected: " + expLines);
            System.out.println("result: " + resLines);
            errorCount++;
        }
        if (!expPositions.equals(resPositions)) {
            System.err.println("error in test " + testCount + ": getIntersectionPositions invalid");
            System.out.println("expected: " + expPositions);
            System.out.println("result: " + resPositions);
            errorCount++;
        }
        if (!expInter.equals(resInter)) {
            System.err.println("error in test " + testCount + ": getIntersectionsWithLines invalid");
            System.out.println("expected: " + expInter);
            System.out.println("result: " + resInter);
            errorCount++;
        }
        if (expPair.size() != resPair.size()) {
            System.err.println("error in test " + testCount + ": getIntersectionOfLinesPair invalid - wrong length");
            System.out.println("expected: " + expPair);
            System.out.println("result: " + resPair);
            errorCount++;
        } else {
            for (var expEntry : expPair.entrySet()) {
                BusLineInterface.LinesPair expLinesPair = expEntry.getKey();
                var match = resPair.entrySet().stream()
                        .filter(entry -> entry.getKey().getFirstLineName().equals(expLinesPair.getFirstLineName())
                                && entry.getKey().getSecondLineName().equals(expLinesPair.getSecondLineName()))
                        .findFirst();
                if (match.isEmpty() || !match.get().getValue().equals(expEntry.getValue())) {
                    System.err.println("error in test " + testCount + ": getIntersectionOfLinesPair invalid");
                    System.out.println("expected entry: " + expEntry);
                    System.out.println("result entry: " + (match.isEmpty() ? "" : match.get()));
                    errorCount++;
                    break;
                }
            }
        }
        if (errorCount == 0) {
            System.out.println("test " + testCount + " passed");
            ++passedTestCount;
        }
    }

    public static void main(String[] args) {
        List<String> lineNames = new ArrayList<>();
        List<Position> startPoints = new ArrayList<>();
        List<Position> endPoints = new ArrayList<>();
        List<List<LineSegment>> segments = new ArrayList<>();
        Map<String, List<Position>> expLines = new HashMap<>();
        Map<String, List<Position>> expPositions = new HashMap<>();
        Map<String, List<String>> expInter = new HashMap<>();
        Map<BusLineInterface.LinesPair, Set<Position>> expPair = new HashMap<>();

        // Test 1
        lineNames.add("a");
        startPoints.add(new Position2D(1, 1));
        endPoints.add(new Position2D(2, 6));
        segments.add(List.of(
                new LineSegment(new Position2D(1, 1), new Position2D(7, 7)),
                new LineSegment(new Position2D(7, 1), new Position2D(2, 6)),
                new LineSegment(new Position2D(7, 7), new Position2D(7, 1))));


        lineNames.add("b");
        startPoints.add(new Position2D(4, 7));
        endPoints.add(new Position2D(7, 7));
        segments.add(List.of(
                new LineSegment(new Position2D(4, 7), new Position2D(7, 7))));

        lineNames.add("c");
        startPoints.add(new Position2D(1, 1));
        endPoints.add(new Position2D(4, 2));
        segments.add(List.of(
                new LineSegment(new Position2D(8, 4), new Position2D(8, 2)),
                new LineSegment(new Position2D(1, 4), new Position2D(4, 4)),
                new LineSegment(new Position2D(8, 2), new Position2D(4, 2)),
                new LineSegment(new Position2D(1, 1), new Position2D(1, 4)),
                new LineSegment(new Position2D(4, 4), new Position2D(8, 4))));

        expLines.put("a", List.of(
                new Position2D(1, 1),
                new Position2D(2, 2),
                new Position2D(3, 3),
                new Position2D(4, 4),
                new Position2D(5, 5),
                new Position2D(6, 6),
                new Position2D(7, 7),
                new Position2D(7, 6),
                new Position2D(7, 5),
                new Position2D(7, 4),
                new Position2D(7, 3),
                new Position2D(7, 2),
                new Position2D(7, 1),
                new Position2D(6, 2),
                new Position2D(5, 3),
                new Position2D(4, 4),
                new Position2D(3, 5),
                new Position2D(2, 6)
        ));
        expLines.put("c", List.of(
                new Position2D(1, 1),
                new Position2D(1, 2),
                new Position2D(1, 3),
                new Position2D(1, 4),
                new Position2D(2, 4),
                new Position2D(3, 4),
                new Position2D(4, 4),
                new Position2D(5, 4),
                new Position2D(6, 4),
                new Position2D(7, 4),
                new Position2D(8, 4),
                new Position2D(8, 3),
                new Position2D(8, 2),
                new Position2D(7, 2),
                new Position2D(6, 2),
                new Position2D(5, 2),
                new Position2D(4, 2)
        ));

        expPositions.put("a",
                List.of(new Position2D(4, 4), new Position2D(7, 4), new Position2D(7, 2), new Position2D(4, 4)));
        expPositions.put("c", List.of(new Position2D(7, 4), new Position2D(7, 2)));

        expInter.put("a", List.of("a", "c", "c", "a"));
        expInter.put("c", List.of("a", "a"));

        expPair.put(new BusLine.LinesPair("b", "a"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("c", "b"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("a", "a"), new HashSet<>(List.of(new Position2D(4, 4))));
        expPair.put(new BusLine.LinesPair("b", "b"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("c", "c"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("a", "b"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("b", "c"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("a", "c"), new HashSet<>(List.of(new Position2D(7, 2), new Position2D(7, 4))));
        expPair.put(new BusLine.LinesPair("c", "a"), new HashSet<>(List.of(new Position2D(7, 2), new Position2D(7, 4))));

        doTest(lineNames, startPoints, endPoints, segments, expLines, expPositions, expInter, expPair);

        // Test 2
        startPoints.remove(0);
        endPoints.remove(0);
        segments.remove(0);
        startPoints.add(0, new Position2D(2, 6));
        endPoints.add(0, new Position2D(1, 1));
        segments.add(0, List.of(
                new LineSegment(new Position2D(7, 7), new Position2D(1, 1)),
                new LineSegment(new Position2D(2, 6), new Position2D(7, 1)),
                new LineSegment(new Position2D(7, 1), new Position2D(7, 7))));

        expLines.put("a", List.of(
                new Position2D(2, 6),
                new Position2D(3, 5),
                new Position2D(4, 4),
                new Position2D(5, 3),
                new Position2D(6, 2),
                new Position2D(7, 1),
                new Position2D(7, 2),
                new Position2D(7, 3),
                new Position2D(7, 4),
                new Position2D(7, 5),
                new Position2D(7, 6),
                new Position2D(7, 7),
                new Position2D(6, 6),
                new Position2D(5, 5),
                new Position2D(4, 4),
                new Position2D(3, 3),
                new Position2D(2, 2),
                new Position2D(1, 1)));

        expPositions.put("a",
                List.of(
                        new Position2D(4, 4),
                        new Position2D(7, 2),
                        new Position2D(7, 4),
                        new Position2D(4, 4)
                ));

        doTest(lineNames, startPoints, endPoints, segments, expLines, expPositions, expInter, expPair);
        if (testCount == passedTestCount)
            System.out.println("all tests passed");
    }
}
