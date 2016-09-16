/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.event.CaretEvent;

/**
 *
 * @author alex
 */
public class ClientWindow extends javax.swing.JFrame implements Runnable {

	private Client client;

	/**
	 * Creates new form ClientWindow
	 */

	ClientWindow(String clientName, Client client) {
		this.client = client;
		this.setTitle(clientName);
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		deleteMessageRB = new javax.swing.JRadioButton();
		changeMessageRB = new javax.swing.JRadioButton();
		publishMessageRB = new javax.swing.JRadioButton();
		userIDlabel = new javax.swing.JLabel();
		userIDTextField = new javax.swing.JTextField();
		showMessagesButton = new javax.swing.JButton();
		newMessageRB = new javax.swing.JRadioButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		messageTextArea = new javax.swing.JTextArea();
		sendQueryButton = new javax.swing.JButton();
		messageIDTextField = new javax.swing.JTextField();
		abteilungLabel = new javax.swing.JLabel();
		abteilungTextField = new javax.swing.JTextField();
		messageIDlabel = new javax.swing.JLabel();
		
		//set borders
		userIDTextField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		messageIDTextField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		abteilungTextField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		messageTextArea.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		showMessagesButton.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		sendQueryButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

		buttonGroup1.add(deleteMessageRB);
		buttonGroup1.add(changeMessageRB);
		buttonGroup1.add(publishMessageRB);
		buttonGroup1.add(newMessageRB);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setName("mainWindow"); // NOI18N

		deleteMessageRB.setText("Delete message");
		deleteMessageRB.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				deleteMessageRBItemStateChanged(evt);
			}
		});

		changeMessageRB.setText("Change message");
		changeMessageRB.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				changeMessageRBItemStateChanged(evt);
			}
		});

		publishMessageRB.setText("Publish message");
		publishMessageRB.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				publishMessageRBItemStateChanged(evt);
			}
		});

		userIDlabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		userIDlabel.setText("User ID");

		userIDTextField.setText("User ID");
		userIDTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				userIDTextFieldMouseClicked(evt);
			}
		});
		userIDTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				userIDTextFieldActionPerformed(evt);
			}
		});
		userIDTextField.addCaretListener(new javax.swing.event.CaretListener(){

			@Override
			public void caretUpdate(CaretEvent e) {
				userIDTextFieldCaretMoved(e);
			}
			
		});

		showMessagesButton.setText("Show my messages");
		showMessagesButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				showMessagesButtonMouseClicked(evt);
			}
		});

		newMessageRB.setText("New message");
		newMessageRB.setSelected(true);
		newMessageRB.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				newMessageRBItemStateChanged(evt);
			}
		});
		newMessageRB.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				newMessageRBStateChanged(evt);
			}
		});
		newMessageRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newMessageRBActionPerformed(evt);
			}
		});

		messageTextArea.setColumns(20);
		messageTextArea.setRows(5);
		messageTextArea.setText("Your Message");
		messageTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				messageTextAreaMouseClicked(evt);
			}
		});
		jScrollPane1.setViewportView(messageTextArea);

		sendQueryButton.setText("Send query");
		sendQueryButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				sendQueryButtonMouseClicked(evt);
			}
		});

		messageIDTextField.setText("MessageID");
		messageIDTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				messageIDTextFieldMouseClicked(evt);
			}
		});
		messageIDTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				messageIDTextFieldActionPerformed(evt);
			}
		});
		messageIDTextField.addCaretListener(new javax.swing.event.CaretListener(){

			@Override
			public void caretUpdate(CaretEvent e) {
				messageIDTextFieldCaretMoved(e);
			}
			
		});

		abteilungLabel.setText("Abteilung");

		abteilungTextField.setText("Abteilung");
		abteilungTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				abteilungTextFieldMouseClicked(evt);
			}
		});
		abteilungTextField.addCaretListener(new javax.swing.event.CaretListener(){

			@Override
			public void caretUpdate(CaretEvent e) {
				abteilungTextFieldCaretMoved(e);				
			}
			
		});

		messageIDlabel.setText("Message ID");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(newMessageRB)
						.addContainerGap(448, Short.MAX_VALUE))
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(20, 20, 20)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														userIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 99,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(userIDlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 99,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup()
														.addComponent(abteilungTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(showMessagesButton,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																170, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(layout.createSequentialGroup().addComponent(abteilungLabel)
														.addGap(0, 0, Short.MAX_VALUE))))
								.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addComponent(changeMessageRB).addGap(0,
												413, Short.MAX_VALUE))
										.addGroup(layout.createSequentialGroup().addGroup(layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup().addGap(2, 2, 2)
														.addComponent(messageIDlabel))
												.addComponent(publishMessageRB, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(deleteMessageRB)).addGap(0, 413, Short.MAX_VALUE))
										.addGroup(layout.createSequentialGroup()
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(messageIDTextField)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(sendQueryButton, javax.swing.GroupLayout.PREFERRED_SIZE,
														122, javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1)))
						.addContainerGap()));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { changeMessageRB, deleteMessageRB, messageIDTextField, publishMessageRB });

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { newMessageRB, userIDTextField });

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { abteilungLabel, abteilungTextField });

		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(abteilungLabel).addComponent(userIDlabel,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(userIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(showMessagesButton).addComponent(abteilungTextField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(27, 27, 27).addComponent(newMessageRB)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(deleteMessageRB)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(changeMessageRB)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(publishMessageRB).addGap(18, 18, 18).addComponent(messageIDlabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(sendQueryButton).addComponent(messageIDTextField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

		layout.linkSize(javax.swing.SwingConstants.VERTICAL,
				new java.awt.Component[] { sendQueryButton, showMessagesButton });

		pack();
	}// </editor-fold>//GEN-END:initComponents

	protected void messageIDTextFieldCaretMoved(CaretEvent e) {
		if(!isValidInput(messageIDTextField.getText())){
			sendQueryButton.setEnabled(false);
			showMessagesButton.setEnabled(false);
			messageIDTextField.setBorder(BorderFactory.createLineBorder(Color.red,2));
		} else {
			sendQueryButton.setEnabled(true);
			showMessagesButton.setEnabled(true);
			messageIDTextField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		}
	}

	protected void abteilungTextFieldCaretMoved(CaretEvent e) {
		if(!isValidInput(abteilungTextField.getText())){
			sendQueryButton.setEnabled(false);
			showMessagesButton.setEnabled(false);
			abteilungTextField.setBorder(BorderFactory.createLineBorder(Color.red,2));
		} else {
			sendQueryButton.setEnabled(true);
			showMessagesButton.setEnabled(true);
			abteilungTextField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		}
		
	}

	protected void userIDTextFieldCaretMoved(CaretEvent e) {
		if(!isValidInput(userIDTextField.getText())){
			sendQueryButton.setEnabled(false);
			showMessagesButton.setEnabled(false);
			userIDTextField.setBorder(BorderFactory.createLineBorder(Color.red,2));
		} else {
			sendQueryButton.setEnabled(true);
			showMessagesButton.setEnabled(true);
			userIDTextField.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		}
		
	}

	private void userIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userIDTextFieldActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_userIDTextFieldActionPerformed

	private void messageIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_messageIDTextFieldActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_messageIDTextFieldActionPerformed

	private void newMessageRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newMessageRBActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_newMessageRBActionPerformed

	private void newMessageRBStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_newMessageRBStateChanged
		// TODO add your handling code here:
	}// GEN-LAST:event_newMessageRBStateChanged

	private void newMessageRBItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_newMessageRBItemStateChanged
		// TODO add your handling code here:
		System.out.println("itemStateChanged");
		if (newMessageRB.isSelected()) {
			messageTextArea.setEnabled(true);
			messageIDTextField.setEnabled(false);
		} else {
			messageTextArea.setEnabled(false);
		}
	}// GEN-LAST:event_newMessageRBItemStateChanged

	private void deleteMessageRBItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_deleteMessageRBItemStateChanged
		// TODO add your handling code here:
		if (deleteMessageRB.isSelected()) {
			messageTextArea.setEnabled(false);
			messageIDTextField.setEnabled(true);
			messageTextArea.setCaretPosition(0);
		}
	}// GEN-LAST:event_deleteMessageRBItemStateChanged

	private void changeMessageRBItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_changeMessageRBItemStateChanged
		// TODO add your handling code here:
		if (changeMessageRB.isSelected()) {
			messageTextArea.setEnabled(true);
			messageIDTextField.setEnabled(true);

		}
	}// GEN-LAST:event_changeMessageRBItemStateChanged

	private void publishMessageRBItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_publishMessageRBItemStateChanged
		// TODO add your handling code here:
		if (publishMessageRB.isSelected()) {
			messageTextArea.setEnabled(false);
			messageIDTextField.setEnabled(true);
			messageTextArea.setCaretPosition(0);
		}
	}// GEN-LAST:event_publishMessageRBItemStateChanged

	private void userIDTextFieldMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_userIDTextFieldMouseClicked
		// TODO add your handling code here:
		userIDTextField.setText("");
		// userIDTextField.selectAll();
	}// GEN-LAST:event_userIDTextFieldMouseClicked

	private void messageTextAreaMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_messageTextAreaMouseClicked
		// TODO add your handling code here:
		if (newMessageRB.isEnabled()) {
			messageTextArea.setText(null);
		}
		messageTextArea.selectAll();
		if (!changeMessageRB.isSelected())
			newMessageRB.setSelected(true);
	}// GEN-LAST:event_messageTextAreaMouseClicked

	private void messageIDTextFieldMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_messageIDTextFieldMouseClicked
		// TODO add your handling code here:
		messageIDTextField.setText("");
		// messageIDTextField.selectAll();
	}// GEN-LAST:event_messageIDTextFieldMouseClicked

	private void showMessagesButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_showMessagesButtonMouseClicked
		// TODO add your handling code here:
		System.out.println("Mouse clicked!");

		messageTextArea.setText(client.showMessagesWithGui(Integer.parseInt(abteilungTextField.getText()),
				Integer.parseInt(userIDTextField.getText())));

	}// GEN-LAST:event_showMessagesButtonMouseClicked

	private void abteilungTextFieldMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_abteilungTextFieldMouseClicked
		abteilungTextField.setText("");
		// abteilungTextField.selectAll();
	}// GEN-LAST:event_abteilungTextFieldMouseClicked

	private void sendQueryButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_sendQueryButtonMouseClicked
		if (newMessageRB.isSelected() && !abteilungTextField.getText().isEmpty() && !userIDTextField.getText().isEmpty()
				&& !messageTextArea.getText().isEmpty()) {
			client.sendeMessageWithGui(Integer.parseInt(abteilungTextField.getText()), messageTextArea.getText(),
					Integer.parseInt(userIDTextField.getText()));
		}

		if (deleteMessageRB.isSelected() && !abteilungTextField.getText().isEmpty()
				&& !userIDTextField.getText().isEmpty() && !messageIDTextField.getText().isEmpty()) {
			client.removeMessageWithGui(Integer.parseInt(abteilungTextField.getText()),
					Integer.parseInt(userIDTextField.getText()), Integer.parseInt(messageIDTextField.getText()));
		}

		if (changeMessageRB.isSelected() && !abteilungTextField.getText().isEmpty()
				&& !userIDTextField.getText().isEmpty() && !messageIDTextField.getText().isEmpty()) {
			String neueMessage = client.changeMessageWithGui(Integer.parseInt(abteilungTextField.getText()),
					Integer.parseInt(userIDTextField.getText()), Integer.parseInt(messageIDTextField.getText()),
					messageTextArea.getText());
			messageTextArea.setText(neueMessage);
		}

		if (publishMessageRB.isSelected() && !abteilungTextField.getText().isEmpty()
				&& !userIDTextField.getText().isEmpty() && !messageIDTextField.getText().isEmpty()) {
			client.publishMessageWithGui(Integer.parseInt(messageIDTextField.getText()),
					Integer.parseInt(userIDTextField.getText()));
			// TODO handle publishing failures
		}

	}// GEN-LAST:event_sendQueryButtonMouseClicked

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel abteilungLabel;
	private javax.swing.JTextField abteilungTextField;
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JRadioButton changeMessageRB;
	private javax.swing.JRadioButton deleteMessageRB;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField messageIDTextField;
	private javax.swing.JLabel messageIDlabel;
	private javax.swing.JTextArea messageTextArea;
	private javax.swing.JRadioButton newMessageRB;
	private javax.swing.JRadioButton publishMessageRB;
	private javax.swing.JButton sendQueryButton;
	private javax.swing.JButton showMessagesButton;
	private javax.swing.JTextField userIDTextField;
	private javax.swing.JLabel userIDlabel;
	// End of variables declaration//GEN-END:variables

	public boolean isValidInput(String str) {
		boolean isValid = true;
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException exception) {
			isValid = false;
		}
		return isValid;
	}

	@Override
	public void run() {
		this.setVisible(rootPaneCheckingEnabled);
	}
}
