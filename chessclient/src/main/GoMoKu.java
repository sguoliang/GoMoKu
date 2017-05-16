package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class GoMoKu
{
	public static final int X = 15;
	public static final int Y = 15;
	public static final int GAP = 30;
	
	private int[][] pieces = new int[X][Y];
	private boolean isGameOver;
	private int color = 0;

	public GoMoKu(int color)
	{
		ini(color);
	}
	
	public GoMoKu()
	{
		ini();
	}
	
	public void ini()
	{
		isGameOver = false;
		for (int i = 0; i < X; i++)
		{
			for (int j = 0; j < Y; j++)
			{
				pieces[i][j] = -1;
			}
		}
	}
	
	
	
	
	public void ini(int color)
	{
		this.color = color;
		ini();
	}
	
	public void drawMe(Graphics2D g,int startX,int startY)
	{   
		g.setColor(Color.BLACK);
		drawChessBoard(g,startX,startY);
		drawPieces(g,startX,startY);
		
	}
	
	
	//画棋盘 
	private void drawChessBoard(Graphics2D g,int startX,int startY)
	{
		for (int i = 0; i <= (X-1); i++)
		{
			g.drawLine(startX, startY + GAP * i, startX+(X-1)*GAP, startY + GAP * i);
			g.drawLine(startX + GAP * i, startY, startX + GAP * i, startY+(X-1)*GAP);
		}
		
		g.setStroke(new BasicStroke(3));
		g.drawLine(startX, startY, startX+(X-1)*GAP, startY);
		g.drawLine(startX, startY + GAP * (X-1), startX+(X-1)*GAP, startY + GAP * (X-1));
		g.drawLine(startX, startY, startX, startY + GAP * (X-1));
		g.drawLine(startX+(X-1)*GAP, startY , startX+(X-1)*GAP, startY + GAP * (X-1));
		//g.fill3DRect(startX+3*GAP-3, startY+3*GAP-2, 6, 6, false);
		//g.fill3DRect(startX+3*GAP-3, startY+11*GAP-2, 6, 6, false);
		//g.fill3DRect(startX+11*GAP-3, startY+3*GAP-2, 6, 6, false);
		//g.fill3DRect(startX+11*GAP-3, startY+11*GAP-2, 6, 6, false);
		//g.fill3DRect(startX+7*GAP-3, startY+7*GAP-2, 6, 6, false);
		g.fill3DRect(startX+(X/2/2)*GAP-3, startY+(X/2/2)*GAP-2, 6, 6, false);
		g.fill3DRect(startX+(X/2/2)*GAP-3, startY+(X-X/4)*GAP-2, 6, 6, false);
		g.fill3DRect(startX+(X-X/4)*GAP-3, startY+(X/2/2)*GAP-2, 6, 6, false);
		g.fill3DRect(startX+(X-X/4)*GAP-3, startY+(X-X/4)*GAP-2, 6, 6, false);
		g.fill3DRect(startX+X/2*GAP-3, startY+X/2*GAP-2, 6, 6, false);
	}
	
	//画棋子
	private void drawPieces(Graphics2D g,int StartX,int StartY)
	{
		for (int i = 0; i < X; i++) 
		{
			for (int j = 0; j < Y; j++)
			{   
				RadialGradientPaint paint;
				int tempX = i * GAP + StartX;
				int tempY = j * GAP + StartY;
				if(pieces[i][j]==0)
				{
					//g.setColor(Color.BLACK);
					
					paint = new RadialGradientPaint(tempX,  tempY, 10, new float[]{0f, 1f}  , new Color[]{new Color(60,60,60),Color.BLACK});  
		               
				}else
				{   
					//g.setColor(Color.WHITE);
					paint = new RadialGradientPaint(tempX,  tempY, 70, new float[]{0f, 1f}  , new Color[]{Color.WHITE, Color.BLACK});  
				}
				
				g.setPaint(paint);  
	            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
	            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT); 
	            
				if (pieces[i][j] == color)
				{
					//int tempX = i * GAP + StartX;
					//int tempY = j * GAP + StartY;
					//g.fillOval(tempX - 15, tempY - 15, 30, 30);
					Ellipse2D e = new Ellipse2D.Float(tempX-GAP/2, tempY-GAP/2, GAP, GAP+1);  
			        g.fill(e);
				
				}
				if (pieces[i][j] == (1-color))
				{
					//int tempX = i * GAP + StartX;
					//int tempY = j * GAP + StartY;
					//g.fillOval(tempX - 15, tempY - 15, 30, 30);
					Ellipse2D e = new Ellipse2D.Float(tempX-GAP/2, tempY-GAP/2, GAP, GAP+1);  
			        g.fill(e);
				}
			}
		}
	}
	
	
	//判断游戏是否结束，并设置isGameOver标志
	public int judge(int x, int y)
	{  
		int c1 = countNumber(x,y,1,0);//横向判断
		int c2 = countNumber(x,y,0,1);//纵向判断
		int c3 = countNumber(x,y,1,1);//右下，左上方向
		int c4 = countNumber(x,y,-1,1);//左下 右上方向
		if((c1>=5)||(c2>=5)||(c3>=5)||(c4>=5))
		{
			return pieces[x][y];
		}else
		{
			boolean flag = false;
			for (int i = 0; i < X; i++)
			{
				for (int j = 0; j < Y; j++)
				{
					if(pieces[i][j]==-1)
					{
						flag = true;
						break;
					}
				}
				
				if(flag)
					break;
			}
			if(flag)
				return -1;
			else
				return 2;
		}
	}
	
	private int countNumber(int x,int y,int dx,int dy)
	{  
		int c = pieces[x][y];
		int count = 1;
		int tx = dx;
		int ty = dy;
		while(((x+tx)>=0)&&((x+tx)<X)&&((y+ty)>=0)&&((y+ty)<Y)&&(c == pieces[x+tx][y+ty]))
		{
			count++;
			if(ty!=0)
				ty++;
			if(tx!=0)
			{
				if(tx>0)
					tx++;
				else
					tx--;
			}
			
		}
		tx = dx;
		ty = dy;
		while(((x-tx)>=0)&&((x-tx)<X)&&((y-ty)>=0)&&((y-ty)<Y)&&(c == pieces[x-tx][y-ty]))
		{
			count++;
			if(ty!=0)
				ty++;
			if(tx!=0)
			{
				if(tx>0)
					tx++;
				else
					tx--;
			}
			
		}
		return count;
	}
	
	public boolean checkLocation(int x,int y)
	{
		if((x<0)||(x>14))
		{
			return false;
		}
		if((y<0)||(y>14))
		{
			return false;
		}
		int value = pieces[x][y];
		if(value<0)
		{
			return true;
		}
		return false;
	}
	
	
	public boolean setLocation(int x,int y,int color)
	{
		// 需要进行位置判断
		if(checkLocation(x, y))
		{
			pieces[x][y] = color;
			return true;
		}
		return false;
	}
	
	public int getLocation(int x,int y)
	{
		return pieces[x][y];
	}
	public void setColor(int color)
	{
		this.color = color;
	}
	public int getColor()
	{
		return color;
	}
	public boolean getGameState()
	{
		return isGameOver;
	}
}
