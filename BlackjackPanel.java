import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class BlackjackPanel extends JPanel implements KeyListener{

    String state;
    char userInput;
    Deck deck;
    BlackjackPlayer player1;
    BlackjackPlayer dealer;
    final int CARD_WIDTH = 128;
    final int CARD_HEIGHT = 186;
    final int FRAME_RATE = 100;
    private ArrayList<Card> discard;

    public BlackjackPanel(){
        setPreferredSize(new Dimension(800,800));
        setBackground(new Color(2,108,26));
        addKeyListener(this);
        state = "READY";
        userInput = '-';
        deck = new Deck();
        player1 = new BlackjackPlayer("Soumya");
        dealer = new BlackjackPlayer("Disha");
        deck.shuffle();
        discard = new ArrayList<Card>();
    }

    //display method - handle screen output here DO NOT TRANSITION STATES HERE 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Britannic Bold", Font.BOLD, 40));
        g.drawString("BLACKJACK!", 300, 40);
        // g.drawImage(image,x,y,width,height,null);
        int playerX = 0;
        for(int i = 0; i < player1.seeCards().size(); i++) {
            playerX += 50;
            Card c = player1.seeCards().get(i);
            g.drawImage(c.getFace(), playerX, 50, CARD_WIDTH, CARD_HEIGHT, null);
        }
        int dealerX = 0;
        for(int i = 0; i < dealer.seeCards().size(); i++) {
            dealerX += 50;
            Card c = dealer.seeCards().get(i);
            g.drawImage(c.getFace(), dealerX, 300, CARD_WIDTH, CARD_HEIGHT, null);
        }
        if(state.equals("READY")) {
            g.drawString("Press d to deal", 300, 350);
        }
        if(state.equals("SHOW")) {
            if(player1.getScore() == 21 && dealer.getScore() == 21) {
                g.drawString("THERE WAS A TIE", 250, 250);
            }
            if(player1.getScore() > 21) {
                g.drawString("DEALER WON", 250, 290);
            } else if(dealer.getScore() > 21) {
                g.drawString("PLAYER WON", 250, 290);
            } else if(player1.getScore() > dealer.getScore()) {
                g.drawString("PLAYER WON", 250, 290);
            } else {
                g.drawString("DEALER WON", 250, 290);
            }
        }
    }

    public void run() {
        while(true) {
            update();
            repaint();
            delay(FRAME_RATE);
        }
    }

    //state machine: handle states. DO NOT PAINT ANYTHING
    private void update() {
        if (state.equals("READY")){
            if(userInput == 'd') {
                userInput = '-';
                state = "SHUFFLE AND DEAL";
            }
        } else if (state.equals("SHUFFLE AND DEAL")){
            player1.take(deck.deal());
            player1.take(deck.deal());
            dealer.take(deck.deal());
            state = "P1_TURN";
        } else if (state.equals("P1_TURN")){
            if(userInput == 'h') {
                userInput = '-';
                player1.take(deck.deal());
            }
            if(userInput == 's') {
                userInput = '-';
                state = "DEALER TURN";
            }
            if(player1.getScore() > 21) {
                state = "SHOW";
            }
        } else if (state.equals("DEALER TURN")){
            dealer.take(deck.deal());
            while(dealer.getScore() <= 16) {
                dealer.take(deck.deal());
            }
            if(dealer.getScore() > 21 || userInput == 's') {
                state = "SHOW";
            }
        } else if(state.equals("SHOW")) {
            if(userInput == 'c') {
                state = "CLEAR";
            }
        } else if(state.equals("CLEAR")) {
            discard.addAll(player1.fold());
            discard.addAll(dealer.fold());
            state = "READY";
        }else {
            throw new RuntimeException("Unkown state: " + state);
        }
        if(deck.getCardsLeft() < 15) {
            deck = new Deck();
            discard.clear();
            deck.shuffle();
        }
    }

    public void keyPressed(KeyEvent e) {
        
    }
    public void keyReleased(KeyEvent e) {
        
    }
    public void keyTyped(KeyEvent e) {
        userInput = e.getKeyChar();
    }

    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}