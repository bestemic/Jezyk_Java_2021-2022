import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Start extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Start okno = new Start();
                okno.setTitle("Grafownik");
                okno.setSize(500, 300);

                JButton load = new JButton("Load");
                okno.setLayout(new BorderLayout());
                okno.add(load, BorderLayout.SOUTH);

                Graf tlo = new Graf();
                okno.add(tlo, BorderLayout.CENTER);

                load.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File dir = new File(System.getProperty("user.dir"));
                        JFileChooser j = new JFileChooser();
                        j.setCurrentDirectory(dir);

                        int info = j.showOpenDialog(null);
                        if (info == JFileChooser.APPROVE_OPTION) {
                            tlo.getData(j.getSelectedFile());
                        }
                    }
                });

                okno.setVisible(true);
                okno.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }
}

class Graf extends JPanel {

    class Punkt {
        private int col;
        private int row;

        public int getCol() {
            return col;
        }

        public int getRow() {
            return row;
        }

        public Punkt(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

    class Linia {
        private Punkt first;
        private Punkt second;
        private int waga;

        public Punkt getFirst() {
            return first;
        }

        public Punkt getSecond() {
            return second;
        }

        public int getWaga() {
            return waga;
        }

        public Linia(Punkt first, Punkt second, int waga) {
            this.first = first;
            this.second = second;
            this.waga = waga;
        }
    }

    private int sizex = 1;
    private int sizey = 1;
    private int maxwaga = 1;
    private int originalw;
    private int originalh;
    private List<Punkt> punkty = new ArrayList<>();
    private List<Linia> krawendzie = new ArrayList<>();

    private static List<String> readLines(File file) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void getData(File file) {
        punkty.clear();
        krawendzie.clear();
        List<String> linie = readLines(file);
        Iterator<String> itr = linie.iterator();
        int n = 0;
        if (itr.hasNext()) {
            n = Integer.parseInt(itr.next());
        }

        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] splited = itr.next().split("\\s+");
            x.add(Integer.parseInt(splited[0]));
            y.add(Integer.parseInt(splited[1]));
        }

        int min = Collections.min(x);
        if (min > 0) {
            for (int i = 0; i < x.size(); i++) {
                x.set(i, x.get(i) - min);
            }
        } else if (min < 0) {
            min = min * (-1);
            System.out.println(min);
            for (int i = 0; i < x.size(); i++) {
                x.set(i, x.get(i) + min);
            }
        }

        min = Collections.min(y);
        if (min > 0) {
            for (int i = 0; i < y.size(); i++) {
                y.set(i, y.get(i) - min);
            }
        } else if (min < 0) {
            min = min * (-1);
            for (int i = 0; i < y.size(); i++) {
                y.set(i, y.get(i) + min);
            }
        }

        sizex = Collections.max(x);
        sizey = Collections.max(y);

        for (int i = 0; i < x.size(); i++) {
            punkty.add(new Punkt(x.get(i), y.get(i)));
        }

        if (itr.hasNext()) {
            n = Integer.parseInt(itr.next());
            List<Integer> first = new ArrayList<>();
            List<Integer> last = new ArrayList<>();
            List<Integer> waga = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                String[] splited = itr.next().split("\\s+");
                first.add(Integer.parseInt(splited[0]) - 1);
                last.add(Integer.parseInt(splited[1]) - 1);
                waga.add(Integer.parseInt(splited[2]));
            }

            maxwaga = Collections.max(waga);

            for (int i = 0; i < first.size(); i++) {
                krawendzie.add(new Linia(punkty.get(first.get(i)), punkty.get(last.get(i)), waga.get(i)));
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (originalh == 0) {
            originalw = getWidth();
            originalh = getHeight();
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double skala = Math.min(((double) getWidth() / originalw), ((double) getHeight() / originalh));
        int pointSize = (int) (20 * skala);
        int MARGINES = pointSize / 2 + 5;
        int width;
        int heigth;
        int marginLeft = 0;
        int marginDown = 0;
        int size = 0;
        int n = (int) (12 * skala);
        int white = (pointSize - n);

        if(sizex == 0 && sizey == 0){
            pointSize = Math.min(getWidth()-20, getHeight()-20);
            MARGINES = pointSize / 2 + 5;
            white = pointSize/2;
        }

        if (sizex == 0) {
            width = 0;
            marginLeft = (getWidth() - 2 * MARGINES)/ 2;
        } else {
            width = (getWidth() - 2 * MARGINES) / (sizex);
            size = width;
        }

        if (sizey == 0) {
            heigth = 0;
            marginDown = (getHeight() - 2 * MARGINES)/ 2;
        } else {
            heigth = (getHeight() - 2 * MARGINES) / (sizey);
            size = heigth;
        }

        if (sizex != 0 && sizey != 0) {
            if (width <= heigth) {
                marginDown = (getHeight() - 2 * MARGINES - width * (sizey)) / 2;
                size = width;

            } else {
                marginLeft = (getWidth() - 2 * MARGINES - heigth * (sizex)) / 2;
                size = heigth;
            }
        }

        for (Punkt punkt : punkty) {
            g2.fillOval(MARGINES + marginLeft + punkt.getCol() * size - pointSize / 2, getHeight() - MARGINES - marginDown - punkt.getRow() * size - pointSize / 2, pointSize, pointSize);
        }

        for (Linia linia : krawendzie) {
            g2.setStroke(new BasicStroke((int) (12 * skala * linia.getWaga() / maxwaga)));
            g2.drawLine(MARGINES + marginLeft + linia.getFirst().getCol() * size, getHeight() - MARGINES - marginDown - linia.getFirst().getRow() * size, MARGINES + marginLeft + linia.getSecond().getCol() * size, getHeight() - MARGINES - marginDown - linia.getSecond().getRow() * size);
        }

        g2.setColor(Color.WHITE);

        for (Punkt punkt : punkty) {
            g2.fillOval(MARGINES + marginLeft + punkt.getCol() * size - white / 2, getHeight() - MARGINES - marginDown - punkt.getRow() * size - white / 2, white, white);
        }
    }
}