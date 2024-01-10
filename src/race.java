package ex_race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class RaceEvent {
    private int winner = -1;
    private long time;

    public RaceEvent(long time) {
        this.time = time;
    }

    public RaceEvent(long time, int winner) {
        this(time);
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }

    public long getTime() {
        return time;
    }
}

interface RaceListener {
    void action(RaceEvent e);
}

class JRace extends JPanel implements ActionListener {
    private static class Runner {
        static Random random = new Random();
        static int count = 0;

        int number = count++;
        double position = 0;

        void move() {
            if (position < 100)
                position += random.nextDouble();
        }
    }

    private static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE,
            Color.BLACK, Color.MAGENTA};
    private Runner runners[] = new Runner[10];
    private Timer timer;
    private RaceListener raceListener;
    private long startTime;

    JRace() {
        setBackground(Color.WHITE);
    }

    public void addRaceListener(RaceListener l) {
        // Simple implementation: only one listener at a time...
        raceListener = l;
    }

    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    public void actionPerformed(ActionEvent e) {
        Runner winner = null;

        for (int i = 0; i < runners.length; i++) {
            runners[i].move();
            if (runners[i].position >= 100 &&
                    (winner == null || runners[i].position > winner.position))
                winner = runners[i];
        }
        if (raceListener != null)
            if (winner != null) {
                raceListener.action(
                        new RaceEvent(System.currentTimeMillis() - startTime, winner.number));
                timer.stop();
            } else
                raceListener.action(
                        new RaceEvent(System.currentTimeMillis() - startTime));
        repaint();
    }

    public void run() {
        Runner.count = 0;

        for (int i = 0; i < runners.length; i++)
            runners[i] = new Runner();

        timer = new Timer(50, this);
        timer.start();
        startTime = System.currentTimeMillis();
    }

    public void paintComponent(Graphics g) {
        int width = getWidth() - 40; // borders
        double ySize = getHeight() / (runners.length * 2 + 1.0);

        super.paintComponent(g);

        for (int i = 0; i < runners.length; i++)
            if (runners[i] != null) {
                g.setColor(colors[i % colors.length]);
                g.fillRect(20, (int) (ySize * (1 + 2 * i)),
                        Math.min((int) runners[i].position, 100) * width / 100,
                        (int) ySize);
            }
        g.setColor(Color.BLACK);
        g.drawLine(20, 0, 20, getHeight());
        g.drawLine(width + 20, 0, width + 20, getHeight());
    }
}

public class Race {
    public static void main(String[] args) {
        // try {
        //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) { }

        final JRace race = new JRace();
        JFrame frame = new JFrame("Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(race, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new GridLayout(0, 3));
        frame.getContentPane().add(south, BorderLayout.SOUTH);

        final JLabel label = new JLabel("Race not started", JLabel.CENTER);
        final JLabel clock = new JLabel("0.0", JLabel.CENTER);
        JPanel buttonPanel = new JPanel(); // to avoid button resizing
        south.add(label);
        south.add(clock);
        south.add(buttonPanel);

        final JButton run = new JButton("Run");
        buttonPanel.add(run);

        frame.pack();
        frame.setVisible(true);

        run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(e);
                label.setText("Running...");
                race.run();
                run.setEnabled(false);
            }
        });

        race.addRaceListener(new RaceListener() {
            public void action(RaceEvent e) {
                double time = e.getTime() / 1000.0;
                int secs = (int) time;
                clock.setText(secs + "." + (int) ((time - secs) * 10));
                if (e.getWinner() != -1) {
                    label.setText("Race won by #" + e.getWinner());
                    run.setEnabled(true);
                }
            }
        });
    }
}