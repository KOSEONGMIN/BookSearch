package booksearch;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dialog_NoResult extends JDialog implements ActionListener{

	JLabel label1;
	JButton closeButton;
	JPanel panel;
	public Dialog_NoResult(){

		Font labelFont = new Font("배달의민족 도현", Font.PLAIN, 15);
		Font buttonFont = new Font("배달의민족 도현", Font.PLAIN, 15);
		panel = new JPanel();
		panel.setLayout(null);
		label1 = new JLabel("검색된 책이 없습니다");
		closeButton = new JButton("확인");
		closeButton.addActionListener(this);
		label1.setFont(labelFont);
		closeButton.setFont(buttonFont);
		
		panel.add(label1);			label1.setBounds(50, 50, 400, 50);
		panel.add(closeButton);		closeButton.setBounds(280, 200, 40, 50);
		this.add(panel);
		this.setModal(true);
		this.setSize(600,300);
		panel.setBackground(new Color(237,211,74));
		this.setModal(true);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == closeButton) {
			this.setVisible(false);
		}
	}
}
