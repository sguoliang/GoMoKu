package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ChessServer
{   
	private List<ServerThread> serverThreads = Collections.synchronizedList(new ArrayList<ServerThread>());
	private List<String> names = Collections.synchronizedList(new ArrayList<>());
	//private Map<String,ServerThread> clientMap = Collections.synchronizedMap(new HashMap<String,ServerThread>());
	
	
	private ServerSocket ss;
	private boolean flag;
	public static ChessServer server;
	
	
	public static ChessServer getInstance()
	{
		if(server==null)
		{
			server = new ChessServer();
		}
		return server;
	}
	
	private ChessServer() 
	{
		flag = true;
	}
	
	
	
	public void start()throws IOException
	{   
		System.out.println("服务器已启动");
		flag = true;
		ss = new ServerSocket(8888);
		while(flag)
		{
			Socket s = ss.accept();
			//System.out.println("客户端有新的连接");
			ServerThread thread = new ServerThread(server, s);
			serverThreads.add(thread);
			new Thread(thread).start();
		}
	}
	
	
	
	
	
	
	
	public boolean addClientName(String name)
	{   
		if(names.contains(name))
		{
			return false;
		}else
		{
			names.add(name);
			return true;
		}
	}
	
	public void removeClient(String name,ServerThread thread)
	{  
		if(names.contains(name))
		{
			names.remove(name);
			serverThreads.remove(thread);
		}
		thread = null;
	}
	
	
	
	public ServerThread findOppoents(ServerThread thread)
	{   
		Iterator<ServerThread> it = serverThreads.iterator();
		ServerThread t = null;
		boolean flag = false;
		while(it.hasNext())
		{
			t = it.next();
			if(t!=thread)
			{
				if((t.getState()==0)&&(t.getOppo()==null)&&(t.getName()!=null))
				{
					flag = true;
					break;
				}
			}
		}
		if(flag)
		    return t;
		else
			return null;
	}
	
	
	
	//停止服务，并清除资源
	public void stop()
	{
		
		System.out.println("服务器已关闭！");
		this.flag = false;
		try
		{
			
			clear();//关闭其它线程，并清空队列
			if(ss!=null)
			{
				if(!ss.isClosed())
				{
					ss.close();
				}
			}
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	private void clear()
	{   
		/*Iterator<ServerThread> it = serverThreads.iterator();
		while(it.hasNext())
		{
			ServerThread st = it.next();
			st.close();
		}*/
		serverThreads.clear();
		names.clear();
		server = null;
	}
}
