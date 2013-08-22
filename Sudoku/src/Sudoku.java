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

public class Sudoku extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sudoku frame = new Sudoku();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JTextField text[][]=new JTextField[33][33];
	private JLabel status;
	private JComboBox comboBox;
	private JButton btnNewButton_1;
	private CalSudoku sudoku = new CalSudoku();
	private JButton btnNewButton;
	
	int N, M, a[][]=new int[33][33], b[][]=new int[33][33];
	private JLabel lblNewLabel_2;
	
	void init(int _N)
	{
		N = _N;
		M = (int)Math.sqrt(N);
		
		int i,j,k,l;
		for (i=0;i<16;i++)
			for (j=0;j<16;j++) {
				text[i][j].setVisible(false);
				text[i][j].setText("");
				text[i][j].setForeground(new Color(0, 0, 0));
				text[i][j].setBackground(new Color(255, 255, 255));
			}
		for (i=0;i<N;i++)
			for (j=0;j<N;j++)
			{
				text[i][j].setBounds(400-N*20+j*30, 250-M*30+i*30, 30, 30);
				text[i][j].setVisible(true);
				int x = i/M, y = j/M;
				Color bg = new Color(200, 200, 200);
				if ((x+y) % 2 == 1)
					text[i][j].setBackground(bg);
			}
	}
	
	String check(int a[][], int c[])
	{
		int i,j,k,l,d[]=new int[33];
		
		for (i=0;i<N;i++)
		{
			for (j=1;j<=N;j++) d[j] = -1;
			for (j=0;j<N;j++)
			if (a[i][j] > 0)
			{
				if (d[a[i][j]] != -1)
				{
					c[0] = 4;
					c[1] = d[a[i][j]]/1000; c[2] = d[a[i][j]]%1000; c[3] = i; c[4] = j;
					return "第"+(i+1)+"行有两个"+a[i][j];
				}
				d[a[i][j]] = i*1000 + j;
			}
		}
		for (i=0;i<N;i++)
		{
			for (j=1;j<=N;j++) d[j] = -1;
			for (j=0;j<N;j++)
			if (a[j][i] > 0)
			{
				if (d[a[j][i]] != -1)
				{
					c[0] = 4;
					c[1] = d[a[j][i]]/1000; c[2] = d[a[j][i]]%1000; c[3] = j; c[4] = i;
					return "第"+(i+1)+"列有两个"+a[j][i];
				}
				d[a[j][i]] = j*1000 + i;
			}
		}
		for (i=0;i<M;i++)
			for (j=0;j<M;j++)
			{
				for (k=1;k<=N;k++) d[k] = -1;
				for (k=0;k<M;k++)
					for (l=0;l<M;l++)
					{
						int x = i*M + k, y = j*M + l;
						if (a[x][y] < 0) continue;
						if (d[a[x][y]] != -1)
						{
							c[0] = 4;
							c[1] = d[a[x][y]]/1000; c[2] = d[a[x][y]]%1000; c[3] = x; c[4] = y;
							return "第"+(x*M + y + 1)+"个方格有两个"+a[x][y];
						}
						d[a[x][y]] = x*1000 + y;
					}
			}
		return "";
	}
	/**
	 * Create the frame.
	 */
	public Sudoku() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 793, 682);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		for (int i=0;i<16;i++)
			for (int j=0;j<16;j++)
			{
				text[i][j] = new JTextField();
				text[i][j].setFont(new Font("宋体", Font.PLAIN, 16));
				text[i][j].setHorizontalAlignment(JTextField.CENTER);
				contentPane.add(text[i][j]);
			}
		
		init(4);
		
		String ItemName[] = {"4*4","9*9"};//,"16*16"};
		comboBox = new JComboBox(ItemName);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String value = (String) e.getItem();
	            if (value.compareTo("4*4") == 0) init(4);
	            if (value.compareTo("9*9") == 0) init(9);
	            if (value.compareTo("16*16") == 0) init(16);
	            btnNewButton.setEnabled(true);
	            btnNewButton_1.setEnabled(false);
			}
		});
		comboBox.setBounds(653, 83, 77, 30);
		contentPane.add(comboBox);
		
		btnNewButton = new JButton("计算");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int c[]=new int[5];
				for (int i=0;i<N;i++)
					for (int j=0;j<N;j++)
					{
						int x = i/M, y = j/M;
						if ((x+y) % 2 == 1)
							text[i][j].setBackground(new Color(200, 200, 200));
						else
							text[i][j].setBackground(new Color(255, 255, 255));
						text[i][j].setForeground(new Color(0, 0, 0));
					}
				for (int i=0;i<N;i++)
					for (int j=0;j<N;j++)
					{
						String val = text[i][j].getText();
						if (val.compareTo("") == 0)
						{
							a[i][j] = -1; b[i][j] = -1;
						}
						else
						{
							b[i][j] = 0;
							try {
								a[i][j] = Integer.parseInt(val);
							} catch (Exception e) {
								text[i][j].setBackground(new Color(255, 0, 0));
								status.setText("("+(i+1)+","+(j+1)+") 格不是整数");
								return;
							}
							if (a[i][j] < 1 || a[i][j] > N) {
								text[i][j].setBackground(new Color(255, 0, 0));
								status.setText("("+(i+1)+","+(j+1)+") 格的数应该大等于1，小等于"+N);
								return;
							}
						}
					}
				String ret = check(a, c);
				if (ret.compareTo("") != 0) {
					for (int i=1;i<=c[0];i+=2) text[c[i]][c[i+1]].setBackground(new Color(255, 0, 0));
					status.setText(ret);
					return;
				}
				long t1 = new Date().getTime();
				a = sudoku.main(M, a);
				long t2 = new Date().getTime();
				
				if (a[0][0] == -1) {
					status.setText("无解");
					return;
				}
				status.setText("计算成功，耗时："+1.0*(t2-t1)/1000.0+"秒");
				for (int i=0;i<N;i++)
					for (int j=0;j<N;j++)
					if (b[i][j] == -1)
					{
						text[i][j].setText(String.valueOf(a[i][j]));
						text[i][j].setForeground(new Color(50, 0, 255));
					}
				btnNewButton_1.setEnabled(true);
				btnNewButton.setEnabled(false);
			}
		});
		btnNewButton.setBounds(653, 139, 77, 31);
		contentPane.add(btnNewButton);
		
		status = new JLabel("");
		status.setBounds(125, 91, 439, 21);
		contentPane.add(status);
		
		btnNewButton_1 = new JButton("删除系统添加的数");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i=0;i<N;i++)
					for (int j=0;j<N;j++)
					{
						if (b[i][j] == -1)
						{
							text[i][j].setText("");
							text[i][j].setForeground(new Color(0, 0, 0));
						}
					}
				btnNewButton_1.setEnabled(false);
				btnNewButton.setEnabled(true);
			}
		});
		btnNewButton_1.setBounds(608, 281, 157, 31);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("清空");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String value = comboBox.getSelectedItem().toString();
	            if (value.compareTo("4*4") == 0) init(4);
	            if (value.compareTo("9*9") == 0) init(9);
	            if (value.compareTo("16*16") == 0) init(16);
	            btnNewButton.setEnabled(true);
	            btnNewButton_1.setEnabled(false);
			}
		});
		btnNewButton_2.setBounds(653, 204, 77, 30);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_1 = new JLabel("数独");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 23));
		lblNewLabel_1.setBounds(357, 8, 85, 34);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("规则：每行、每列、每块正方形的数都是1～N。可以预填一些数。");
		lblNewLabel_2.setBounds(87, 46, 441, 26);
		contentPane.add(lblNewLabel_2);
	}
}

class CalSudoku {
	final static int MAXN = 730; //4097; // N*N*N + 1
	final static int MAXM = 325; //1025; // N*N*4 + 1
	
	int L[]=new int[MAXN*MAXM], R[]=new int[MAXN*MAXM], U[]=new int[MAXN*MAXM], D[]=new int[MAXN*MAXM];
	int S[]=new int[MAXN];
	int Col[]=new int[MAXN*MAXM], Row[]=new int[MAXN*MAXM],Ans[]=new int[MAXN],ans,limit,up;

	void Remove(int c) {
		L[R[c]] = L[c];
		R[L[c]] = R[c];
		for (int i = D[c]; i != c; i = D[i])
		for (int j = R[i]; j != i; j = R[j]) {
			U[D[j]] = U[j];
			D[U[j]] = D[j];
			-- S[Col[j]];
		}
	}
	void Resume(int c) {
		for (int i = U[c]; i != c; i = U[i])
		for (int j = L[i]; j != i; j = L[j]) {
			U[D[j]] = j;
			D[U[j]] = j;
			++ S[Col[j]];
		}
		L[R[c]] = c;
		R[L[c]] = c;
	}

	boolean dfs(int depth) {
		//printf("ddd = %d  R0 = %d\n",depth,R[0]);
		if(R[0] == 0) { if(depth > ans)ans = depth; return true; }
		int i, j, c=0, minnum = 1000000000;
		for (i = R[0]; i != 0; i = R[i]) {
			if (S[i] < minnum) {
				minnum = S[i];
				c = i;
			}
		}
		Remove(c);
		for (i = U[c]; i != c; i = U[i]) {
			Ans[depth] = Row[i];
			for (j = R[i]; j != i; j = R[j]) Remove(Col[j]);
			if (dfs(depth + 1)) return true; 
			for (j = L[i]; j != i; j = L[j]) Resume(Col[j]);
		}
		Resume(c);
		return false;
	}
	int solve(int n, int m, int DL[][]) {
		for (int i = 0; i <= m; i ++) {
			L[i] = i - 1;
			R[i] = i + 1;
			U[i] = D[i] = i;
		}
		L[0] = m;
		R[m] = 0;
		int cnt = m + 1;
		for (int i=0;i<S.length;i++) S[i] = 0;
		for (int i = 1; i <= n; i ++) {
			int head = cnt, tail = cnt;
			for (int c = 1; c <= m; c ++) if (DL[i][c] == 1) {
				S[c] ++;
				Row[cnt] = i;
				Col[cnt] = c;
				U[D[c]] = cnt;
				D[cnt] = D[c];
				U[cnt] = c;
				D[c] = cnt;
				L[cnt] = tail;
				R[tail] = cnt;
				R[cnt] = head;
				L[head] = cnt;
				tail = cnt;
				cnt ++;
			}
		}
		if (dfs(0)) return 1;
		return 0;
	}


	int M, N;
	int mark[][]=new int[MAXN][MAXM],x[]=new int[MAXN],y[]=new int[MAXN],z[]=new int[MAXN];

	int getColId(int j, int k)
	{
	    return j*N + k;
	}

	int getRowId(int i, int k)
	{
	    return N*N + i*N + k;
	}

	int getCubeId(int i, int j, int k)
	{
	    i /= M;
	    j /= M;
	    return N*N + N*N + (i*M + j)*N + k;
	}

	int[][] main(int _M, int a[][])
	{
		int i,j,k,l,ii, row, col;
	    M = _M; N = M * M;

		row = 0; col = N*N*3;
		for(i=0;i<N;i++)
			for(j=0;j<N;j++)
	        {//printf("   %d %d %d   %d  %d\n", i,j,a[i][j], row, col);
	            if (a[i][j] == -1)
	            {
	                col++;
	                for(k=1;k<=N;k++)
				    {
					    row++; x[row] = i; y[row] = j; z[row] = k;
					    //if(i == 1 && j == 7 && k == 4)printf("row ===== %d\n",row);
					    for(ii=1;ii<=N*N*4;ii++) mark[row][ii] = 0;
                        mark[row][getColId(j, k)] = 1;
	                    mark[row][getRowId(i, k)] = 1;
	                    mark[row][getCubeId(i, j, k)] = 1;
	                    mark[row][col] = 1;
	                }
	            }
	            else
	            {
	            	row++; x[row] = i; y[row] = j; z[row] = a[i][j];
					for(ii=1;ii<=N*N*4;ii++) mark[row][ii] = 0;
					mark[row][getColId(j, a[i][j])] = 1;
					mark[row][getRowId(i, a[i][j])] = 1;
					mark[row][getCubeId(i, j, a[i][j])] = 1;
					col++;
					mark[row][col] = 1;
	            }
	        }
			
	//	printf("%d %d\n",row,col);
	/*  for (i=1;i<=row;i++)
	    {
	        for (j=1;j<=col;j++)
	            printf("%d ", mark[i][j]);
	        printf("\n");
	    }
	    printf("\n");
	*/
			
		ans = 0;
		k = solve(row, col, mark);
	//	printf("ans = %d\n", ans);
			
		for(i=0;i<ans;i++)
		{
		//	printf("%d   %d %d %d\n",Ans[i],x[Ans[i]],y[Ans[i]],z[Ans[i]]);
			a[x[Ans[i]]][y[Ans[i]]] = z[Ans[i]];
		}
		if (k == 0)
			a[0][0] = -1;
		return a;
	}
}
