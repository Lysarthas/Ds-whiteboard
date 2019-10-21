import java.awt.event.*;
import javax.swing.*;
import java.awt.*;



public class chat extends JFrame{
	JButton sendBt;
	JTextField inputField;
	JTextArea chatContent;
	public chat(){
		this.setLayout(new BorderLayout());
		chatContent =new JTextArea(12,34);
		
		JScrollPane showPanel = new JScrollPane(chatContent);
		chatContent.setEditable(false);
		JPanel inputPanel = new JPanel();
		
		inputField = new JTextField(20);	
		sendBt = new JButton("Send");

		
		sendBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String content = inputField.getText();
				if(content != null && !content.trim().equals("")){
					chatContent.append("Usre 1:"+content+"\n");
				}else{
					JOptionPane.showMessageDialog(null, "Input can't be empty", "Warning", JOptionPane.INFORMATION_MESSAGE);
				}
				inputField.setText("");
			}
		});
		

		
		Label label =new Label("Input");
		inputPanel.add(label);
		inputPanel.add(inputField);
		inputPanel.add(sendBt);
		this.add(showPanel,BorderLayout.CENTER);
		this.add(inputPanel,BorderLayout.SOUTH);
		this.setTitle("Chat Room");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		this.setVisible(true);

		}
	public static void main(String[] args) {
		new chat();

	}

}
