import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;


class TextF extends JTextField {
	public int flag, x, y;
	public TextF() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Stone.nowgo == Stone.MAN_GO && Stone.btn5.getText() == "暂停")
				{
					if (flag == Stone.STONE)
					{
						setBackground(new Color(0, 0, 255));
						flag = Stone.MARK;
					}
					else
					if (flag == Stone.MARK)
					{
						setBackground(new Color(0, 0, 0));
						flag = Stone.STONE;
					}
					else
					if (flag == Stone.EMPTY)
					{
						System.out.println(x+" "+y);
						int step = 0;
						if (Stone.text[x][Stone.where[x]].flag != Stone.MARK) {
							System.out.println("111");
							return;
						}
						if (y >= Stone.where[x]) {
							System.out.println("222");
							return;
						}
						step = Stone.where[x] - y;
						int i, xx[] = new int[4];
						for (i=1;i<=Stone.stones;i++)
						{
							xx[i] = 1000;
							if (Stone.text[i][Stone.where[i]].flag == Stone.MARK)
							{
								xx[i] = Stone.where[i] - step;
								if (xx[i] < 0) {
									System.out.println("333");
									return;
								}
							}
						}
						int s1 = Stone.where[1];
						int s2 = Stone.where[2];
						int s3 = Stone.where[3];
						for (i=1;i<=Stone.stones;i++)
						{
							if (Stone.text[i][Stone.where[i]].flag == 2)
							{
								switch (i) {
								case 1: s1 = xx[i]; break;
								case 2: s2 = xx[i]; break;
								case 3: s3 = xx[i]; break;
								}
							}
						}
						Stone.go(s1, s2, s3, Stone.COMPUTER_GO);
						Stone.nowgo = Stone.COMPUTER_GO;
					}
				}
			}
		});
	}
}

public class Stone extends JFrame implements Runnable {

	final static int MAN_GO=0, COMPUTER_GO=1;
	final static int EMPTY=0, STONE=1, MARK=2;
	
	private JPanel contentPane;
	public static TextF text[][]=new TextF[4][33];
	JButton btn1, btn2, btn3, btn4;
	public static int stones, whofirst, nowgo, step, where[]=new int[4];
	public static JButton btn5, btn6;
	
	public static int sta_2[][]=new int[33][33], sta_3[][][]=new int[33][33][33];
	
	public static Stone frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		int i,j,k,l;
		// 2*N
		sta_2[0][0] = 1;
		for (i=0;i<33;i++)
			for (j=0;j<33;j++)
			{
				if (i+j > 0)
				{
					sta_2[i][j] = 1;
					for (k=0;k<i;k++) if (sta_2[k][j] == 1) sta_2[i][j] = 0;
					for (k=0;k<j;k++) if (sta_2[i][k] == 1) sta_2[i][j] = 0;
					for (k=1;k<=i&&k<=j;k++) if (sta_2[i-k][j-k] == 1) sta_2[i][j] = 0;
				}
			}
		// 3*N
		sta_3[0][0][0] = 1;
		for (i=0;i<33;i++)
			for (j=0;j<33;j++)
				for (k=0;k<33;k++)
				{
					if (i+j+k > 0)
					{
						sta_3[i][j][k] = 1;
						for (l=0;l<i;l++) if (sta_3[l][j][k] == 1) sta_3[i][j][k] = 0;
						for (l=0;l<j;l++) if (sta_3[i][l][k] == 1) sta_3[i][j][k] = 0;
						for (l=0;l<k;l++) if (sta_3[i][j][l] == 1) sta_3[i][j][k] = 0;
						for (l=1;l<=i&&l<=j;l++) if (sta_3[i-l][j-l][k] == 1) sta_3[i][j][k] = 0;
						for (l=1;l<=i&&l<=k;l++) if (sta_3[i-l][j][k-l] == 1) sta_3[i][j][k] = 0;
						for (l=1;l<=j&&l<=k;l++) if (sta_3[i][j-l][k-l] == 1) sta_3[i][j][k] = 0;
						for (l=1;l<=i&&l<=j&&l<=k;l++) if (sta_3[i-l][j-l][k-l] == 1) sta_3[i][j][k] = 0;
					}
				}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Stone();
					frame.setVisible(true);
					Thread th = new Thread(frame);
					th.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void go(int s1, int s2, int s3, int who)
	{
		Color bg[] = new Color[2];
		bg[0] = new Color(250, 250, 180);
		bg[1] = new Color(180, 250, 250);
		step++;
		
		if (s1 != where[1]) {
			text[1][where[1]].setBackground(bg[who]);
			text[1][s1].setBackground(new Color(0, 0, 0));
			text[1][where[1]].setText(String.valueOf(step));
			text[1][where[1]].flag = EMPTY;
			text[1][s1].flag = STONE;
			where[1] = s1;
		}
		if (s2 != where[2]) {
			text[2][where[2]].setBackground(bg[who]);
			text[2][s2].setBackground(new Color(0, 0, 0));
			text[2][where[2]].setText(String.valueOf(step));
			text[2][where[2]].flag = EMPTY;
			text[2][s2].flag = STONE;
			where[2] = s2;
		}
		if (s3 != where[3]) {
			text[3][where[3]].setBackground(bg[who]);
			text[3][s3].setBackground(new Color(0, 0, 0));
			text[3][where[3]].setText(String.valueOf(step));
			text[3][where[3]].flag = EMPTY;
			text[3][s3].flag = STONE;
			where[3] = s3;
		}
		
		int win = 1;
		for (int i=1;i<=stones;i++) if (where[i] != 0) win = 0;
		if (win == 1) {
			btn6.setEnabled(true);
			btn5.setText("开始");
			btn5.setEnabled(false);
			String str;
			if (who == 0) {
				str = "很遗憾，计算机赢了～";
			} else {
				str = "恭喜你，你赢了～";
			}
			JOptionPane.showMessageDialog(frame.getContentPane(), str, "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void run()
	{
		while (true)
		{
			int kk = 0;
			for (int o=1;o<=stones;o++) if (where[o] > 0) { kk = 1; break; }
			System.out.println(btn5.getText()+" "+kk+" "+nowgo);
			if (btn5.getText() == "开始" || kk == 0 || nowgo == MAN_GO) {
				try {
					Thread.sleep(2000);
				} catch(Exception e) {
					
				}
				continue;
			}
			
			if (nowgo == COMPUTER_GO)
			{
				int i,j=0,k=0,l,flag=0,s1=-1,s2=-1,s3=0;
				if (stones == 2) {
					j = where[2];
					for (i=where[1]-1;i>=0;i--)
						if (sta_2[i][j] == 1 && i+j>s1+s2) {flag = 1;s1=i; s2=j;}
					i = where[1];
					for (j=where[2]-1;j>=0;j--)
						if (sta_2[i][j] == 1 && i+j>s1+s2) {flag = 1;s1=i; s2=j;}
					for (k=1;k<=where[1]&&k<=where[2];k++){
						i = where[1]-k;j = where[2]-k;
						if (sta_2[i][j] == 1 && i+j>s1+s2) {flag = 1;s1=i; s2=j;}
					}
					if (flag == 0) {
						s1 = where[1]; s2 = where[2];
						if (s1 > s2) s1--; else s2--;
					}
					go(s1, s2, where[3], 0);
				} else {
					j = where[2]; k = where[3];
					for (i=where[1]-1;i>=0;i--)
						if (sta_3[i][j][k] == 1 && i+j+k>s1+s2+s3) { flag = 1; s1=i; s2=j; s3=k; }
					i = where[1]; k = where[3];
					for (j=where[2]-1;j>=0;j--)
						if (sta_3[i][j][k] == 1 && i+j+k>s1+s2+s3) { flag = 1; s1=i; s2=j; s3=k; }
					i = where[1]; j = where[2];
					for (k=where[3]-1;k>=0;k--)
						if (sta_3[i][j][k] == 1 && i+j+k>s1+s2+s3) { flag = 1; s1=i; s2=j; s3=k; }
					k = where[3];
					for (l=1;l<=where[1] && l<=where[2];l++) {
						i=where[1]-l; j=where[2]-l;
						if (sta_3[i][j][k] == 1 && i+j+k>s1+s2+s3) { flag = 1; s1=i; s2=j; s3=k; }
					}
					j = where[2];
					for (l=1;l<=where[1] && l<=where[3];l++) {
						i=where[1]-l; k=where[3]-l;
						if (sta_3[i][j][k] == 1 && i+j+k>s1+s2+s3) { flag = 1; s1=i; s2=j; s3=k; }
					}
					i = where[1];
					for (l=1;l<=where[2] && l<=where[3];l++) {
						j=where[2]-l; k=where[3]-l;
						if (sta_3[i][j][k] == 1 && i+j+k>s1+s2+s3) { flag = 1; s1=i; s2=j; s3=k; }
					}
					for (l=1;l<=where[1] && l<=where[2] && l<=where[3];l++) {
						i = where[1]-l; j=where[2]-l; k=where[3]-l;
						if (sta_3[i][j][k] == 1 && i+j+k>s1+s2+s3) { flag = 1; s1=i; s2=j; s3=k; }
					}
					if (flag == 0) {
						s1 = where[1]; s2 = where[2]; s3 = where[3];
						if (s1 >= s2 && s1 >= s3) s1--;
						else
						if (s2 >= s1 && s2 >= s3) s2--;
						else
							s3--;
					}
					go(s1, s2, s3, 0);
				}
				nowgo = MAN_GO;
			}
		}
	}
	
	void randomOne()
	{
		step = 0;
		nowgo = whofirst;
		btn5.setEnabled(true);
		for (int i=1;i<=3;i++) {
			for (int j=0;j<30;j++) {
				text[i][j].setText("");
				text[i][j].setBackground(new Color(255, 255, 255));
				text[i][j].flag = EMPTY;
			}
		}
		for (int i=1;i<=3;i++) {
			where[i] = (int)(Math.random()*10000)%29+1;
			text[i][where[i]].setBackground(new Color(0, 0, 0));
			text[i][where[i]].flag = STONE;
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Stone() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1005, 445);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		btn1 = new JButton("两堆石子");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i=0;i<30;i++) text[3][i].setVisible(false);
				stones = 2;
				btn1.setEnabled(false);
				btn2.setEnabled(true);
			}
		});
		btn1.setBounds(229, 46, 117, 25);
		btn1.setEnabled(false);
		contentPane.add(btn1);
		
		btn2 = new JButton("三堆石子");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i=0;i<30;i++) text[3][i].setVisible(true);
				stones = 3;
				btn1.setEnabled(true);
				btn2.setEnabled(false);
			}
		});
		btn2.setBounds(380, 46, 117, 25);
		contentPane.add(btn2);
		
		btn3 = new JButton("你先走");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				whofirst = 0;
				btn3.setEnabled(false);
				btn4.setEnabled(true);
			}
		});
		btn3.setBounds(229, 92, 117, 25);
		btn3.setEnabled(false);
		contentPane.add(btn3);
		
		btn4 = new JButton("计算机先走");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				whofirst = COMPUTER_GO;
				btn3.setEnabled(true);
				btn4.setEnabled(false);
				for (int i=1;i<=3;i++) text[i][where[i]].flag = STONE;
				nowgo = COMPUTER_GO;
			}
		});
		btn4.setBounds(380, 92, 117, 25);
		contentPane.add(btn4);
		
		btn6 = new JButton("随机生成一个");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				randomOne();
			}
		});
		btn6.setBounds(230, 133, 117, 25);
		contentPane.add(btn6);
		
		btn5 = new JButton("开始");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btn5.getText() == "开始") {
					btn5.setText("暂停");
					btn1.setEnabled(false);
					btn2.setEnabled(false);
					btn3.setEnabled(false);
					btn4.setEnabled(false);
					btn6.setEnabled(false);
					
				} else {
					btn5.setText("开始");
					btn6.setEnabled(true);
					if (stones == 2) {
						btn2.setEnabled(true);
					} else {
						btn1.setEnabled(true);
					}
					if (whofirst == MAN_GO) {
						btn4.setEnabled(true);
					} else {
						btn3.setEnabled(true);
					}
				}
			}
		});
		btn5.setBounds(383, 135, 117, 25);
		contentPane.add(btn5);
		
		JTextArea textArea = new JTextArea("规则：\n可以同时选择1行或2行或3行，将他们同时向前移动任意步。\n当对方无法移动时，获胜。\n\n单击黑色，当其变成蓝色时表示选中，可以选多行，然后在选中的某行的\n空白处单击，表示选中的都向前走若干步。");
		textArea.setEditable(false);
		textArea.setBounds(527, 52, 380, 100);
		contentPane.add(textArea);
		
		for (int i=0;i<4;i++)
			for (int j=0;j<30;j++) {
				text[i][j] = new TextF();
				text[i][j].setBounds(50+j*30, 200+i*30, 30, 30);
				text[i][j].setHorizontalAlignment(JTextField.CENTER);
				text[i][j].setBackground(new Color(255, 255, 255));
				text[i][j].flag = EMPTY;
				text[i][j].x = i;
				text[i][j].y = j;
				text[i][j].setEditable(false);
				if (i == 0) {
					text[i][j].setText(String.valueOf(j));
				}
				if (i == 3) {
					text[i][j].setVisible(false);
				}
				contentPane.add(text[i][j]);
			}
		
		stones = 2;
		whofirst = 0;
		randomOne();
	}
}
