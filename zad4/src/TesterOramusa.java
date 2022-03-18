import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TesterOramusa {
  static int errorCount = 0;

  public static void inputLines(Map<String, List<LineSegment>> lines,Map<String, LineSegment> starts, BusLine busline) {
    for (var entry : starts.entrySet()) {
      busline.addBusLine(entry.getKey(), entry.getValue().getFirstPosition(), entry.getValue().getLastPosition());
    }
    for (var entry : lines.entrySet()) {
      for (var segment : entry.getValue()) {
        busline.addLineSegment(entry.getKey(), segment);
      }
    }
  }

  public static void main(String[] args) {
    // testy oramusa fourintersections
    var test1 = new HashMap<String, List<LineSegment>>();
    test1.put("A",
        List.of(new LineSegment(new Position2D(6, 12), new Position2D(1, 12)),
            new LineSegment(new Position2D(1, 14), new Position2D(3, 16)),
            new LineSegment(new Position2D(6, 4), new Position2D(6, 12)),
            new LineSegment(new Position2D(1, 1), new Position2D(1, 4)),
            new LineSegment(new Position2D(1, 4), new Position2D(6, 4)),
            new LineSegment(new Position2D(1, 12), new Position2D(1, 14))));
    test1.put("B", List.of(new LineSegment(new Position2D(3, 14), new Position2D(3, 2)),
        new LineSegment(new Position2D(1, 16), new Position2D(3, 14))));
    test1.put("C",
        List.of(new LineSegment(new Position2D(1, 8), new Position2D(11, 8)),
            new LineSegment(new Position2D(9, 5), new Position2D(9, 12)),
            new LineSegment(new Position2D(11, 5), new Position2D(9, 5)),
            new LineSegment(new Position2D(11, 8), new Position2D(11, 5))));
    test1.put("D", List.of(new LineSegment(new Position2D(1, 10), new Position2D(3, 10)),
        new LineSegment(new Position2D(3, 10), new Position2D(11, 10))));

    var start1 = new HashMap<String, LineSegment>();
    start1.put("A", new LineSegment(new Position2D(1, 1), new Position2D(3, 16)));
    start1.put("B", new LineSegment(new Position2D(1, 16), new Position2D(3, 2)));
    start1.put("C", new LineSegment(new Position2D(1, 8), new Position2D(9, 12)));
    start1.put("D", new LineSegment(new Position2D(1, 10), new Position2D(11, 10)));

    var b = new BusLine();
    inputLines(test1,start1,b);
    b.findIntersections();
    System.out.println(b.getIntersectionPositions());
  }
}