package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ServerMain extends JFrame implements WindowListener
{

	private static final long serialVersionUID = 1L;
	JButton start;
	JButton stop;
	ChessServer server;
    Thread t=null;
	public ServerMain()
	{
		start = new JButton("开启服务");
		stop = new JButton("关闭服务");
		setLayout(new BorderLayout());
		add(start, BorderLayout.NORTH);
		add(stop, BorderLayout.SOUTH);
		setSize(50, 100);
		setLocation(500, 500);
		setResizable(false);
		this.setVisible(true);
        addWindowListener(this);
		start.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				startService();
			}
		});

		stop.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.out.println("服务器已关闭！");
				System.exit(0);
			}
		});
	}

	public static void main(String[] args)
	{
		new ServerMain();
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
		
	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		//System.out.println("关闭");
		System.out.println("服务器已关闭");
		closeService();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{	
	}

	@Override
	public void windowIconified(WindowEvent e)
	{	
	}

	@Override
	public void windowOpened(WindowEvent e)
	{	
	}
	
	private void closeService()
	{
		if ((server != null)&&(t!=null))
		{
			//t.stop();
			t = null;
		}
	}
	private void startService()
	{
		
		if(t==null)
		{
			t = new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{  
					if(server==null)
					{   
						server = ChessServer.getInstance();
						try
						{
							server.start();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					
					}
					
				}
			});
			t.start();
		}else
		{
			t.start();
		}
		
	}
}
