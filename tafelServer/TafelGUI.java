package tafelServer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import verteilteAnzeigetafel.Anzeigetafel;
import verteilteAnzeigetafel.Message;

public class TafelGUI implements Observer {
    
    private final JFrame window;
    private JTextArea localMessages;
    private JTextArea globalMessages;
    
    TafelGUI(int abteilung, TafelServer ts){
        this.window = new JFrame("Abteilung "+Integer.toString(abteilung));
        
        this.localMessages = new JTextArea();
        localMessages.setEditable(false);
        localMessages.setPreferredSize(new Dimension(200,150));
        localMessages.setBorder(new TitledBorder("Local messages"));
        localMessages.setSize(new Dimension(200,150));
        
        this.globalMessages = new JTextArea();
        globalMessages.setEditable(false);
        globalMessages.setPreferredSize(new Dimension(200,150));
        globalMessages.setBorder(new TitledBorder("Global messages"));
        globalMessages.setSize(new Dimension(200,150));
        
        window.setLayout(new GridLayout(2,1));
        window.add(localMessages);
        window.add(globalMessages);
        window.setPreferredSize(new Dimension(400,300));
        window.setSize(new Dimension(400,300));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.setVisible(true);
       
    }
    
    @Override
    public void update(Observable o, Object arg) {
        Anzeigetafel at = (Anzeigetafel)o;
        localMessages.setText(null);
        globalMessages.setText(null);
        for(Message m: at.getLocalMsgs()){
            localMessages.append(m.getUserID()+" : "+m.getInhalt()+'\n');
        }
        for(Message m: at.getGlobalMsgs()){
            globalMessages.append(m.getUserID()+" : "+m.getInhalt()+'\n');
        }
    }    
}
