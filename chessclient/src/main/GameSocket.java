package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import global.Config;
import util.CallBack;

public class GameSocket implements Runnable 
{   
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
    private boolean flag;
	private CallBack callback;
    
    public GameSocket(CallBack callback)
	{   
    	this.callback = callback;
		flag = true;
		try
		{
			socket = new Socket(Config.IP_ADDRESS,Config.PORT_NUMBER);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			
		} catch (Exception e)
		{
			//e.printStackTrace();
			if(callback!=null)
				callback.OnError("服务器连接失败!");
		    close();
		} 
	}

	@Override
	public void run()
	{   
		try
		{
			String msg = null;
			if(callback!=null)
			{
				callback.doAction();
			}
			while(flag&&((msg = reader.readLine())!=null))
			{
				if(callback!=null)
					callback.onMessageReceived(msg);
			}
		} catch (Exception e)
		{   
			//e.printStackTrace();
			if(callback!=null)
				callback.OnError("服务器连接失败!");
			close();
		}
	}
	
	
	public void sendMSG(String msg)
	{   
		//System.out.println("客户端将要发送的信息："+msg);
		try
		{
			if(flag)
			{
				writer.println(msg);
				writer.flush();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			if(callback!=null)
			{
				callback.OnError("服务器通信异常！");
			}
			close();
		}

	}
	
	
	public void close()
	{
		flag = false;
		try
		{   
			if(reader!=null)
			{
				reader.close();
			}
			if(writer!=null)
			{
				writer.close();
			}
			if(socket!=null)
			{
				if(!socket.isClosed())
				{
					socket.close();
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
