import javax.swing.JFrame;

public class BlackjackRunner {

  public static void main(String[] args) {
    JFrame f = new JFrame("Blackjack"); 
    BlackjackPanel p = new BlackjackPanel();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(p);
    f.pack();
    f.setVisible(true);
    p.setFocusable(true);
    p.requestFocusInWindow();
    p.run();
  }
}
