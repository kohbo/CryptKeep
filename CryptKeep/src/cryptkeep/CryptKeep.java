
package cryptkeep;

import javax.swing.JFrame;

public class CryptKeep {

    public static void main(String[] args) {
        HomePage homePage = new HomePage();
        homePage.pack();
        homePage.setLocationRelativeTo(null);
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePage.setVisible(true);
    }
}
