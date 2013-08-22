import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.Image;
import javax.swing.ImageIcon;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import java.util.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;

public class HRD extends JFrame implements Runnable {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HRD frame = new HRD();
					frame.setVisible(true);
					frame.autoGo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		try {
			frame = new HRD();
			frame.setVisible(true);
			//frame.autoGo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static HRD frame;
	private JPanel contentPane;
	private String name[] = {"1", "2", "3", "4", "C", "G1", "G2", "Z1", "Z2", "Y1", "Y2", "M1", "M2", "H1", "H2"};
	private int startX[] = {100, 200, 300, 400,100,100,100,100,100,100,100,100,100,100,100};
	private int startY[] = {50,50,50,50,300,300,300,300,300,300,300,300,300,300,300};
	private JLabel status=new JLabel(), label[] = new JLabel[33];
	private ImageIcon icon[]=new ImageIcon[33];
	private int stop = 0, totalStep;
	private JButton btnNewButton, btnNewButton_1;
	private JComboBox comboBox;
	private Thread thread;
	private JTextField text[][] = new JTextField[5][4];
	private TextArea textArea_1;
	
	private void loadIcon()
	{
		int i;
		for (i=0;i<name.length;i++)
		{
			String tmp = "image/"+name[i]+".png";
			icon[i] = new ImageIcon(tmp);
			//icon[i] = new ImageIcon(this.getClass().getClassLoader().getResource(tmp));
		}
	}
	
	private ImageIcon getIcon(String str)
	{
		int i;
		for (i=0;i<name.length;i++)
			if (str.compareTo(name[i]) == 0){
				return icon[i];
			}
		return null;
	}
	
	private int getID(String str)
	{
		switch (str.charAt(0))
		{
		case '1':
		case '2':
		case '3':
		case '4':
			return 1;
		case 'C':
			return 4;
		default:
			if (str.charAt(1) == '1') {
				return 3;
			} else {
				return 2;
			}
		}
	}
	
	private Point getSize(String str)
	{
		Point p = new Point();
		switch (str.charAt(0))
		{
		case '1':
		case '2':
		case '3':
		case '4':
			p.x = 75; p.y = 75;
			return p;
		case 'C':
			p.x = 150; p.y = 150;
			return p;
		default:
			if (str.charAt(1) == '1') {
				p.x = 150; p.y = 75;
			} else {
				p.x = 75; p.y = 150;
			}
			return p;
		}
	}
	
	private int row[] = {85, 160, 235, 310, 385};
	private int col[] = {500, 575, 650, 725};
	private String proName[][] = {
			{"1", "2", "3", "4", "C", "G1", "Z2", "Y2", "M2", "H2"},
			{"1", "2", "3", "4", "C", "G1", "Z1", "Y1", "M1", "H2"},
			{"1", "2", "3", "4", "C", "G1", "Z2", "Y2", "M1", "H2"},
			{"1", "2", "3", "4", "C", "G1", "Z2", "Y2", "M1", "H1"},
			{"1", "2", "3", "4", "C", "G1", "Z2", "Y2", "M2", "H1"},
	};
	private int proX[][] = {
			{col[0], col[1], col[2], col[3], col[1], col[1], col[0], col[3], col[0], col[3]},
			{col[0], col[0], col[0], col[3], col[1], col[2], col[0], col[2], col[0], col[3]},
			{col[0], col[3], col[3], col[3], col[1], col[1], col[0], col[3], col[1], col[0]},
			{col[0], col[0], col[3], col[3], col[1], col[1], col[0], col[3], col[1], col[1]},
			{col[0], col[0], col[3], col[3], col[1], col[0], col[0], col[3], col[1], col[2]},
	};
	private int proY[][] = {
			{row[4], row[3], row[3], row[4], row[0], row[2], row[0], row[0], row[2], row[2]},
			{row[0], row[1], row[4], row[4], row[0], row[3], row[2], row[2], row[3], row[0]},
			{row[4], row[2], row[3], row[4], row[0], row[3], row[0], row[0], row[2], row[2]},
			{row[2], row[3], row[2], row[3], row[0], row[4], row[0], row[0], row[2], row[3]},
			{row[0], row[3], row[0], row[3], row[0], row[4], row[1], row[1], row[2], row[4]},
	};
	
	private void setPro(int N)
	{
		int i;
		for (i=0;i<10;i++)
		{
			Jname[i] = proName[N][i];
			
			Point p = getSize(proName[N][i]);
			label[i].setBounds(proX[N][i], proY[N][i], p.x, p.y);
			label[i].setIcon(getIcon(proName[N][i]));
		}
		totalStep = 0;
	}
	
	private int isRight(char c)
	{
		switch (c)
		{
		case 'G':
		case 'Z':
		case 'Y':
		case 'M':
		case 'H':
			for (int i=0;i<5;i++)
				for (int j=0;j<4;j++)
				{
					String s1 = text[i][j].getText();
					if (s1.length() == 1 && s1.charAt(0) == c)
					{
						if (j < 3 && text[i][j+1].getText().length() == 1 && text[i][j+1].getText().charAt(0) == c) return 1;
						if (i < 4 && text[i+1][j].getText().length() == 1 && text[i+1][j].getText().charAt(0) == c) return 1;
					}
				}
			return 0;
		case 'C':
			for (int i=0;i<5;i++)
				for (int j=0;j<4;j++)
				{
					String s1 = text[i][j].getText();
					if (s1.length() == 1 && s1.charAt(0) == c)
					{
						if (i<4 && j < 3 && text[i][j+1].getText().length() == 1 && text[i][j+1].getText().charAt(0) == c &&
								text[i+1][j].getText().length() == 1 && text[i+1][j].getText().charAt(0) == c &&
								text[i+1][j+1].getText().length() == 1 && text[i+1][j+1].getText().charAt(0) == c) return 1;
					}
				}
			return 0;
		}
		return 0;
	}
	
	private String checkInput()
	{
		int i,j,k=0,l,a[][]=new int[5][4], coun[]=new int[333];
		String tex[][]=new String[5][4], okName[]={"B", "G", "Z", "Y", "M", "H", "C"};
		for (i='A';i<='Z';i++) coun[i] = 0;
		for (i=0;i<5;i++)
			for (j=0;j<4;j++)
			{
				tex[i][j] = text[i][j].getText();
				if (tex[i][j].compareTo("") == 0) continue;
				if (tex[i][j].length() != 1) return "("+(i+1)+","+(j+1)+")不合法";
				for (l=0;l<okName.length;l++)
					if (okName[l].compareTo(tex[i][j]) == 0) break;
				if (l >= okName.length) return "("+(i+1)+","+(j+1)+")不合法";
				if (tex[i][j].charAt(0) < 'A' || tex[i][j].charAt(0) > 'Z') return "("+(i+1)+","+(j+1)+")不合法";
				coun[tex[i][j].charAt(0)] ++;
			}
		if (coun['B'] != 4) return "兵的个数不合法";
		if (coun['G'] != 2 || isRight('G') == 0) return "关羽占的格子不合法";
		if (coun['Z'] != 2 || isRight('Z') == 0) return "张飞占的格子不合法";
		if (coun['Y'] != 2 || isRight('Y') == 0) return "赵云占的格子不合法";
		if (coun['M'] != 2 || isRight('M') == 0) return "马超占的格子不合法";
		if (coun['H'] != 2 || isRight('H') == 0) return "黄忠占的格子不合法";
		if (coun['C'] != 4 || isRight('C') == 0) return "曹操占的格子不合法";
		return "";
	}
	
	private void setProNew()
	{
		String ret = checkInput();
		if (ret.compareTo("") != 0) {
			status.setText(ret);
			return;
		}
		Map mp = new HashMap();
		mp.clear();
		int i,j,idx=0,bing=0;
		for (i=0;i<5;i++)
		{
			for (j=0;j<4;j++)
			{
				String s1 = text[i][j].getText(), s2;
				s2 = s1;
				if (s1.length() == 0) continue;
				switch (s1.charAt(0)) {
				case 'B': s1 = s2 = String.valueOf(++bing); break;
				case 'G':
				case 'Z':
				case 'Y':
				case 'M':
				case 'H':
					if (j<3 && text[i][j+1].getText().length()==1 && text[i][j+1].getText().charAt(0) == s1.charAt(0))
						s2 = s1+"1";
					else
						s2 = s1+"2";
					break;
				}
				int l;
				for (l=0;l<name.length;l++) if (name[l].compareTo(s2) == 0) break;
				if (l < name.length && !mp.containsKey(s1)) {System.out.println(s1+" "+s2);
					mp.put(s1, 1);
					Jname[idx] = s2;
					
					Point p = getSize(s2);
					label[idx].setBounds(col[j], row[i], p.x, p.y);
					label[idx].setIcon(getIcon(s2));
					idx++;
				}
			}
		}
		totalStep = 0;
	}
	
	private int Jway[] = new int [10];
	private String Jname[] = new String[10];
	// 检查，{N,x0,y0,x1,y1... } 要满足 a[xi][yi] = 0;
	static int CK[][][] = {
		{{2,-1,0}, {2,1,0}, {2,0,-1}, {2,0,1}},
		{{2,-1,0}, {2,2,0}, {4,0,-1,1,-1}, {4,0,1,1,1}},
		{{4,-1,0,-1,1}, {4,1,0,1,1}, {2,0,-1}, {2,0,2}},
		{{4,-1,0,-1,1}, {4,2,0,2,1}, {4,0,-1,1,-1}, {4,0,2,1,2}}
	};
	private JTextField textField;
	
	private boolean tryGo(int idx, int w)
	{
		int i,ID = getID(Jname[idx]);
		ID--;
		for (i=1;i<=CK[ID][w][0];i+=2)
		{
			int dx = label[idx].getBounds().x+35 + CK[ID][w][i+1]*75;
			int dy = label[idx].getBounds().y+35 + CK[ID][w][i]*75;
			
			if (dx < col[0] || dx > col[0]+300 || dy < row[0] || dy > row[0]+375) return false;
			for (int j=0;j<10;j++)
				if (label[j].getBounds().inside(dx, dy))
					return false;
		}
		Rectangle rt = label[idx].getBounds();
		switch (w) {
		case 0: rt.y -= 75; break;
		case 1: rt.y += 75; break;
		case 2: rt.x -= 75; break;
		case 3: rt.x += 75; break;
		}
		label[idx].setBounds(rt);
		return true;
	}
	
	String showText(String jname, int w)
	{
		String s1 = "", s2="";
		switch (w) {
		case 0: s1 = "向上"; break;
		case 1: s1 = "向下"; break;
		case 2: s1 = "向左"; break;
		case 3: s1 = "向右"; break;
		}
		switch (jname.charAt(0))
		{
		case '1':
		case '2':
		case '3':
		case '4':
			return "兵"+jname+s1;
		case 'G': s2 = "关羽"; break;
		case 'Z': s2 = "张飞"; break;
		case 'Y': s2 = "赵云"; break;
		case 'M': s2 = "马超"; break;
		case 'H': s2 = "黄忠"; break;
		case 'C': s2 = "曹操"; break;
		}
		return s2+s1;
	}
	
	public void run()
	{
		String s[] = new String[5];
		for (int i=0;i<5;i++)
		{
			s[i] = "";
			for (int j=0;j<4;j++)
			{
				int dx = col[j]+35;
				int dy = row[i]+35;
				int k;
				for (k=0;k<10;k++) if (label[k].getBounds().inside(dx, dy)) break;
				if (k >= 10) s[i] += "0";
				else {
					s[i] += Jname[k].charAt(0);
				}
			}
			System.out.println(s[i]);
		}
		int ret[];
		ret = CalGo.getGo(s);
		int step = ret.length;
		if (step == 1 && ret[0] == -1) {
			status.setText("无解");
			System.out.println("无解");
			return;
		}
		
		System.out.println("step = "+(step/2));
		int i;
		for (i=0;i<step;i+=2)
		{
			if (stop == 1) break;
			totalStep++;
			status.setText("当前步数："+totalStep+",  剩余步数："+((step-i)/2-1));
			
			for (int j=0;j<10;j++) if (Jname[j].charAt(0) == ret[i])
			{
				tryGo(j, ret[i+1]);
				textArea_1.setText(textArea_1.getText() + "\n" + "第"+totalStep+"步：" + showText(Jname[j], ret[i+1]));
				textArea_1.setCaretPosition(textArea_1.getText().length());
				break;
			}
			try { Thread.sleep(300); } catch (Exception e) {}
		}
		if (i >= step) {
			stop = 1;
			btnNewButton.setText("自动走");
			comboBox.setEnabled(true);
			btnNewButton_1.setEnabled(true);
		}
	}
	/**
	 * Create the frame.
	 */
	public HRD() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (stop == 0)
					return;
				int i, x = e.getX(), y = e.getY();
				for (i=0;i<10;i++) {
					if (label[i].getBounds().inside(x, y)) break;
				}
				if (i < 10) {
					for (int j=0;j<4;j++)
					{
						Jway[i] = (Jway[i]+1) % 4;
						if (tryGo(i, Jway[i])) {
							totalStep++;
							status.setText("当前步数："+totalStep);
							if (Jway[i] < 2) Jway[i] = 1-Jway[i]; else Jway[i] = 5-Jway[i];
							break;
						}
					}
				}
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("状态：");
		lblNewLabel.setBounds(97, 552, 45, 24);
		contentPane.add(lblNewLabel);
		
		status = new JLabel("");
		status.setBounds(148, 552, 281, 24);
		contentPane.add(status);
		
		
		loadIcon();
		for (int i=0;i<10;i++) {
			label[i] = new JLabel();
			Jway[i] = 0;
			contentPane.add(label[i]);
		}
		JLabel back = new JLabel();
		back.setBounds(496, 80, 300+6, 375+6);
		back.setIcon(new ImageIcon("image/back2.png"));
		//back.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("image/back2.png")));
		contentPane.add(back);
		
		setPro(0);
		status.setText("第1关");
		totalStep = 0;
		stop = 1;
		
		String ItemName[] = new String[proName.length];
		for (int i=0;i<ItemName.length;i++) ItemName[i] = "第"+(i+1)+"关";
		comboBox = new JComboBox(ItemName);
		comboBox.setEnabled(true);
		comboBox.setBounds(241, 165, 90, 37);
		comboBox.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent e) {
	            String value = (String) e.getItem();
	            status.setText(value);
	            for (int i=0;i<proName.length;i++) if (value.compareTo("第"+(i+1)+"关") == 0) setPro(i);
	        }
	    });
		contentPane.add(comboBox);
		
		btnNewButton = new JButton("自动走");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				stop = 1-stop;
				System.out.println("stop = "+stop);
				if (stop == 1) {
					btnNewButton.setText("自动走");
					comboBox.setEnabled(true);
					btnNewButton_1.setEnabled(true);
				} else {
					btnNewButton.setText("暂停");
					comboBox.setEnabled(false);
					btnNewButton_1.setEnabled(false);
					thread = new Thread(frame);
					thread.start();
				}
			}
		});
		btnNewButton.setBounds(388, 229, 90, 37);
		contentPane.add(btnNewButton);
		
		
		btnNewButton_1 = new JButton("自定义");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setProNew();
			}
		});
		btnNewButton_1.setBounds(241, 296, 90, 37);
		contentPane.add(btnNewButton_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(55, 125, 311, 2);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(55, 245, 311, 2);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(55, 505, 311, 2);
		contentPane.add(separator_2);
		
		JTextPane textPane = new JTextPane();
		textPane.setText("说明: 自定义的时候 兵:B, 关羽:G, 张飞:Z, 赵云:Y, 马超:M, 黄忠:H, 曹操:C。B必须为4个，G、Z、Y、M、H各占两格且要相邻，C要占四格且为正方形");
		textPane.setBounds(201, 367, 177, 110);
		contentPane.add(textPane);
		
		textArea_1 = new TextArea();
		textArea_1.setBounds(499, 503, 300, 155);
		contentPane.add(textArea_1);
		
		JLabel lblNewLabel_1 = new JLabel("规则：将曹操移动到最后一行的中间即可");
		lblNewLabel_1.setBounds(55, 85, 311, 29);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("华容道");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 23));
		lblNewLabel_2.setBounds(392, 12, 108, 43);
		contentPane.add(lblNewLabel_2);
		
		
		// ============
		String init_str[][] = {
				{"Z", "C", "C", "Y"},
				{"Z", "C", "C", "Y"},
				{"M", "G", "G", "H"},
				{"M", "B", "B", "H"},
				{"B", "", "", "B"},
			};
		int sx = 50, sy=285;
		for (int i=0;i<5;i++) {
			for (int j=0;j<4;j++) {
				text[i][j] = new JTextField(init_str[i][j]);
				text[i][j].setBounds(sx+j*35, sy+i*35, 35, 35);
				contentPane.add(text[i][j]);
				text[i][j].setColumns(3);
			}
		}
	}
}

// BFS计算行进过程
class CalGo {
	// 原始表示：空格0，兵1、2、3、4，关羽G，张飞Z，赵云Y，马超M，黄忠H，曹操C。
	// 关张赵马黄可以随意横竖。
	// 搜索过程中的状态表示：空格0，兵1，竖将2，横将3，曹操4。
	
	// 当左上角的C在x,y时离终点的距离
	static int DIS[] = {4,3,4,5,3,2,3,4,2,1,2,3,1,0,1,2,2,1,2,3};
	
	// 检查，{N,x0,y0,x1,y1... } 前半部分要满足 a[xi][yi] = wi; 后半部分要满足 a[xi][yi] = 0;
	static int CK[][][] = {
		{{2,0,0,-1,0}, {2,0,0,1,0}, {2,0,0,0,-1}, {2,0,0,0,1}},
		{{2,1,0,-1,0}, {2,1,0,2,0}, {4,0,0,1,0,0,-1,1,-1}, {4,0,0,1,0,0,1,1,1}},
		{{4,0,0,0,1,-1,0,-1,1}, {4,0,0,0,1,1,0,1,1}, {2,0,1,0,-1}, {2,0,1,0,2}},
		{{4,1,0,0,1,-1,0,-1,1}, {4,1,0,0,1,2,0,2,1}, {4,1,0,0,1,0,-1,1,-1}, {4,1,0,0,1,0,2,1,2}}
	};
	
	// 移动，{N,x0,y0,x1,y1... } 前半部分要移走 a[xi][yi] = 0; 后半部分要填充 a[xi][yi] = wi;
	static int GO[][][] = {
		{{2,0,0,-1,0}, {2,0,0,1,0}, {2,0,0,0,-1}, {2,0,0,0,1}},
		{{2,1,0,-1,0}, {2,0,0,2,0}, {4,0,0,1,0,0,-1,1,-1}, {4,0,0,1,0,0,1,1,1}},
		{{4,0,0,0,1,-1,0,-1,1}, {4,0,0,0,1,1,0,1,1}, {2,0,1,0,-1}, {2,0,0,0,2}},
		{{4,1,0,1,1,-1,0,-1,1}, {4,0,0,0,1,2,0,2,1}, {4,0,1,1,1,0,-1,1,-1}, {4,0,0,1,0,0,2,1,2}}
	};
	
	static long init_sta[][]=new long[5][4];

	public static class Node {
		int pre, way;
		long status;
	};

	public static int caldis(long status)
	{
		int i;
		long sta = status;
		for (i=0;i<20;i++) {
			if ((sta&7) == 4) return DIS[14-i];
			sta >>= 3;
		}
		return 0;
	}

	static long cal(char s[][])
	{
		int i,j,k;
		char NAME[] = {'0','1','2','3','4','G','Z','Y','M','H','C'};
		int VAL[] = {0,1,1,1,1,2,2,2,2,2,4};
		long ret=0;
		for (i=0;i<5;i++)
			for (j=0;j<4;j++) {
				for (k=0;k<10;k++) if (NAME[k] == s[i][j]) break;
				if (VAL[k] == 2 && j<3 && s[i][j+1] == s[i][j]) VAL[k]++;
				ret = (ret<<3) + VAL[k];
			}
		return ret;
	}

	public static int[] go(int step, int way[], char ch[][])
	{
		//System.out.println(step);
		int i,j,k=0, ret[]=new int[step+step];
		char cc[]={'u','d','l','r'};
		for (i=step-1;i>=0;i--)
		{
			int w = way[i] & 7;
			int y = (way[i]>>4) & 7;
			int x = (way[i]>>8) & 7;
			int v = (way[i]>>12) & 255;
			
			//System.out.println(ch[x][y]+" "+cc[w]);
			ret[k++] = ch[x][y];
			ret[k++] = w;
			for (j=GO[v][w][0]+1; j<=GO[v][w][0]*2; j+=2) {
				ch[x+GO[v][w][j]][y+GO[v][w][j+1]] = ch[x][y];
			}
			for (j=1; j<=GO[v][w][0]; j+=2) {
				ch[x+GO[v][w][j]][y+GO[v][w][j+1]] = '0';
			}
			/*for (j=0;j<5;j++) {
				for (k=0;k<4;k++) printf("%c", ch[j][k]);
				printf("\n");
			}*/
		}
		return ret;
	}

	static int tryGo(long trygo[], int way[], long status)
	{
		long sta = status;
		int i,j,k,w,v,a[][]=new int[5][4], b[][]=new int[5][4], c[][]=new int[5][4], ret = 0;

		for (i=4;i>=0;i--)
			for (j=3;j>=0;j--)
			{
				a[i][j] = (int)(sta&7);
				sta >>= 3;
				b[i][j] = c[i][j] = 0;
				if (a[i][j] == 2) b[i][j] = 1; 
				if (i < 4) b[i][j] += b[i+1][j];
				if (a[i][j] == 3) c[i][j] = 1;
				if (j < 3) c[i][j] += c[i][j+1];
			}
		for (i=0;i<5;i++)
			for (j=0;j<4;j++)
			{
				if (a[i][j] == 0) continue;
				v = a[i][j]-1;
				for (w=0;w<4;w++) {
					int ok = 1;
					for (k=1;k<=CK[v][w][0];k+=2) {
						int dx = i+CK[v][w][k], dy=j+CK[v][w][k+1];
						if (!(dx>=0 && dx<5 && dy>=0 && dy<4 && a[dx][dy] == a[i][j])) ok = 0;
					}
					for (k=CK[v][w][0]+1;k<=CK[v][w][0]*2;k+=2) {
						int dx = i+CK[v][w][k], dy=j+CK[v][w][k+1];
						if (!(dx>=0 && dx<5 && dy>=0 && dy<4 && a[dx][dy] == 0)) ok = 0;
					}
					if (a[i][j] == 2 && (b[i][j]&1) != 0) ok = 0;
					if (a[i][j] == 3 && (c[i][j]&1) != 0) ok = 0;
					if (ok == 1) {
						way[ret] = (v<<12)+(i<<8) + (j<<4) + w;
						trygo[ret] = status;
						for (k=1;k<=GO[v][w][0];k+=2) {
							trygo[ret] -= a[i][j]*init_sta[i+GO[v][w][k]][j+GO[v][w][k+1]];
						}
						for (k=GO[v][w][0]+1;k<=GO[v][w][0]*2;k+=2) {
							trygo[ret] += a[i][j]*init_sta[i+GO[v][w][k]][j+GO[v][w][k+1]];
						}
						if (a[i][j] == 4)
						{
							if (w==0 || (w>=2 && j==1))
								trygo[ret] += (1L<<60);
							else
								trygo[ret] -= (1L<<60);
						}
						ret++;
					}
				}		
			}
		return ret;
	}

	static Set<Long> mp = new HashSet<Long>();
	static int N = 300000;
	static Node Q[] = new Node[N];

	public static int[] getGo(String[] args)
	{
		int i,j,k,ret;
		long trygo[]=new long[333];
		int way[]=new int[3333];
		char s[][]=new char[7][7];
		
		k = 0;
		for (i=4;i>=0;i--)
			for (j=3;j>=0;j--)
			{
				init_sta[i][j] = (1L<<k);
				k += 3;
			}
		for (i=0;i<N;i++) Q[i] = new Node();
		
		for (i=0;i<5;i++) s[i] = args[i].toCharArray();
		
		int op=0, cl=1;
		Q[op].status = cal(s);
		Q[op].status += ((long)caldis(Q[op].status)) << 60;
		Q[op].pre = -1;
		Q[op].way = -1;
		mp.clear();
		mp.add(Q[op].status);
			
		if ((Q[op].status>>60) == 0) {
			return new int[0];
		}
			
		while (op < cl)
		{
			ret = tryGo(trygo, way, Q[op].status);
			op++;
			for (i=0;i<ret;i++)
				if (!mp.contains(trygo[i])) {
					Q[cl].status = trygo[i];
					Q[cl].pre = op-1;
					Q[cl].way = way[i];
					cl++;
					k = (int)(trygo[i]>>60);
					if (k == 0) break;
					mp.add(trygo[i]);
				}
			if (k == 0) break;
		}
		//System.out.println(op+" "+cl+" "+k);
		if (k != 0) {
			int re[] = {-1};
			return re;
		}
		int step=0;
		int idx=cl-1;
		while (Q[idx].pre != -1) {
			way[step++] = Q[idx].way;
			idx = Q[idx].pre;
		}
		return go(step, way, s);
	}
}