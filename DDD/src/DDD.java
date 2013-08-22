import java.awt.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;  
import javax.swing.*;
import java.util.*;


public class DDD extends JFrame implements Runnable
{
    public DDD()
    {
    	setSize(400, 300);
    	setLocation(1000, 27);
        setLayout(new GridLayout(9, 1));
        //setLayout(new FlowLayout());
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	int val = e.getKeyCode();
            	switch (val) {
            		case 37:
            			x = x-2; robot.mouseMove(x, y); break;
            		case 38:
            			y = y-2; robot.mouseMove(x, y); break;
            		case 39:
            			x = x+2; robot.mouseMove(x, y); break;
            		case 40:
            			y = y+2; robot.mouseMove(x, y); break;
            		case 10:
            			if (running == false) {
            				running = true;
            				stop = false;
            				thread = new Thread(frame);
            				thread.start();
            			} else {
            				text.setText("is running");
            			}
            			break;
            		case 'q':
            		case 'Q':
            			stop = true;
            			text.setText("已停止");
            	}
            	if (val >= 37 && val <= 40) {
            		Color c = robot.getPixelColor(x,y);
            		text.setText("x="+x+", y="+y+", rgb="+c.toString().replaceAll("[a-z]+\\.", ""));
            	}
            }
        });
    }
    
    static DDD frame;
    private final int N = 15, M = 23;
    private Robot robot;
    private static JLabel text;
    private int x, y;
    private boolean stop = false, running = false;
    private Thread thread;
    
    int noColor = 0;
    int ans, ansX[] = new int[333], ansY[] = new int[333];
    
    // 0 ~ 9 为10种颜色， 10、11为可以点击的空格。转换到map[][]后，1～10是10种颜色，0为空格。
    private int RED[]   = {0,  0,  100,150,200,200,200,255,255,255,237,255};
    private int GREEN[] = {100,200,200,150,100,100,200,100,150,170,237,255};
    private int BLUE[]  = {250,0,  200,150,0,  200,100,100,0,  250,237,255};
    private int RED_DIV[]   = {4, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private int GREEN_DIV[] = {2, 1, 1, 1, 2, 2, 1, 2, 2, 2, 1, 1};
    private int BLUE_DIV[]  = {1, 4, 1, 1, 4, 1, 2, 2, 4, 1, 1, 1};
    
    public int getColorId(int red_val, int green_val, int blue_val)
    {
    	int abs_val[] = new int[12];
    	int i, ret = 0;
    	for (i=0;i<12;i++) {
    		int red_abs = Math.abs(red_val-RED[i]*10);
    		int green_abs = Math.abs(green_val-GREEN[i]*10);
    		int blue_abs = Math.abs(blue_val-BLUE[i]*10);
    		abs_val[i] = red_abs/RED_DIV[i] + green_abs/GREEN_DIV[i] + blue_abs/BLUE_DIV[i];
    		if (abs_val[i] < abs_val[ret]) ret = i;
    	}
    	return ret >= 10 ? noColor : ret+1;
    }
    
    public void random(int arr[], int nm) {
    	int i,j, k=nm;
    	while (k-- > 0) {
    		i = (int)(Math.random()*100)%nm;
    		j = (int)(Math.random()*100)%nm;
    		int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    	}
    }
    
    int Limit, next[]=new int[333], pre[]=new int[333], Lv[]=new int[333], Lx[]=new int[333], Ly[]=new int[333];
    int dfs(int leave, int map[][])
    {
    	if (leave == 0) return 1;
		int i, j, k, ui, uj;
    	int Fx[]=new int[4], Fy[]=new int[4], Fcolor[]=new int[4], Fsum[]=new int[4];
    	
    	for (ui=next[0];ui<=Limit;ui=next[ui])
    		for (uj=next[ui];uj<=Limit;uj=next[uj]) if (Lv[ui] == Lv[uj])
    		{
    			i=Lx[ui]; j=Ly[uj];
    			if (map[i][j] != noColor) continue;
    			
    			for (k=0;k<4;k++) { Fcolor[k] = -1; Fsum[k] = 1; }
    			int ii, jj, qx[]={-1, 1, 0, 0}, qy[]={0, 0, -1, 1};
    			for (k=0;k<4;k++) {
    				ii = i+qx[k]; jj = j+qy[k];
   					while (ii >= 0 && ii < N && jj >= 0 && jj < M && map[ii][jj] == noColor) {
   						ii += qx[k]; jj += qy[k];
   					}
   					if (ii >= 0 && ii < N && jj >= 0 && jj < M) {
						Fx[k] = ii; Fy[k] = jj; Fcolor[k] = map[ii][jj];
    				}
    			}
    			for (ii=0;ii<4;ii++)
   					for (jj=0;jj<4;jj++) if (ii != jj) {
   						if (Fcolor[jj] != -1 && Fcolor[jj] == Fcolor[ii]) Fsum[ii]++;
   					}
    			
    			int flag = 0;
    			int val[]=new int[4];
   				for (ii=0;ii<4;ii++) if (Fsum[ii]%2 == 0) {
    				flag = 1;
    				leave--;
    				val[ii] = map[Fx[ii]][Fy[ii]];
    				map[Fx[ii]][Fy[ii]] = noColor;
    			}
   				
   				if (flag == 1) {
   					ansX[ans] = i;
   					ansY[ans++] = j;
   					next[pre[ui]] = next[ui];
   					pre[next[ui]] = pre[ui];
   					next[pre[uj]] = next[uj];
   					pre[next[uj]] = pre[uj];
   					if (dfs(leave, map) == 1) return 1;
   					pre[next[uj]] = uj;
   					next[pre[uj]] = uj;
   					pre[next[ui]] = ui;
   					next[pre[ui]] = ui;
   					ans--;
   	   				for (ii=0;ii<4;ii++) if (Fsum[ii]%2 == 0) {
   	    				leave++;
   	    				map[Fx[ii]][Fy[ii]] = val[ii];
   	    			}
   				}
    		}
    	return 0;
    }
    
    int tryRun(int map[][], int LL)
    {
        int i,j,k,count = 0, total = 0;
        int row[]=new int[33], col[]=new int[33];
        for (i=0;i<N;i++) row[i] = i;
        for (i=0;i<M;i++) col[i] = i;
        
        for (i=0;i<N;i++)
        	for (j=0;j<M;j++) if (map[i][j] != 0) total++;
        ans = 0;
    	while (count < total-LL) {
    		if (stop) break;
    		/*
    		bi = robot.createScreenCapture(new Rectangle(x, y, x+800, y+800));
        	int rgb_tmp = bi.getRGB(26, 26*N);
        	if ((rgb_tmp&((1<<24)-1)) != (237<<16) + (237<<8) + 237) break;
        	
    		for (i=0;i<N;i++) {
    			for (j=0;j<M;j++) {
    				System.out.printf("%02d ", map[i][j]);
    			}
    			System.out.println();
    		}*/
    		
        	int Fx[]=new int[4], Fy[]=new int[4], Fcolor[]=new int[4], Fsum[]=new int[4];
            int flag = 0;
            
    		random(row, N);
            random(col, M);

	    	for (int ui=0;ui<N;ui++)
	    	{
	    		for (int uj=0;uj<M;uj++)
	    		{
	    			i = row[ui]; j = col[uj];
	    			if (map[i][j] != noColor) continue;
	    			
	    			for (k=0;k<4;k++) { Fcolor[k] = -1; Fsum[k] = 1; }
	    			int ii, jj, qx[]={-1, 1, 0, 0}, qy[]={0, 0, -1, 1};
	    			for (k=0;k<4;k++) {
	    				ii = i+qx[k]; jj = j+qy[k];
	   					while (ii >= 0 && ii < N && jj >= 0 && jj < M && map[ii][jj] == noColor) {
	   						ii += qx[k]; jj += qy[k];
	   					}
	   					if (ii >= 0 && ii < N && jj >= 0 && jj < M) {
    						Fx[k] = ii; Fy[k] = jj; Fcolor[k] = map[ii][jj];
	    				}
	    			}
	    			for (ii=0;ii<4;ii++)
	   					for (jj=0;jj<4;jj++) if (ii != jj) {
	   						if (Fcolor[jj] != -1 && Fcolor[jj] == Fcolor[ii]) Fsum[ii]++;
	   					}
	   				for (ii=0;ii<4;ii++) if (Fsum[ii]%2 == 0) {
    					ansX[ans] = i;
    					ansY[ans++] = j;
	    				flag = 1;
	    				break;
	    			}
	    			if (flag == 1) break;
	   			}
	   			if (flag == 1) break;
	   		}
	    	
	    	if (flag == 0) break;
	    	
	    	for (k=0;k<4;k++) if(Fsum[k]%2 == 0) {
	    		count++;
	    		//System.out.println("xx[N]="+Fx[k]+", yy[N]="+Fy[k]+" ,i="+i+", j="+j +",   map[i][j] = "+Fcolor[k]);
	   			for (int kk=0;kk<4;kk++) if (Fcolor[k] == Fcolor[kk]) {
	   				//System.out.println("xx[k]="+Fx[kk]+", yy[k]="+Fy[kk]);
	   				map[Fx[kk]][Fy[kk]] = noColor;
	    		}
	    	}
    	}
    	Limit = 0;
    	next[0] = 1;
    	pre[1] = 0;
    	for (i=1;i<=10;i++)
    		for (j=0;j<N;j++)
    			for (k=0;k<M;k++) if (i == map[j][k]) {
    				Limit++;
    				next[Limit] = Limit+1;
    				pre[Limit+1] = Limit;
    				Lv[Limit] = i;
    				Lx[Limit] = j;
    				Ly[Limit] = k;
    			}
    	return dfs(Limit, map);
    }
    
    public void run()
    {
    	int i,j=0,k;
    	int map[][]=new int[133][133], tmpmap[][]=new int[N][M];
    	int dx,dy;
    	int px[]={0,1,2,3,4,0,1,2,3,4};
    	int py[]={0,0,0,0,0,1,1,1,1,1};
    	
    	BufferedImage bi = null;
		bi = robot.createScreenCapture(new Rectangle(x, y, x+800, y+800));	
        for (i=0;i<N;i++)
        	for (j=0;j<M;j++) {
        		int red_tmp=0, green_tmp=0, blue_tmp=0;
        		for (k=0;k<10;k++) {
	        		dy = i*26+px[k]; dx = j*26+py[k];
	        		int rgb = bi.getRGB(dx, dy);
	                red_tmp   += (rgb & 16711680) >> 16;
	                green_tmp += (rgb & 65280) >> 8;
	                blue_tmp  += (rgb & 255);
        		}
        		map[i][j] = getColorId(red_tmp, green_tmp, blue_tmp);
        	}
        
        int tim = 0, flag=0;
        while (tim++ < 1000) {
        	if (stop) break;
        	for (i=0;i<N;i++) for (j=0;j<M;j++) tmpmap[i][j] = map[i][j];
        	
        	long t1 = new Date().getTime();
        	if (tryRun(tmpmap, 20) == 1) {
        		flag = 1; break;
        	}
        	long t2 = new Date().getTime();
        	//System.out.println("TIM = "+tim+", TTT = "+(t2-t1));
        }
        
        if (flag == 0) {
        	for (i=0;i<N;i++) for (j=0;j<M;j++) tmpmap[i][j] = map[i][j];
        	tryRun(tmpmap, 0);
        }
        
        k = 200;
        for (i=0;i<N;i++) for (j=0;j<M;j++) if (tmpmap[i][j] != 0) k--;
        text.setText("随机次数："+tim+", 得分为："+k);

	    
        for (i=0;i<ans;i++) {
        	if (stop) break;
    		
        	Color color = robot.getPixelColor(x+26, y+26*N);
        	if (color.getRed() != 237 || color.getGreen() != 237 || color.getBlue() != 237) {
        		text.setText("已结束，或窗口被移动");
        		break;
        	}
        	
        	Point point= MouseInfo.getPointerInfo().getLocation();
        	
		    robot.mouseMove(x+ansY[i]*26, y+ansX[i]*26);
		    robot.mousePress(InputEvent.BUTTON1_MASK);
		    robot.mouseRelease(InputEvent.BUTTON1_MASK);
		    
		    try { Thread.sleep(50); } catch (Exception e) { }
		    robot.mouseMove(point.x, point.y);
		    
		    //frame.show();
		    try { Thread.sleep(300); } catch (Exception e) { }
        }
        running = false;
    }
    
    public static void main(String[] args) throws Exception
    {
    	frame = new DDD();     	
       	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
    	frame.add(new JLabel("警告:请将重要窗口最小化"));
    	frame.add(new JLabel("使用方法：打开打豆豆页面，按上下左右移动鼠标，"));
    	frame.add(new JLabel("                使得鼠标位于第一格的中心，然后按回车"));
    	frame.add(new JLabel("                开始后不要移动窗口"));
    	frame.add(new JLabel("------------------------------------------------------------"));
    	frame.add(new JLabel("状态："));
    	
    	text = new JLabel("");
    	frame.add(text);
    	
    	frame.setAlwaysOnTop(true);
    	frame.robot = new Robot();
    	frame.x = 93; frame.y = 305;
    	frame.robot.mouseMove(1030, 30);
    }
}
