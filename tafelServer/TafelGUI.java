package tafelServer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import verteilteAnzeigetafel.Anzeigetafel;
import verteilteAnzeigetafel.Message;

public class TafelGUI implements Observer {
    
    private final JFrame window;
    private JTextArea localMessages;
    private JTextArea globalMessages;
    private JScrollPane localScroll;
    private JScrollPane globalScroll;
    
    TafelGUI(int abteilung, TafelServer ts){
        this.window = new JFrame("Abteilung "+Integer.toString(abteilung));
        
        this.localMessages = new JTextArea();
        localMessages.setEditable(false);
//        localMessages.setPreferredSize(new Dimension(200,150));
        localMessages.setBorder(new TitledBorder("Local messages"));
//        localMessages.setSize(new Dimension(200,150));
        
        this.globalMessages = new JTextArea();
        globalMessages.setEditable(false);
//        globalMessages.setPreferredSize(new Dimension(200,150));
        globalMessages.setBorder(new TitledBorder("Global messages"));
//        globalMessages.setSize(new Dimension(200,150));
        
        
        this.localScroll = new JScrollPane(localMessages);
        localScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        localScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        this.globalScroll = new JScrollPane(globalMessages);
        globalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        globalScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        window.setLayout(new GridLayout(2,1));
        window.add(localScroll);
        window.add(globalScroll);
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
            localMessages.append("UserID: " + m.getUserID()+'\t'+"Zeit: "
                    +m.getTime()+'\n'+m.getInhalt()+'\n'+'\n');
        }
        for(Message m: at.getGlobalMsgs()){
            globalMessages.append("Abt: "+m.getAbtNr()+'\t'+"UserID: "
                    + m.getUserID()+'\t'+"Zeit: "+m.getTime()+'\n'
                    +m.getInhalt()+'\n'+'\n');
        }
    }    
}
