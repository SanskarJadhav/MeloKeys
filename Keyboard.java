import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class Keyboard {
    // piano key colors
    private static final boolean WHITE_KEY = false;
    private static final boolean BLACK_KEY = true;

    // initial font sizes
    private static final int DEFAULT_FONT_SIZE_BLACK_KEY = 16;
    private static final int DEFAULT_FONT_SIZE_WHITE_KEY = 18;

    // initial width and height
    private final int initialWidth;
    private final int initialHeight;

    // white and black keys
    private ArrayList<Key> blackKeys = new ArrayList<Key>();
    private ArrayList<Key> whiteKeys = new ArrayList<Key>();

    // new object for synchronization of key typed, pressed and released
    private final Object keyLock = new Object();

    // queue of typed keys (yet to be processed by client)
    private LinkedList<Character> keysTyped = new LinkedList<Character>();

    // set of key characters currently pressed down
    private TreeSet<Character> keysDown = new TreeSet<Character>();


    // default 36 keys or 3 octaves keyboard
    public Keyboard() {
        this("zsxdcvgbhnjm,l.;/q2w3e4rt6y7ui9o0p-[", "C");
    }

    // for any customisation
    private Keyboard(String keyboardString, String firstWhiteKey) {

        String[] whiteKeyNames = { "C", "D", "E", "F", "G", "A", "B" };

        // create the white and black keys
        for (int i = 0; i < keyboardString.length(); i++) {

            // next key is white, and after 7 keys, repeats with C again
            String whiteKeyName = whiteKeyNames[whiteKeys.size() % 7];
            Key whiteKey = new Key(whiteKeys.size(), whiteKeyName, keyboardString.charAt(i),
                                   WHITE_KEY);
            whiteKeys.add(whiteKey);

            // black keys are immediately after C, D, F, G, and A
            if ("ACDFG".contains(whiteKeyName)) {
                i++;
                if (i >= keyboardString.length()) break;
                String blackKeyName = whiteKeyName + "#";
                Key blackKey = new Key(whiteKeys.size(), blackKeyName, keyboardString.charAt(i),
                                       BLACK_KEY);
                blackKeys.add(blackKey);
            }
        }

        // reasonable values for initial dimensions
        initialWidth = 55 * whiteKeys.size();
        initialHeight = 220;

        // create and show the GUI (in the event-dispatching thread)
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new KeyboardPanel();
            panel.setPreferredSize(new Dimension(initialWidth, initialHeight));

            JFrame frame = new JFrame("3 Octave Keyboard");
            //frame.setMinimumSize(new Dimension(initialWidth / 2, initialHeight / 2));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack(); // uses preferred sizes for larger viewing
            frame.setLocationRelativeTo(null);   // center on screeen
            frame.setVisible(true);
        });
    }

    public char nextKeyPlayed() {
        synchronized (keyLock) {
            if (keysTyped.isEmpty()) {
                throw new NoSuchElementException(
                        "All typed keys have been processed");
            }
            return keysTyped.removeLast();
        }
    }

    public boolean hasNextKeyPlayed() {
        synchronized (keyLock) {
            return !keysTyped.isEmpty();
        }
    }

    // the JPanel for drawing the keyboard
    private class KeyboardPanel extends JPanel implements KeyListener {

        public KeyboardPanel() {
            setBackground(Color.WHITE);
            addKeyListener(this);
            setFocusable(true);
        }

        // draw the keyboard
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Dimension size = getSize();
            double width = size.getWidth();
            double height = size.getHeight();

            // first, draw the white keys
            for (Key whiteKey : whiteKeys) {

                // key typed
                if (keysDown.contains(whiteKey.getKeyStroke())) {
                    whiteKey.draw(g, width, height, Color.GREEN, Color.WHITE);
                }
                else {
                    whiteKey.draw(g, width, height, Color.WHITE, Color.BLACK);
                }
            }

            // then, draw the black keys
            for (Key blackKey : blackKeys) {

                // key typed
                if (keysDown.contains(blackKey.getKeyStroke())) {
                    blackKey.draw(g, width, height, Color.GREEN, Color.WHITE);
                }

                // draw as usual
                else {
                    blackKey.draw(g, width, height, Color.BLACK, Color.GRAY);
                }
            }
        }

        /***************************************************************************
         *  Keyboard events.
         ***************************************************************************/

        public void keyTyped(KeyEvent e) {
            synchronized (keyLock) {
                char c = Character.toLowerCase(e.getKeyChar());
                keysTyped.addFirst(c);
            }
        }

        public void keyPressed(KeyEvent e) {
            synchronized (keyLock) {
                char c = Character.toLowerCase(e.getKeyChar());
                keysDown.add(c);
                repaint();
            }
        }

        public void keyReleased(KeyEvent e) {
            synchronized (keyLock) {
                char c = Character.toLowerCase(e.getKeyChar());
                keysDown.remove(c);
                repaint();
            }
        }
    }


    /***************************************************************************
     *  Helper data type to represent individual white or black keys.
     ***************************************************************************/
    private Font getFont(int defaultFontSize, double width, double height) {
        int size = (int) (width * defaultFontSize / initialWidth);
        return new Font("SansSerif", Font.PLAIN, size);
    }

    private class Key {
        private final String name;        // key name (e.g., C)
        private final boolean isBlack;    // is it a black key?
        private final char keyStroke;     // keyboard keystroke that correspond to piano key

        // rectangle for key
        // (coordinate system is scaled so that white keys have width and height 1.0)
        private final double xmin, xmax, ymin, ymax;


        public Key(double x, String name, char keyStroke, boolean isBlack) {
            this.name = name;
            this.keyStroke = keyStroke;
            this.isBlack = isBlack;

            if (!isBlack) {
                xmin = x;
                xmax = x + 1;
                ymin = 0.0;
                ymax = 1.0;
            }
            else {
                xmin = x - 0.3;
                xmax = x + 0.3;
                ymin = 0.0;
                ymax = 0.6;
            }
        }


        // draw the key using the given background and foreground colors
        private void draw(Graphics2D g, double width, double height,
                          Color backgroundColor, Color foregroundColor) {
            double SCALE_X = width / whiteKeys.size();
            double SCALE_Y = height;
            Rectangle2D.Double rectangle = new Rectangle2D.Double(xmin * SCALE_X,
                                                                  ymin * SCALE_Y,
                                                                  (xmax - xmin) * SCALE_X,
                                                                  (ymax - ymin) * SCALE_Y);

            // black key
            if (isBlack) {
                g.setColor(backgroundColor);
                g.fill(rectangle);
                g.setFont(getFont(DEFAULT_FONT_SIZE_BLACK_KEY, width, height));
                FontMetrics metrics = g.getFontMetrics();
                int ws = metrics.stringWidth(keyStroke + "");
                g.setColor(foregroundColor);
                g.drawString(keyStroke + "",
                             (float) ((xmin + xmax) / 2.0 * SCALE_X - ws / 2.0),
                             25.0f);
            }

            // white key
            else {
                g.setColor(backgroundColor);
                g.fill(rectangle);

                // include outline since white keys must be identifiable
                g.setColor(Color.BLACK);
                g.draw(rectangle);

                g.setFont(getFont(DEFAULT_FONT_SIZE_WHITE_KEY, width, height));
                FontMetrics metrics = g.getFontMetrics();
                int hs = metrics.getHeight();
                int ws = metrics.stringWidth(name);
                g.setColor(foregroundColor);
                g.drawString(keyStroke + "",
                             (float) ((xmin + xmax) / 2.0 * SCALE_X - ws / 2.0),
                             (float) (0.95 * SCALE_Y - hs / 2.0));
            }


        }

        // the computer keyboard keystroke corresponding to this piano key
        private char getKeyStroke() {
            return keyStroke;
        }

        // does the rectangle contain the given (x, y)
        private boolean contains(double x, double y) {
            return x >= xmin && x < xmax && y >= ymin && y < ymax;
        }

        public String toString() {
            return String.format("%-2s: [%4.1f, %4.1f] x [%2.1f, %2.1f]", name, xmin, xmax, ymin, ymax);
        }
    }


    public static void main(String[] args) {
        Keyboard keyboard = new Keyboard();
    }

}
