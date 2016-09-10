package client;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;



public class LoggingWindow extends javax.swing.JFrame implements Runnable{

    private JTextArea logtext;
    private Client client;
    public LoggingWindow(Client client){
        super("Log");
        this.client = client;
        this.logtext = new JTextArea();
        logtext.setEditable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
           
    }
    public void clear(){
        logtext.setText(null);
    }
    public void addEntry(String entry){
        logtext.setText(logtext.getText()+'\n'+entry);
    }

    @Override
    public void run() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.setSize(new Dimension(400,300));
        this.setLayout(new GridLayout(1,1));
        this.add(logtext);        
        this.setVisible(true);
    }
    
}
