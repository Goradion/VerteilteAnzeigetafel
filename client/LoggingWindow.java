package client;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class LoggingWindow extends javax.swing.JFrame implements Runnable{

	private static final long serialVersionUID = 200676197613299000L;
	private JTextArea logtext;
    private JScrollPane scroll;
    public LoggingWindow(ClientWindow mainWindow){
        super("Log");
        this.logtext = new JTextArea();
        logtext.setEditable(false); 
        this.scroll = new JScrollPane(logtext);
        this.setLocation(mainWindow.getX()+mainWindow.getWidth(),mainWindow.getY());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
        this.add(scroll);        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }
    
}
