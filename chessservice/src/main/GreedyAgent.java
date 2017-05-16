package main;

import java.awt.Point;

public class GreedyAgent implements GoAgent
{
    private int[][] pieces;
    private int[][] scores;
    
    
	private int x,y;
	public GreedyAgent()
	{
		pieces = new int[X][Y];
		scores = new int[X][Y];
		ini();
	}

	@Override
	public void ini()
	{
		for (int i = 0; i < X; i++)
		{
			for (int j = 0; j < Y; j++)
			{
				pieces[i][j] = -1;
				scores[i][j] = -1;
			}
		}
	}

	@Override
	public void setPiece(int x, int y)
	{
		//System.out.println("�ͻ������룺 x: "+x+" y: "+y);
		if((x>=0)&&(x<X)&&(y>=0)&&(y<Y))
		{
			if(pieces[x][y]==-1)
			{
				this.x = x;
				this.y = y;
				pieces[x][y] = 1;
				setScore();
			}
		}

	}

	@Override
	public Point doChess()
	{   
		// ��Ҫ����ƽ�ֵ���� ����̰���㷨   
		//Point p = null;
		//boolean flag = false;
		int max = 0;
        for (int i = 0; i < X; i++)
		{
			for (int j = 0; j < Y; j++)
			{
				/*if(pieces[i][j]==-1)
				{    
					pieces[i][j] = 0;
					this.x = i;
					this.y = j;
					p = new Point(x,y);
					flag = true;
					break;
				}*/
				if(scores[i][j]>max)
				{
					this.x = i;
					this.y = j;
					max = scores[i][j];
				}
			}
			/*if(flag)
				break;*/
		}
        pieces[x][y] = 0;
        
		return new Point(x,y);
	}

	@Override
	public int getFlag()
	{
		int c1 = countNumber(x,y,1,0);//�����ж�
		int c2 = countNumber(x,y,0,1);//�����ж�
		int c3 = countNumber(x,y,1,1);//���£����Ϸ���
		int c4 = countNumber(x,y,-1,1);//���� ���Ϸ���
		if((c1>=5)||(c2>=5)||(c3>=5)||(c4>=5))
		{
			return pieces[x][y];
		}else
		{   
			//�;��ж�
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
	
	
	
	
	private void setScore()
	{   
		int human = 1;
		int computer = 0;
		for (int i = 0; i < X; i++)
		{
			for (int j = 0; j < Y; j++)
			{
				int score_human = getState(i,j,human);
				int score_computer = getState(i,j,computer);
				scores[i][j] = score_human + score_computer;
			}
		}
	}
	
	
	
	private int getState(int x,int y, int flag)
	{   
		if(pieces[x][y]==-1)
		{   
			
			int lt = 1-flag;   //��������           -1 Ϊ����             1Ϊ����              0 Ϊ����    
			int rb = 1-flag;  // �������� 
			int num = 0; //���������
			int score = 0;
			
			
			
			//����ɨ��
			for (int i = 1; i < X -x; i++)
			{   
				/*//�жϱ߽�
				if((x+i)==(X-1))
				{
					rb = 1 - flag;
					//System.out.println("�߽�");
					//break;
					
				}*/
				if(pieces[x+i][y]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x+i][y]==-1)
				{   
					rb = -1;
					break;
				}
				if(pieces[x+i][y]==(1-flag))
				{
					rb = 1 - flag;
					break;
				}
				

			}
			
			for (int i = 1; i<=x; i++)
			{
				
				/*//�жϱ߽�
				if((x-i)==0)
				{
					lt = 1 - flag;
					//System.out.println("�߽�");
					//break;
				}*/
				
				if(pieces[x-i][y]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x-i][y]==-1)
				{   
					lt = -1;
					break;
				}
				if(pieces[x-i][y]==(1-flag))
				{
					lt = 1 - flag;
					break;
				}

			}
			
			score = score + getScore(num,lt,rb,flag);
			
			
			
			//����ɨ��
			lt = 1-flag;
			rb = 1-flag;
			num = 0;
			
			for (int i = 1; i < Y -y; i++)
			{
				
				/*//�жϱ߽�
				if((y+i)==(Y-1))
				{
					rb = 1 - flag;
					//System.out.println("�߽�");
					//break;
				}*/
				
				
				if(pieces[x][y+i]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x][y+i]==-1)
				{   
					rb = -1;
					break;
				}
				if(pieces[x][y+i]==(1-flag))
				{
					rb = 1 - flag;
					break;
				}
				

			}
			
			for (int i = 1; i<=y; i++)
			{
				
				/*//�жϱ߽�
				if((y-i)==0)
				{
					lt = 1 - flag;
					//System.out.println("�߽�");
					//break;
				}*/
				
				
	
				if(pieces[x][y-i]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x][y-i]==-1)
				{   
					lt = -1;
					break;
				}
				if(pieces[x][y-i]==(1-flag))
				{
					lt = 1 - flag;
					break;
				}
				

			}
			
			score = score + getScore(num,lt,rb,flag);
			
			
			
			
			//���ϵ�����   ����ɨ��
			lt = 1-flag;
			rb = 1-flag;
			num = 0;
			//�ϵķ���
			for (int i = 1; (i < X -x)&&(i<=y); i++)
			{   
				
				
				/*//�߽��ж�
				if(((x+i)==(X-1))||((y-i)==0))
				{
					rb = 1 - flag;
					//System.out.println("�߽�");
					//break;
				}*/
				
				if(pieces[x+i][y-i]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x+i][y-i]==-1)
				{   
					rb = -1;
					break;
				}
				if(pieces[x+i][y-i]==(1-flag))
				{
					rb = 1 - flag;
					break;
				}
				
				

			}
			//�µķ���
			for (int i = 1; (i<=x)&&(i<(Y-y)); i++)
			{
				
				/*//�߽��ж�
				if(((x-i)==0)||((y+i)==(Y-1)))
				{
					lt = 1 - flag;
					//System.out.println("�߽�");
					//break;
				}*/
				
				if(pieces[x-i][y+i]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x-i][y+i]==-1)
				{   
					lt = -1;
					break;
				}
				if(pieces[x-i][y+i]==(1-flag))
				{
					lt = 1 - flag;
					break;
				}
				

			}
			score = score + getScore(num,lt,rb,flag);
			
			
			
			
			//���µ����Ϸ���ɨ��
			lt = 1-flag;
			rb = 1-flag;
			num = 0;
			//�µķ���
			for (int i = 1; (i < (X -x))&&(i < (Y-y)); i++)
			{
				
				
				/*//�߽��ж�
				if(((x+i)==(X-1))||((y+i)==(Y-1)))
				{
					rb = 1 - flag;
					//System.out.println("�߽�");
					//break;
				}*/
				
				if(pieces[x+i][y+i]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x+i][y+i]==-1)
				{   
					rb = -1;
					break;
				}
				if(pieces[x+i][y+i]==(1-flag))
				{
					rb = 1 - flag;
					break;
				}
				

			}
			for (int i = 1; (i<=x)&&(i<=y); i++)
			{   
				
				
				/*//�߽��ж�
				if(((x-i)==0)||((y-i)==0))
				{
					lt = 1 - flag;
					//System.out.println("�߽�");
					//break;
				}*/
				
				
				if(pieces[x-i][y-i]==flag)
				{
					num++;
					continue;
				}
				if(pieces[x-i][y-i]==-1)
				{   
					lt = -1;
					break;
				}
				if(pieces[x-i][y-i]==(1-flag))
				{
					lt = 1 - flag;
					break;
				}

			}
			score = score + getScore(num,lt,rb,flag);
			
			return score;
		}else
		{
			//�Ѿ���������
			return -1;
		}
	}
	
	

	
	
	
	
	private int getScore(int num, int lt, int rb,int flag)
	{
		
		//��5
		if(num>=4)
		{
			//��5
			return 10000;
		}
		
		if(num==3)
		{
			if((lt==(1-flag))&&(rb==(1-flag)))
			{
				return 0;
			}else if((lt==-1)&&(rb==-1))
			{
				//�� 4
				return 3000; 
			}else
				return 900; //��4
		}
		
		
		if(num==2)
		{
			if((lt==(1-flag))&&(rb==(1-flag)))
			{
				return 0;
			}else if((lt==-1)&&(rb==-1))
			{
				//�� 3
				return 460; 
			}else
				return 30; //��3
		}
		if(num==1)
		{
			if((lt==(1-flag))&&(rb==(1-flag)))
			{
				return 0;
			}else if((lt==-1)&&(rb==-1))
			{
				//�� 2
				return 45; 
			}else
				return 5; //��2
		}
		if(num==0)
		{
			if((lt==(1-flag))&&(rb==(1-flag)))
			{
				return 0;
			}else if((lt==-1)&&(rb==-1))
			{
				//�� 1
				return 3; 
			}else
				return 1; //��1
		}
		return 0;
	}
	
}
