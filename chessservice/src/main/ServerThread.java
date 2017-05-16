package main;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import utils.JsonUtil;

public class ServerThread implements Runnable
{   
	
	private ChessServer server;
	
	private Socket socket;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String name = null;
	private boolean isAI = false;
	private boolean flag;
	private GoAgent agent;
	private ServerThread oppo;
	private int state;//��ʾ�û���ǰ��״̬          0  ��ʾ����      1  �Ѿ�ƥ�����          2  ���ڶ�ս��
	
	public ServerThread(ChessServer server,Socket socket)throws IOException
	{
		this.server = server;
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
	    flag = true;
	    state = 0;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	public PrintWriter getOut()
	{
		return out;
	}

	public boolean isClosed()
	{
		return socket.isClosed();
	}
	
	public void setOppo(ServerThread thread)
	{
		this.oppo = thread;
	}
	public ServerThread getOppo()
	{
		return oppo;
	}
	
	
	
	
	//���������߼�
	private void setName(String name)
	{    
		if(this.name==null)
		{
			boolean flag = server.addClientName(name);
			if(flag)
			{
				//�������óɹ����سɹ���Ϣ
				this.name = name;
				out.println(JsonUtil.setNameJson(true));
			}else
			{
				out.println(JsonUtil.setNameJson(false));
			}
			out.flush();
		}
		
	}
	
	
	//��������߼�
	private void findOppenet()
	{   
        if(oppo==null)
        {
        	ServerThread thread = server.findOppoents(this);
        	if(thread==null)
        	{
        		out.println(JsonUtil.findOppoJson(false, null));
        	}else
        	{
        		setOppo(thread);
        		setState(1);
        		out.println(JsonUtil.findOppoJson(true, thread.getName()));
        		
        		thread.setOppo(this);
        		thread.setState(1);
        		thread.getOut().println(JsonUtil.findOppoJson(true, name));
        		thread.getOut().flush();
        	}
        	
        }else
        {
        	out.println(JsonUtil.findOppoJson(true, oppo.getName()));
        }
        out.flush();
		
	}
	
	//������Ϸ�߼�
	private void startGame()
	{
		if((state==1)&&(oppo.getState()==1))
		{	
			setState(2);
			out.println(JsonUtil.startGameJson(true, 0));
			out.flush();
			
			oppo.setState(2);
			oppo.getOut().println(JsonUtil.startGameJson(true, 1));
			oppo.getOut().flush();
		}
		
	}
	//����Ϸ�߼�
	private void playGame(Map<String,Object> map)
	{   
		boolean flag = (boolean)map.get("flag");
		int x = (int)(double)(map.get("x"));
		int y = (int)(double)(map.get("y"));
		if(isAI)
		{   
			//�˻���ս
			//System.out.println("�˻���ս");
			if(!flag)
			{
				agent.setPiece(x, y);
				Point p = agent.doChess();
				x = (int)p.getX();
				y = (int)p.getY();
				int f = agent.getFlag();
				if(f>=0)
				{
					out.println(JsonUtil.playGameJson(true, x, y));
				}
				else
				{
					out.println(JsonUtil.playGameJson(false, x, y));
				}
				out.flush();
				if(f>=0)
				{
					isAI = false;
					setState(0);
				}

			}else
			{
				isAI = false;
				setState(0);
			}
			
			
		}
		else if(oppo!=null)
		{   
			//���˶�ս
			//System.out.println("���˶�ս");
			if(flag)
		    {
		    	setState(1);
		    	if(oppo!=null)
		    	{
		    		oppo.setState(1);
		    	}
		    }
			if(oppo!=null)
			{
				oppo.getOut().println(JsonUtil.playGameJson(flag, x, y));
				oppo.getOut().flush();
			}
	
		}
	}
	//�ͻ���Ͷ���߼�
	private void surrender()
	{ 
		if(!isAI)
		{
			oppo.getOut().println(JsonUtil.surrenderJson());
			oppo.getOut().flush();
			oppo.setState(1);
			setState(1);
		}else
		{
			setState(0);
			isAI = false;
		}
		

	}
	//�˳��߼�
	private void exit()
	{   
		if(oppo!=null)
		{
			oppo.getOut().println(JsonUtil.exitJson());
			oppo.getOut().flush();
			oppo.setOppo(null);
			oppo.setState(0);
		}
		close();
		server.removeClient(name,this);
		
	}
	
	
	private void aiMode()
	{
		if(!(state==2))
		{
			isAI = true;
			if(state==1)
			{
				if(oppo!=null)
				{
					oppo.setOppo(null);
					oppo.setState(0);
					oppo.getOut().println(JsonUtil.separteJson());
					oppo.getOut().flush();
					setOppo(null);
				}
			}
			
			if(agent==null)
			{
				agent = new GreedyAgent();
			}else
			{
				// ��ʼ��agent
				agent.ini();
			}
			setState(2);
			out.println(JsonUtil.aiModeJson(true));
			out.flush();
			
			out.println(JsonUtil.messageJson("AI","�������� !"));
			out.flush();
		}
	}
	
	private void sendMessage(Map<String, Object> map)
	{
		String data = map.get("data").toString();
		String name = map.get("name").toString();
		if(oppo!=null)
		{
			oppo.getOut().println(JsonUtil.messageJson(name, data));
			oppo.getOut().flush();
		}
		
	}
	
	@Override
	public void run()
	{
		try
		{    
		    System.out.println("�ͻ������ӣ��߳�������");
			String content = null;
			while((flag)&&((content = in.readLine())!=null))
			{
				//System.out.println("�յ��ͻ������� "+content);
				Map<String,Object> map = JsonUtil.json2map(content);
				String action = map.get("action").toString();
				if(JsonUtil.SETNAME.equals(action))
				{    
					//System.out.println("�յ��ͻ���������������");
					setName((String)map.get("data"));
				}else if(JsonUtil.FINDOPPO.equals(action))
				{
					//System.out.println("�յ��ͻ���ƥ���������");
					findOppenet();
					
				}else if(JsonUtil.STARTGAME.equals(action))
				{
					//System.out.println("�յ��ͻ��˿�ʼ��Ϸ����");
					startGame();
				}else if(JsonUtil.PLAYGAME.equals(action))
				{
					playGame(map);
				}else if(JsonUtil.SURRENDER.equals(action))
				{   
					//System.out.println("�յ��ͻ���Ͷ������");
					surrender();
				}else if(JsonUtil.EXIT.equals(action))
				{   
					//System.out.println("�յ��ͻ����˳�Ӧ������");
					exit();
					
				}else if(JsonUtil.AIMODE.equals(action))
				{  
					//System.out.println("�յ��ͻ����˻���ս����");
					aiMode();
				}else if(JsonUtil.MESSAGE.equals(action))
				{
					sendMessage(map);
				}
			}
			System.out.println("�ͻ����˳����߳̽�����");
		} catch (Exception e)
		{
			e.printStackTrace();
			close();
			server.removeClient(name,this);
		}
	}

	
	
	
	public void close()
	{
		flag = false;
		try
		{   
			oppo = null;
			state = 0;
			if(in!=null)
			{
				in.close();
				
			}
			if(out!=null)
			{
				out.close();
			}
			if(socket!=null)
			{
				if(!socket.isClosed())
				{
					socket.close();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
}
