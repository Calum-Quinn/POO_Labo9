package ex_race.solution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class DiskEvent {
    private int winner = -1;
    private long time;

    public DiskEvent(long time) {
        this.time = time;
    }

    public DiskEvent(long time, int winner) {
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

interface DiskListenerMain {
    void action(DiskEvent e);
}

class JDiskMain extends JPanel implements ActionListener, MouseListener {
    private static class Runner {
        static Random random = new Random();
        static int count = 0;
        boolean paused = false;

        int number = count++;
        double position = 0;

        void move() {
            if (!paused && position < 100)
                position += random.nextDouble();
        }
    }

    private static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE,
            Color.BLACK, Color.MAGENTA};
    private Runner runners[] = new Runner[10];
    private Runner stopped;
    private Timer timer;
    private DiskListenerMain DiskListener;
    private long startTime;

    JDiskMain() {
        setBackground(Color.WHITE);
        addMouseListener(this);
    }

    // MouseListener

    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < runners.length; i++)
            if (runners[i] != null) {
                Rectangle r = runnerRectangle(i, DiskWidth(), DiskrHeight());
                if (r.contains(e.getPoint().x, e.getPoint().y)) {
                    stopped = runners[i];
                    stopped.paused = true;
                    break;
                }
            }
    }

    public void mouseReleased(MouseEvent e) {
        if (stopped != null) {
            stopped.paused = false;
            stopped = null;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void addDiskListener(DiskListenerMain l) {
        // Simple implementation: only one listener at a time...
        DiskListener = l;
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
        if (DiskListener != null)
            if (winner != null) {
                DiskListener.action(
                        new DiskEvent(System.currentTimeMillis() - startTime, winner.number));
                timer.stop();
            } else
                DiskListener.action(
                        new DiskEvent(System.currentTimeMillis() - startTime));
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

    private int DiskWidth() {
        return getWidth() - 40; // borders
    }

    private double DiskrHeight() {
        return getHeight() / (runners.length * 2 + 1.0);
    }

    private Rectangle runnerRectangle(int i, int maxWidth, double height) {
        return new Rectangle(20, (int) (height * (1 + 2 * i)),
                Math.min((int) runners[i].position, 100) *
                        maxWidth / 100, (int) height);
    }

    public void paintComponent(Graphics g) {
        int DiskWidth = DiskWidth();
        double DiskrHeight = DiskrHeight();

        super.paintComponent(g);

        for (int i = 0; i < runners.length; i++)
            if (runners[i] != null) {
                if (runners[i] == stopped)
                    g.setColor(Color.GRAY);
                else
                    g.setColor(colors[i % colors.length]);
                Rectangle r = runnerRectangle(i, DiskWidth, DiskrHeight);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        g.setColor(Color.BLACK);
        g.drawLine(20, 0, 20, getHeight());
        g.drawLine(DiskWidth + 20, 0, DiskWidth + 20, getHeight());
    }
}

public class Main {
    public static void main(String[] args) {
        // try {
        //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) { }

        final JDiskMain Disk = new JDiskMain();
        JFrame frame = new JFrame("Disk");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(Disk, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new GridLayout(0, 3));
        frame.getContentPane().add(south, BorderLayout.SOUTH);

        final JLabel label = new JLabel("Disk not started", JLabel.CENTER);
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
                label.setText("Running...");
                Disk.run();
                run.setEnabled(false);
            }
        });

        Disk.addDiskListener(new DiskListenerMain() {
            public void action(DiskEvent e) {
                double time = e.getTime() / 1000.0;
                int secs = (int) time;
                clock.setText(secs + "." + (int) ((time - secs) * 10));
                if (e.getWinner() != -1) {
                    label.setText("Disk won by #" + e.getWinner());
                    run.setEnabled(true);
                }
            }
        });
    }
}
