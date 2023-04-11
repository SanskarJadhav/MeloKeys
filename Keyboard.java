import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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
import java.io.IOException;
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
    private static final int DEFAULT_FONT_SIZE_WHITE_KEY = 16;

    // initial width and height
    private final int initialWidth;
    private final int initialHeight;

    // white and black keys
    private final ArrayList<Key> blackKeys = new ArrayList<Key>();
    private final ArrayList<Key> whiteKeys = new ArrayList<Key>();

    // new object for synchronization of key typed, pressed and released
    private final Object keyLock = new Object();

    // queue of typed keys (yet to be processed by client)
    private LinkedList<Character> keysTyped = new LinkedList<Character>();

    // set of key characters currently pressed down
    private TreeSet<Character> keysDown = new TreeSet<Character>();


    // default 36 keys or 3 octaves keyboard
    public Keyboard() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this("zsxdcvgbhnjm,l.;/q2w3e4rt6y7ui9o0p-[", "C");
    }

    // for any customisation
    private Keyboard(String keyboardString, String firstWhiteKey) throws UnsupportedAudioFileException, LineUnavailableException, IOException {

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
            JPanel panel = null;
            try {
                panel = new KeyboardPanel();
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            panel.setPreferredSize(new Dimension(initialWidth, initialHeight));

            JFrame frame = new JFrame("3 Octave Keyboard");
            //frame.setMinimumSize(new Dimension(initialWidth / 2, initialHeight / 2));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack(); // uses preferred sizes for larger viewing
            frame.setLocationRelativeTo(null);   // center on screen
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

        String c3file = "Octave 3/Piano.ff.C3.aiff";
        String db3file = "Octave 3/Piano.ff.Db3.aiff";
        String d3file = "Octave 3/Piano.ff.D3.aiff";
        String eb3file = "Octave 3/Piano.ff.Eb3.aiff";
        String e3file = "Octave 3/Piano.ff.E3.aiff";
        String f3file = "Octave 3/Piano.ff.F3.aiff";
        String gb3file = "Octave 3/Piano.ff.Gb3.aiff";
        String g3file = "Octave 3/Piano.ff.G3.aiff";
        String ab3file = "Octave 3/Piano.ff.Ab3.aiff";
        String a3file = "Octave 3/Piano.ff.A3.aiff";
        String bb3file = "Octave 3/Piano.ff.Bb3.aiff";
        String b3file = "Octave 3/Piano.ff.B3.aiff";
        String c4file = "Octave 4/Piano.ff.C4.aiff";
        String db4file = "Octave 4/Piano.ff.Db4.aiff";
        String d4file = "Octave 4/Piano.ff.D4.aiff";
        String eb4file = "Octave 4/Piano.ff.Eb4.aiff";
        String e4file = "Octave 4/Piano.ff.E4.aiff";
        String f4file = "Octave 4/Piano.ff.F4.aiff";
        String gb4file = "Octave 4/Piano.ff.Gb4.aiff";
        String g4file = "Octave 4/Piano.ff.G4.aiff";
        String ab4file = "Octave 4/Piano.ff.Ab4.aiff";
        String a4file = "Octave 4/Piano.ff.A4.aiff";
        String bb4file = "Octave 4/Piano.ff.Bb4.aiff";
        String b4file = "Octave 4/Piano.ff.B4.aiff";
        String c5file = "Octave 5/Piano.ff.C5.aiff";
        String db5file = "Octave 5/Piano.ff.Db5.aiff";
        String d5file = "Octave 5/Piano.ff.D5.aiff";
        String eb5file = "Octave 5/Piano.ff.Eb5.aiff";
        String e5file = "Octave 5/Piano.ff.E5.aiff";
        String f5file = "Octave 5/Piano.ff.F5.aiff";
        String gb5file = "Octave 5/Piano.ff.Gb5.aiff";
        String g5file = "Octave 5/Piano.ff.G5.aiff";
        String ab5file = "Octave 5/Piano.ff.Ab5.aiff";
        String a5file = "Octave 5/Piano.ff.A5.aiff";
        String bb5file = "Octave 5/Piano.ff.Bb5.aiff";
        String b5file = "Octave 5/Piano.ff.B5.aiff";
        SimpleAudioPlayer c3audio = new SimpleAudioPlayer(c3file);
        SimpleAudioPlayer db3audio = new SimpleAudioPlayer(db3file);
        SimpleAudioPlayer d3audio = new SimpleAudioPlayer(d3file);
        SimpleAudioPlayer eb3audio = new SimpleAudioPlayer(eb3file);
        SimpleAudioPlayer e3audio = new SimpleAudioPlayer(e3file);
        SimpleAudioPlayer f3audio = new SimpleAudioPlayer(f3file);
        SimpleAudioPlayer gb3audio = new SimpleAudioPlayer(gb3file);
        SimpleAudioPlayer g3audio = new SimpleAudioPlayer(g3file);
        SimpleAudioPlayer ab3audio = new SimpleAudioPlayer(ab3file);
        SimpleAudioPlayer a3audio = new SimpleAudioPlayer(a3file);
        SimpleAudioPlayer bb3audio = new SimpleAudioPlayer(bb3file);
        SimpleAudioPlayer b3audio = new SimpleAudioPlayer(b3file);
        SimpleAudioPlayer c4audio = new SimpleAudioPlayer(c4file);
        SimpleAudioPlayer db4audio = new SimpleAudioPlayer(db4file);
        SimpleAudioPlayer d4audio = new SimpleAudioPlayer(d4file);
        SimpleAudioPlayer eb4audio = new SimpleAudioPlayer(eb4file);
        SimpleAudioPlayer e4audio = new SimpleAudioPlayer(e4file);
        SimpleAudioPlayer f4audio = new SimpleAudioPlayer(f4file);
        SimpleAudioPlayer gb4audio = new SimpleAudioPlayer(gb4file);
        SimpleAudioPlayer g4audio = new SimpleAudioPlayer(g4file);
        SimpleAudioPlayer ab4audio = new SimpleAudioPlayer(ab4file);
        SimpleAudioPlayer a4audio = new SimpleAudioPlayer(a4file);
        SimpleAudioPlayer bb4audio = new SimpleAudioPlayer(bb4file);
        SimpleAudioPlayer b4audio = new SimpleAudioPlayer(b4file);
        SimpleAudioPlayer c5audio = new SimpleAudioPlayer(c5file);
        SimpleAudioPlayer db5audio = new SimpleAudioPlayer(db5file);
        SimpleAudioPlayer d5audio = new SimpleAudioPlayer(d5file);
        SimpleAudioPlayer eb5audio = new SimpleAudioPlayer(eb5file);
        SimpleAudioPlayer e5audio = new SimpleAudioPlayer(e5file);
        SimpleAudioPlayer f5audio = new SimpleAudioPlayer(f5file);
        SimpleAudioPlayer gb5audio = new SimpleAudioPlayer(gb5file);
        SimpleAudioPlayer g5audio = new SimpleAudioPlayer(g5file);
        SimpleAudioPlayer ab5audio = new SimpleAudioPlayer(ab5file);
        SimpleAudioPlayer a5audio = new SimpleAudioPlayer(a5file);
        SimpleAudioPlayer bb5audio = new SimpleAudioPlayer(bb5file);
        SimpleAudioPlayer b5audio = new SimpleAudioPlayer(b5file);

        public KeyboardPanel() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
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
                // drawn as usual
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

                // drawn as usual
                else {
                    blackKey.draw(g, width, height, Color.BLACK, Color.GRAY);
                }
            }
        }

        // Keyboard Methods

        public void keyTyped(KeyEvent e) {
            synchronized (keyLock) {
                char c = e.getKeyChar();
                keysTyped.addFirst(c);
            }
        }

        public void keyPressed(KeyEvent e) {
            synchronized (keyLock) {
                char c = e.getKeyChar();
                keysDown.add(c);
                repaint();
                switch (c) {
                    case 'z' -> c3audio.play();
                    case 's' -> db3audio.play();
                    case 'x' -> d3audio.play();
                    case 'd' -> eb3audio.play();
                    case 'c' -> e3audio.play();
                    case 'v' -> f3audio.play();
                    case 'g' -> gb3audio.play();
                    case 'b' -> g3audio.play();
                    case 'h' -> ab3audio.play();
                    case 'n' -> a3audio.play();
                    case 'j' -> bb3audio.play();
                    case 'm' -> b3audio.play();
                    case ',' -> c4audio.play();
                    case 'l' -> db4audio.play();
                    case '.' -> d4audio.play();
                    case ';' -> eb4audio.play();
                    case '/' -> e4audio.play();
                    case 'q' -> f4audio.play();
                    case '2' -> gb4audio.play();
                    case 'w' -> g4audio.play();
                    case '3' -> ab4audio.play();
                    case 'e' -> a4audio.play();
                    case '4' -> bb4audio.play();
                    case 'r' -> b4audio.play();
                    case 't' -> c5audio.play();
                    case '6' -> db5audio.play();
                    case 'y' -> d5audio.play();
                    case '7' -> eb5audio.play();
                    case 'u' -> e5audio.play();
                    case 'i' -> f5audio.play();
                    case '9' -> gb5audio.play();
                    case 'o' -> g5audio.play();
                    case '0' -> ab5audio.play();
                    case 'p' -> a5audio.play();
                    case '-' -> bb5audio.play();
                    case '[' -> b5audio.play();
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            synchronized (keyLock) {
                char c = e.getKeyChar();
                keysDown.remove(c);
                repaint();
                switch (c) {
                    case 'z' -> c3audio.pause();
                    case 's' -> db3audio.pause();
                    case 'x' -> d3audio.pause();
                    case 'd' -> eb3audio.pause();
                    case 'c' -> e3audio.pause();
                    case 'v' -> f3audio.pause();
                    case 'g' -> gb3audio.pause();
                    case 'b' -> g3audio.pause();
                    case 'h' -> ab3audio.pause();
                    case 'n' -> a3audio.pause();
                    case 'j' -> bb3audio.pause();
                    case 'm' -> b3audio.pause();
                    case ',' -> c4audio.pause();
                    case 'l' -> db4audio.pause();
                    case '.' -> d4audio.pause();
                    case ';' -> eb4audio.pause();
                    case '/' -> e4audio.pause();
                    case 'q' -> f4audio.pause();
                    case '2' -> gb4audio.pause();
                    case 'w' -> g4audio.pause();
                    case '3' -> ab4audio.pause();
                    case 'e' -> a4audio.pause();
                    case '4' -> bb4audio.pause();
                    case 'r' -> b4audio.pause();
                    case 't' -> c5audio.pause();
                    case '6' -> db5audio.pause();
                    case 'y' -> d5audio.pause();
                    case '7' -> eb5audio.pause();
                    case 'u' -> e5audio.pause();
                    case 'i' -> f5audio.pause();
                    case '9' -> gb5audio.pause();
                    case 'o' -> g5audio.pause();
                    case '0' -> ab5audio.pause();
                    case 'p' -> a5audio.pause();
                    case '-' -> bb5audio.pause();
                    case '[' -> b5audio.pause();
                }
            }
        }
    }


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
        try {
            Keyboard keyboard = new Keyboard();
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
