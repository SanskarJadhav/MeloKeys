/* a very simple code designed for the purpose
of learning how to use Java Swing
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class SwingDemo {

    public static void main(String[] argv) throws Exception {

        JTextField textField = new JTextField();

        textField.addKeyListener(new MKeyListener());

        JFrame jframe = new JFrame();

        jframe.add(textField);

        jframe.setSize(400, 350);

        jframe.setVisible(true);

    }
}

class MKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent event) {

        char ch = event.getKeyChar();

        // to check if the user is entering one of these letters
        if (ch == 'c' ||ch == 'd'||ch == 'e' ) {

            System.out.println(event.getKeyChar());

        }

        // to end the program with the Escape key
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {

            System.exit(0);

        }

    }
}
