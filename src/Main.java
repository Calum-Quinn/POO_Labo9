package ex_race.solution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

class DiskEvent {

    public DiskEvent() {

    }
}

interface DiskListenerMain {
    void action(DiskEvent e);
}

class JDisk extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    Random random = new Random();

    @Override
    public void mouseDragged(MouseEvent e) {
        Disk disk = disks.getLast();
        disk.setRadius(disk.getCentre().distance(e.getPoint()));

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private static class Disk {

        private Point centre;
        private double radius;

        private final Color color;

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        public Color getColor() {
            return color;
        }

        public Point getCentre() {
            return centre;
        }

        public void setCentre(Point centre) {
            this.centre = centre;
        }

        public Disk(Point centre, Color color) {
            radius = 0;
            this.centre = centre;
            this.color = color;
        }
    }

    private static final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE,
            Color.BLACK, Color.MAGENTA, Color.CYAN, Color.DARK_GRAY, Color.GRAY,
            Color.LIGHT_GRAY, Color.ORANGE, Color.PINK, Color.YELLOW};

    private LinkedList<Disk> disks = new LinkedList<>();
    private Disk stopped;
    private Timer timer;
    private DiskListenerMain DiskListener;
    private long startTime;

    public void clearDisks() {
        disks.clear();
        repaint();
    }

    JDisk() {
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // MouseListener



    public void mousePressed(MouseEvent e) {
        disks.add(new Disk(e.getPoint(),colors[random.nextInt(colors.length)]));
        repaint();
    }

    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
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

        repaint();
    }

    public void run() {


    }

    private int DiskRadius() {
        return getWidth() - 40; // borders
    }

    public void paintComponent(Graphics g) {

        int diskRadius = DiskRadius();

        super.paintComponent(g);

        for (Disk disk : disks) {
            g.setColor(disk.getColor());
            g.fillOval((int)(disk.getCentre().x - disk.getRadius()), (int)(disk.getCentre().y - disk.getRadius()),(int)disk.getRadius() * 2,(int)disk.getRadius() * 2);
        }

        g.setColor(Color.BLACK);
        g.drawLine(20, 0, 20, getHeight());
        g.drawLine(diskRadius + 20, 0, diskRadius + 20, getHeight());
    }
}

public class Main {
    public static void main(String[] args) {
        // try {
        //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) { }

        final JDisk Disk = new JDisk();
        JFrame frame = new JFrame("Disk");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(Disk, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new GridLayout(0, 3));
        frame.getContentPane().add(south, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(); // to avoid button resizing
        south.add(buttonPanel);

        final JButton clear = new JButton("Clear");
        final JButton quit = new JButton("Quit");
        buttonPanel.add(clear);
        buttonPanel.add(quit);

        frame.pack();
        frame.setVisible(true);

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Disk.clearDisks();
            }
        });

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);

        Disk.addDiskListener(new DiskListenerMain() {
            public void action(DiskEvent e) {

            }
        });
    }
}
