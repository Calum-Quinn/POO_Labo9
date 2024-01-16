package ex_race.solution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

class JDisk extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    Random random = new Random();

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isShiftDown()) {
            Disk disk = container(e.getPoint());
            if (disk != null) {
                disk.setCentre(new Point (e.getPoint().x + Disk.getXYDiff()[0],e.getPoint().y + Disk.getXYDiff()[1]));
            }
        }
        else {
            Disk disk = disks.getLast();
            disk.setRadius(disk.getCentre().distance(e.getPoint()));
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private static class Disk {

        private Point centre;
        private double radius;

        private final Color color;

        //Static variable used to keep the same vector between the center and the mouse while moving
        static int[] XYDiff;

        public static int[] getXYDiff() {
            return XYDiff;
        }

        public static void setXYDiff(int[] XYDiff) {
            Disk.XYDiff = XYDiff;
        }

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

    private final LinkedList<Disk> disks = new LinkedList<>();

    public Disk container(Point point) {
        for (int i = disks.size() - 1; i >= 0; i--) {
            if (disks.get(i).centre.distance(point) <= disks.get(i).radius) {
                return disks.get(i);
            }
        }
        return null;
    }

    public void clearDisks() {
        disks.clear();
        repaint();
    }

    JDisk() {
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (e.isShiftDown()) {
                Disk disk = container(e.getPoint());
                if (disk != null) {
                    Disk.setXYDiff(new int[]{disk.getCentre().x - e.getPoint().x, disk.getCentre().y - e.getPoint().y});
                }
            }
            else {
                disks.add(new Disk(e.getPoint(),colors[random.nextInt(colors.length)]));
                repaint();
            }
        }
    }

    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            Disk disk = container(e.getPoint());
            if (disk != null) {
                disks.remove(disk);
                repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    public void actionPerformed(ActionEvent e) {}

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

        clear.addActionListener(e -> Disk.clearDisks());

        quit.addActionListener(e -> System.exit(0));

        frame.pack();
        frame.setVisible(true);
    }
}
