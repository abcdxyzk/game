import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MyText extends JTextField {
	public int can;
	public MyText() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (can <= 0) {
					if (Line.color == 0) {
						setBackground(new Color(255, 255, 255));
						can = 0;
					} else if (Line.color == 1) {
						setBackground(new Color(0, 255, 0));
						can = -2;
					} else {
						setBackground(new Color(255, 0, 0));
						can = -3;
					}
				}
			}
		});
	}
}

public class Line extends JFrame implements Runnable {

	private JPanel contentPane;
	private MyText text[][]=new MyText[33][33], ans[][]=new MyText[33][33];
	private int n,m,calMin, showAns, A, B;
	private JButton bt1, bt2, bt3, btnNewButton;
	private MinDis minDis = new MinDis();
	private JLabel lb;
	private JComboBox comboBox;
	private static Thread th;
	
	public static int color;
	static Line frame;
	String tmp[];
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Line();
					frame.setVisible(true);
					//th = new Thread(frame);
					//th.start();
					//frame.runn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	int px[]={0,0,1,-1};
	int py[]={1,-1,0,0};
	
	void bfs()
	{
		A = B = 100;
		int i,j,k,l,op,cl,p[]=new int[233], flag[][]=new int[11][11];
		int Axy[]=new int[4], Bxy[]=new int[4];
		k = l = 0;
		for (i=1;i<=n;i++)
			for (j=1;j<=m;j++)
			{
				if (text[i][j].can == -2) A++;
				if (text[i][j].can == -3) B++;
				if (text[i][j].can == 2) { Axy[k++] = i; Axy[k++] = j; }
				if (text[i][j].can == 3) { Bxy[l++] = i; Bxy[l++] = j; }
				flag[i][j] = 0;
			}
		
		op = 0; cl = 2; p[0] = Axy[0]; p[1] = Axy[1]; flag[Axy[0]][Axy[1]] = 1;
		while (op < cl)
		{
			k = p[op++]; l = p[op++];
			for (i=0;i<4;i++)
			{
				int dx = k + px[i];
				int dy = l + py[i];
				if (dx>0 && dy>0 && dx<=n && dy <= m && flag[dx][dy] == 0 && (text[dx][dy].can == -2 || text[dx][dy].can == 2))
				{
					flag[dx][dy] = 1;
					p[cl++] = dx; p[cl++] = dy;
				}
			}
		}
		if (flag[Axy[2]][Axy[3]] == 0) A = -A;
		
		for (i=1;i<=n;i++)
			for (j=1;j<=m;j++) flag[i][j] = 0;
		op = 0; cl = 2; p[0] = Bxy[0]; p[1] = Bxy[1]; flag[Bxy[0]][Bxy[1]] = 1;
		while (op < cl)
		{
			k = p[op++]; l = p[op++];
			for (i=0;i<4;i++)
			{
				int dx = k + px[i];
				int dy = l + py[i];
				if (dx>0 && dy>0 && dx<=n && dy <= m && flag[dx][dy] == 0 && (text[dx][dy].can == -3 || text[dx][dy].can == 3))
				{
					flag[dx][dy] = 1;
					p[cl++] = dx; p[cl++] = dy;
				}
			}
		}
		if (flag[Bxy[2]][Bxy[3]] == 0) B = -B;
	}
	
	void drawAns()
	{
		if (showAns == 0)
		{
			for (int i=1;i<=n;i++)
				for (int j=1;j<=m;j++) ans[i][j].setVisible(false);
			return;
		}
		for (int i=1;i<=n;i++)
			for (int j=1;j<=m;j++) ans[i][j].setVisible(true);
		
		int L = tmp.length;
		int i;
		for (i=0;i<L-1;i++)
		{
			String tm[] = tmp[i].split(",");
			int x = Integer.parseInt(tm[0]);
			int y = Integer.parseInt(tm[1]);
			if (tm[2].compareTo("1") == 0) {
				ans[x][y].setBackground(new Color(0, 255, 0));
			} else if (tm[2].compareTo("2") == 0){
				ans[x][y].setBackground(new Color(255, 0, 0));
			}
		}
	}
	
	public void runn()
	{
		int i,j,can[][]=new int[33][33];
		for (i=1;i<=n;i++)
			for (j=1;j<=m;j++) can[i][j] = text[i][j].can;
		
		long t1 = new Date().getTime();
		String ret = minDis.main(n, m, can);
		long t2 = new Date().getTime();
		
		tmp = ret.split(";");
		int L = tmp.length;
		if (tmp[L-1].compareTo("-1") == 0) {
			lb.setText("无解, 计算耗时："+(t2-t1)/1000.0+"秒");
		} else {
			lb.setText("最优解法需"+(Integer.parseInt(tmp[L-1])-2)+ "步, 计算耗时："+(t2-t1)/1000.0+"秒");
		}
	}
	public void run()
	{
		int i,j,can[][]=new int[33][33];
		for (i=1;i<=n;i++)
			for (j=1;j<=m;j++) can[i][j] = text[i][j].can;
		
		long t1 = new Date().getTime();
		String ret = minDis.main(n, m, can);
		long t2 = new Date().getTime();
		
		tmp = ret.split(";");
		int L = tmp.length;
		if (tmp[L-1].compareTo("-1") == 0) {
			lb.setText("无解, 计算耗时："+(t2-t1)/1000.0+"秒");
		} else {
			lb.setText("最优解法需"+(Integer.parseInt(tmp[L-1])-2)+ "步, 计算耗时："+(t2-t1)/1000.0+"秒");
		}
	}
	
	String ini[][] = {
			{"5,9", "3,2,3,8,2,5,4,5", ""},
			{"6,5", "1,1,6,3,2,2,6,4", "4,1,4,2,4,3"},
			{"5,5", "3,1,3,3,2,4,5,5", "4,1,4,3,4,4,4,5"},
			{"9,9", "9,7,9,9,2,2,9,8", ""},
			{"9,9", "2,2,3,9,2,9,4,9", "1,4,2,4,3,4,4,4,5,4,5,5,5,6"},
	};
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	
	
	void init(int idx)
	{
		int i,j;
		String tmp[] = ini[idx][0].split(",");
		n = Integer.parseInt(tmp[0]);
		m = Integer.parseInt(tmp[1]);
		init_one(ini[idx][1], ini[idx][2]);
	}
	
	void init_one(String AB, String X)
	{
		int i,j;
		for (i=1;i<=10;i++)
			for (j=1;j<=10;j++) {
				text[i][j].setText("");
				ans[i][j].setText("");
				ans[i][j].setVisible(false);
				if (i <= n && j <= m) {
					text[i][j].setVisible(true);
				} else {
					text[i][j].setVisible(false);
				}
				ans[i][j].setBackground(new Color(255, 255, 255));	
				text[i][j].setBackground(new Color(255, 255, 255));
				text[i][j].can = 0;
			}

		tmp = AB.split(",");
		int x = Integer.parseInt(tmp[0]);
		int y = Integer.parseInt(tmp[1]);
		ans[x][y].setText("A");
		ans[x][y].setBackground(new Color(0, 255, 0));
		text[x][y].setText("A");
		text[x][y].setBackground(new Color(0, 255, 0));
		text[x][y].can = 2;
		x = Integer.parseInt(tmp[2]);
		y = Integer.parseInt(tmp[3]);
		ans[x][y].setText("A");
		ans[x][y].setBackground(new Color(0, 255, 0));
		text[x][y].setText("A");
		text[x][y].setBackground(new Color(0, 255, 0));
		text[x][y].can = 2;
		
		x = Integer.parseInt(tmp[4]);
		y = Integer.parseInt(tmp[5]);
		ans[x][y].setText("B");
		ans[x][y].setBackground(new Color(255, 0, 0));
		text[x][y].setText("B");
		text[x][y].setBackground(new Color(255, 0, 0));
		text[x][y].can = 3;
		x = Integer.parseInt(tmp[6]);
		y = Integer.parseInt(tmp[7]);
		ans[x][y].setText("B");
		ans[x][y].setBackground(new Color(255, 0, 0));
		text[x][y].setText("B");
		text[x][y].setBackground(new Color(255, 0, 0));
		text[x][y].can = 3;
		
		if (X.compareTo("") != 0)
		{
			tmp = X.split(",");
			for (i=0;i<tmp.length;i+=2) {
				x = Integer.parseInt(tmp[i]);
				y = Integer.parseInt(tmp[i+1]);
				ans[x][y].setText("X");
				ans[x][y].setBackground(new Color(0, 0, 0));
				text[x][y].setText("X");
				text[x][y].setBackground(new Color(0, 0, 0));
				text[x][y].can = 1;
			}
		}
		
		color = 1;
		bt1.setEnabled(true);
		bt2.setEnabled(false);
		bt3.setEnabled(true);
		
		runn();
		
		showAns = 0;
		btnNewButton.setText("显示答案");
		drawAns();
	}

	void random()
	{
		n = (int)(Math.random()*10000)%6+4;
		m = (int)(Math.random()*10000)%6+4;
		int x,y,T = 4,a[][]=new int[n+2][m+2];
		for (int i=0;i<=n+1;i++)
			for (int j=0;j<=m+1;j++) a[i][j] = 0;
		
		String s1 = "", s2 = "";
		
		while (T-- > 0)
		{
			while (true)
			{
				x = (int)(Math.random()*10000) % n+1;
				y = (int)(Math.random()*10000) % m+1;
				if (a[x][y] == 0) break;
			}
			a[x][y] = 1;
			s1 += ","+x+","+y;
		}
		T = (int)(Math.random()*10000) % (n*m/10);
		while (T-- > 0)
		{
			while (true)
			{
				x = (int)(Math.random()*10000) % n+1;
				y = (int)(Math.random()*10000) % m+1;
				if (a[x][y] == 0) break;
			}
			a[x][y] = 1;
			s2 += ","+x+","+y;
		}
		s1 = s1.substring(1);
		if (s2.length() > 1) s2 = s2.substring(1);
		init_one(s1, s2);
	}
	
	/**
	 * Create the frame.
	 */
	public Line() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 825, 503);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		bt1 = new JButton("橡皮擦");
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				color = 0;
				bt1.setEnabled(false);
				bt2.setEnabled(true);
				bt3.setEnabled(true);
			}
		});
		bt1.setBounds(96, 193, 74, 25);
		contentPane.add(bt1);
		
		bt2 = new JButton("绿色");
		bt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				color = 1;
				bt1.setEnabled(true);
				bt2.setEnabled(false);
				bt3.setEnabled(true);
			}
		});
		bt2.setBounds(96, 249, 74, 25);
		contentPane.add(bt2);
		
		bt3 = new JButton("红色");
		bt3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				color = 2;
				bt1.setEnabled(true);
				bt2.setEnabled(true);
				bt3.setEnabled(false);
			}
		});
		bt3.setBounds(96, 303, 74, 25);
		contentPane.add(bt3);
		
		JLabel lblNewLabel_2 = new JLabel("选择画笔");
		lblNewLabel_2.setBounds(28, 249, 60, 25);
		contentPane.add(lblNewLabel_2);
		
		lb = new JLabel("");
		lb.setBounds(556, 308, 255, 25);
		contentPane.add(lb);
		
		btnNewButton = new JButton("显示答案");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (showAns == 0) {
					showAns = 1;
					btnNewButton.setText("隐藏答案");
				} else {
					showAns = 0;
					btnNewButton.setText("显示答案");
				}
				drawAns();
			}
		});
		btnNewButton.setBounds(574, 265, 89, 25);
		contentPane.add(btnNewButton);
		
		String Name[] = new String[5];
		for (int i=0;i<5;i++) {
			Name[i] = "第"+(i+1)+"关";
		}
		comboBox = new JComboBox(Name);
		comboBox.setBounds(217, 117, 97, 25);
		comboBox.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent e) {
	            init(comboBox.getSelectedIndex());
	            //th = new Thread(frame);
				//th.start();
	            runn();
	        }
	    });
		contentPane.add(comboBox);
		
		JButton btnNewButton_1 = new JButton("随机生成一个");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do {
					random();
				} while (tmp[tmp.length-1].compareTo("-1") == 0);
			}
		});
		btnNewButton_1.setBounds(354, 117, 115, 25);
		contentPane.add(btnNewButton_1);
		
		lblNewLabel = new JLabel("或");
		lblNewLabel.setBounds(325, 117, 35, 25);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("不相交的线");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 23));
		lblNewLabel_1.setBounds(287, 12, 145, 40);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_3 = new JLabel("规则：将A用绿色连接，B用红色连接，X格子不可用。要求两种线的距离之和最小。");
		lblNewLabel_3.setBounds(57, 69, 486, 25);
		contentPane.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setBounds(556, 367, 255, 27);
		contentPane.add(lblNewLabel_4);
		
		for (int i=1;i<=10;i++)
			for (int j=1;j<=10;j++)
			{
				ans[i][j] = new MyText();
				ans[i][j].setEditable(false);
				ans[i][j].setVisible(false);
				ans[i][j].setFont(new Font("宋体", Font.PLAIN, 10));
				ans[i][j].setBounds(550+j*17, 50+i*17, 17, 17);
				ans[i][j].setHorizontalAlignment(JTextField.CENTER);
				contentPane.add(ans[i][j]);
				
				text[i][j] = new MyText();
				text[i][j].setEditable(false);
				text[i][j].setVisible(false);
				text[i][j].setFont(new Font("宋体", Font.PLAIN, 20));
				text[i][j].setBounds(170+j*30, 150+i*30, 30, 30);
				text[i][j].setHorizontalAlignment(JTextField.CENTER);
				text[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						bfs();
						lblNewLabel_4.setText("已经画了"+(Math.abs(A)+Math.abs(B)-200)+"格，A"+(A>0?"连通":"未连通")+", B"+(B>0?"连通":"未连通"));
					}
				});
				contentPane.add(text[i][j]);
			}
		init(0);
	}
}

class MinDis {
	// max(n) = max(m) = 10
	final int N = 13; // n+3
	final int M = 177147+100; // 3^(m+1)

	int n,m;

	int dp[][]=new int[2][M],H[][]=new int[M][N],val[]=new int[N];
	String dps[][]=new String[2][M];
	
	String solve(int can[][])
	{
	    int i,j,k,LL,UU,all,sta,u,y;

	    all = val[m+2]; u = 0;
	    for(i=0;i<all;i++) {
	    	dp[u][i] = 1000000;
	    	dps[u][i] = "";
	    }
	    dp[u][0] = 0;

	    for(i=1;i<=n;i++)
	        for(j=1;j<=m;j++)
	        {
	        	y = u; u = 1-u;
	            if(j == 1)
	            {
	                for(k=all-1;k>=0;k--)
	                {
	                    dp[y][k] = dp[y][k/3];
	                    dps[y][k] = dps[y][k/3];
	                    if(k%3 != 0) dp[y][k] = 1000000;
	                }
	            }

	            for(k=0;k<all;k++) {
	            	dp[u][k] = 1000000;
	            	dps[u][k] = "";
	            }

	            for(k=0;k<all;k++)
	            {
	                LL = H[k][j]; UU = H[k][j+1];

	                if(can[i][j] == 1)
	                {
	                    if(LL == 0 && UU == 0)
	                    {
	                        dp[u][k] = dp[y][k];
	                        dps[u][k] = dps[y][k];
	                    }
	                    continue;
	                }

	                if(can[i][j] == 0)
	                {
	                    if(LL == 0 && UU == 0)
	                    {
	                        if(dp[u][k] > dp[y][k]) { dp[u][k] = dp[y][k]; dps[u][k] = dps[y][k]; }

	                        sta = k+val[j]+val[j+1];
	                        if(dp[u][sta] > dp[y][k] + 2) { dp[u][sta] = dp[y][k]+2; dps[u][sta] = dps[y][k] + (i+","+j+",1,3;"); }

	                        sta = k+(val[j]+val[j+1])*2;
	                        if(dp[u][sta] > dp[y][k] + 2) { dp[u][sta] = dp[y][k]+2; dps[u][sta] = dps[y][k] + (i+","+j+",2,3;"); }
	                    }
	                    else
	                    if(LL == 0)
	                    {
	                        if(dp[u][k] > dp[y][k]+1) { dp[u][k] = dp[y][k]+1; dps[u][k] = dps[y][k] + (i+","+j+","+UU+","+1+";"); }

	                        sta = k-val[j+1]*UU+val[j]*UU;
	                        if(dp[u][sta] > dp[y][k] + 1) { dp[u][sta] = dp[y][k]+1; dps[u][sta] = dps[y][k] + (i+","+j+","+UU+","+2+";"); }
	                    }
	                    else
	                    if(UU == 0)
	                    {
	                    	if(dp[u][k] > dp[y][k]+1) { dp[u][k] = dp[y][k]+1; dps[u][k] = dps[y][k] + (i+","+j+","+LL+","+2+";"); }

	                        sta = k-val[j]*LL+val[j+1]*LL;
	                        if(dp[u][sta] > dp[y][k] + 1) { dp[u][sta] = dp[y][k]+1; dps[u][sta] = dps[y][k] + (i+","+j+","+LL+","+1+";"); }
	                    }
	                    else
	                    if(LL == 1 && UU == 1)
	                    {
	                        sta = k-val[j]-val[j+1];
	                        if(dp[u][sta] > dp[y][k]) { dp[u][sta] = dp[y][k]; dps[u][sta] = dps[y][k] + (i+","+j+","+LL+","+4+";"); }
	                    }
	                    else
	                    if(LL == 2 && UU == 2)
	                    {
	                        sta = k-(val[j]+val[j+1])*2;
	                        if(dp[u][sta] > dp[y][k]) { dp[u][sta] = dp[y][k]; dps[u][sta] = dps[y][k] + (i+","+j+","+LL+","+4+";");; }
	                    }
	                }
	                else
	                if(can[i][j] == 2)
	                {
	                    if(LL == 0 && UU == 0)
	                    {
	                        sta = k+val[j];
	                        if(dp[u][sta] > dp[y][k] + 1) { dp[u][sta] = dp[y][k]+1; dps[u][sta] = dps[y][k] + (i+","+j+",1,2;"); }

	                        sta = k+val[j+1];
	                        if(dp[u][sta] > dp[y][k] + 1) { dp[u][sta] = dp[y][k]+1; dps[u][sta] = dps[y][k] + (i+","+j+",1,1;"); }
	                    }
	                    else
	                    if((LL == 1 && UU == 0) || (LL == 0 && UU == 1))
	                    {
	                        sta = k-LL*val[j]-UU*val[j+1];
	                        if(dp[u][sta] > dp[y][k]) { dp[u][sta] = dp[y][k];  dps[u][sta] = dps[y][k]; }
	                    }
	                }
	                else
	                if(can[i][j] == 3)
	                {
	                    if(LL == 0 && UU == 0)
	                    {
	                        sta = k+val[j]*2;
	                        if(dp[u][sta] > dp[y][k] + 1) { dp[u][sta] = dp[y][k]+1; dps[u][sta] = dps[y][k] + (i+","+j+","+(22)+";"); }

	                        sta = k+val[j+1]*2;
	                        if(dp[u][sta] > dp[y][k] + 1) { dp[u][sta] = dp[y][k]+1; dps[u][sta] = dps[y][k] + (i+","+j+","+(21)+";"); }
	                    }
	                    else
	                    if((LL == 2 && UU == 0) || (LL == 0 && UU == 2))
	                    {
	                        sta = k-LL*val[j]-UU*val[j+1];
	                        if(dp[u][sta] > dp[y][k]) { dp[u][sta] = dp[y][k];  dps[u][sta] = dps[y][k]; }
	                    }
	                }
	            }
	        }
	    if(dp[u][0] == 1000000) dp[u][0] = -1;
	    return dps[u][0] + String.valueOf(dp[u][0]);
	}

	// can[][] = 1 表示不能放， 2表示一种点， 3表示另一种点
	public String main(int _n, int _m, int can[][])
	{
	    int i,j;

	    val[1] = 1;
	    for(i=2;i<=10+2;i++) val[i] = val[i-1]*3;
	    for(i=0;i<val[10+2];i++) for(j=1;j<=10+1;j++) H[i][j] = i/val[j]%3;

	    n = _n; m = _m;
	    return solve(can);
	}
	/*
	2 3
	2 2 0
	0 3 3
	*/
}
