package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import util.CallBack;
import util.JsonUtil;

public class GameFrame extends JFrame implements MouseListener, WindowListener, ActionListener
{
	public static final int STARTX = 15;
	public static final int STARTY = 15;
	
	private GoMoKu gmk;
    private GameSocket socket;
    private GameCallBack callback;
    
	private BufferedImage image = null;
	
	private Menu start,setting;
	private MenuBar mb;
	private MenuItem game_2player,ai_mode,surrender,chat;
	
	//private boolean isBlack = false;
	
	//private boolean isError = false;
	private boolean haveOpponent = false;
	private boolean turn;
	private int x,y;
	private boolean canPlay;
	
	private String player_name_black;
	private String player_name_white;
	private String player_name;
	private String oppo_name;
	private boolean isAI = false;
	public void setBlackName(String name)
	{   
		if((name!=null)&&!("".equals(name.trim())))
		{
			player_name_black = name;
		}
		
	}
	public void setWhiteName(String name)
	{   
		if((name!=null)&&!("".equals(name.trim())))
		{
			player_name_white = name;
		}
	}
	public String getBlackName()
	{
		return player_name_black;
	}
	public String getWhiteName()
	{
		return player_name_white;
	}
	
	
	
	public GameFrame()
	{  
		player_name_black = "等待匹配";
		player_name_white = "尚未连接";
		
		canPlay = false;
		
		try
		{
			image = ImageIO.read(new File("back.jpg"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//初始化菜单
		mb = new MenuBar();
		start = new Menu("开始游戏");
		game_2player = new MenuItem("双人游戏");
		ai_mode = new MenuItem("人机对战");
		start.add(game_2player);
		start.add(ai_mode);
		surrender = new MenuItem("认输");
		setting = new Menu("设置");
		chat = new MenuItem("聊天");
		setting.add(surrender);
		setting.add(chat);
		mb.add(start);
		mb.add(setting);
		game_2player.addActionListener(this);
		ai_mode.addActionListener(this);
		surrender.addActionListener(this);
		chat.addActionListener(this);
		//主要对象
		gmk = new GoMoKu();
		callback = new GameCallBack();
		socket = new GameSocket(callback);
		new Thread(socket).start();
		
		
		
		//其他设置项
		setMenuBar(mb);
		setTitle("五子棋");
		setSize(750, 600);
		setResizable(false);
		this.setLocation(200, 200);
		addMouseListener(this);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
	}
	
    public static void main(String[] args)
	{
		new GameFrame();
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(image, 0, 0, this);
		g.setColor(Color.white);
		drawBlackName(g);
		drawWhiteName(g);
		
		BufferedImage image = new BufferedImage(450, 450,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		
		g2d.setColor(new Color(143, 110, 0));
		g2d.fillRect(0, 0, 450, 450);
		gmk.drawMe(g2d,STARTX,STARTY);
		//g.drawImage(image, 0, 0, this);
		if(canPlay)
		{
			int lx = x*GoMoKu.GAP + STARTX;
			int ly = y*GoMoKu.GAP + STARTY;
			
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(lx - GoMoKu.GAP/5, ly, lx+GoMoKu.GAP/5, ly);
			g2d.drawLine(lx, ly- GoMoKu.GAP/5, lx, ly+GoMoKu.GAP/5);
			//g2d.drawRect(lx, ly, GoMoKu.GAP, GoMoKu.GAP);
		}
		g.drawImage(image, 260, 120, this);
	}
	
	public void setOppoName(String name)
	{
		oppo_name = name;
	}
	
	//画玩家的名字  根据名字的字数 多少调节位置和字体
	private void drawBlackName(Graphics g)
	{   
		int font_style = Font.ITALIC;
		boolean flag = false;
		if(player_name_black.equals(player_name))
		{
			font_style = Font.BOLD;
			flag = true;
			
		}
		
		if(haveOpponent)
		{  
			g.setColor(new Color(246,119,12));
			g.setFont(new Font("宋体", Font.BOLD, 15));
			if(flag)
			{
				g.drawString("对手", 70, 155);
				g.drawString("您", 78, 390);
			}
			else
			{  
				g.drawString("对手", 70, 390);
				g.drawString("您", 78, 155);
			}
		}
		
		g.setColor(Color.white);
		int x = 40;
		int len = player_name_black.length();
		if(len<=5)
		{
			x = x+6;
			g.setFont(new Font("宋体", font_style, 15));
		}else if(len<=7)
		{   
			x = x -8;
			g.setFont(new Font("宋体", font_style, 15));
		}else if(len<=8)
		{
			x = x -10;
			g.setFont(new Font("宋体", font_style, 13));
		}else if(len>=9)
		{
			x = x -12;
			g.setFont(new Font("宋体", font_style, 12));
		}
		else
		{
			g.setFont(new Font("宋体", font_style, 15));
		}
		g.drawString(player_name_black, x,445);
	}
	private void drawWhiteName(Graphics g)
	{   
		int x = 40;
		int len = player_name_white.length();
		int font_style = Font.ITALIC;
		if(player_name_white.equals(player_name))
		{
			font_style = Font.BOLD;
		}
		if(len<=5)
		{
			x = x+6;
			g.setFont(new Font("宋体", font_style, 15));
		}else if(len<=7)
		{   
			x = x -8;
			g.setFont(new Font("宋体", font_style, 15));
		}else if(len<=8)
		{
			x = x -10;
			g.setFont(new Font("宋体", font_style, 13));
		}else if(len>=9)
		{
			x = x -12;
			g.setFont(new Font("宋体", font_style, 12));
		}
		else
		{
			g.setFont(new Font("宋体", font_style, 15));
		}
		g.setFont(new Font("宋体", font_style, 15));
		g.drawString(player_name_white, x,210);
	}
	

	
	@Override
	public void mouseClicked(MouseEvent e)
	{}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{   
		if (canPlay&&turn)
		{
			x = e.getX();
			y = e.getY();
			// x 275-725  y 135 - 585
			if (x >= 275 && x <= 725 && y >= 135 && y <= 585) 
			{
				x =  (int)Math.round((x - 275) * 1.0/ GoMoKu.GAP);
				y =  (int)Math.round((y - 135) *1.0/ GoMoKu.GAP);
				//System.out.println("x: "+x+" y:"+y);
				boolean flag = gmk.setLocation(x, y, gmk.getColor());
				if(flag)
				{    
					update(getGraphics());
					turn = false;
					int c = gmk.judge(x, y);
					if(c>=0)
					{
						canPlay = false;
						if(isAI)
							isAI = false;
						if(c==2)
						{
							tip("棋力相当，平局！*_*");
						}else
						    tip("恭喜你，获得胜利! ^_^ ");
						socket.sendMSG(JsonUtil.playGameJson(true, x, y));
					}else
					{
						//System.out.println("传送startGame："+JsonUtil.playGameJson(true, x, y));
						socket.sendMSG(JsonUtil.playGameJson(false, x, y));
					}
					
				}
			}
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		
	}

	@Override
	public void windowActivated(WindowEvent e)
	{}

	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub
		//System.out.println("windowClosed");
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		//System.out.println("window is closing!");
		socket.sendMSG(JsonUtil.exitJson());
		socket.close();
		System.exit(0);
		
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{}

	@Override
	public void windowIconified(WindowEvent e)
	{}

	@Override
	public void windowOpened(WindowEvent e)
	{}
	
	public void startGame()
	{
		gmk.ini();
		update(getGraphics());
		socket.sendMSG(JsonUtil.startGameJson());
	}
	@Override
	public void actionPerformed(ActionEvent event)
	{
		String cmd = event.getActionCommand();
		if("双人游戏".equals(cmd))
		{   
			//第一次玩游戏，需要设置名称
			if(!canPlay)
			{
				if(player_name==null)
				{
					sendPlayerName();
				}else if(haveOpponent)
				{
					startGame();
				}
				else if(!haveOpponent)
				{
					socket.sendMSG(JsonUtil.findOpponentJson());
				}
			}else
			{
				JOptionPane.showMessageDialog(this,"游戏进行中!");
			}
			
		}
		
		//人机对战设置
		if("人机对战".equals(cmd))
		{
			if(!canPlay)
			{
				isAI = true;
				if(player_name==null)
				{
					sendPlayerName();
				}else
				{   
					 haveOpponent = false;
					 socket.sendMSG(JsonUtil.AIModeJson());
					 //System.out.println("已经发送AI请求");
				}
				   
			}else
			{
				JOptionPane.showMessageDialog(this,"游戏进行中，请结束本次游戏或投降后再进行人机大战");
			}
			
		}
		
		//投降功能
		if("认输".equals(cmd))
		{
			//正在玩的时候
			if(canPlay)
			{
				int result = JOptionPane.showConfirmDialog(null, "确定认输?", "投降",JOptionPane.YES_NO_OPTION);
				if(result==0)
				{
					canPlay = false;
					socket.sendMSG(JsonUtil.surrenderJson());
					isAI = false;
				}
				
			}
		}
		if("聊天".equals(cmd))
		{
			
			if(isAI)
			{
				tip("机器人暂时不会聊天！");
			}else if(haveOpponent)
			{
				sendChatMSG();
			}
		}
	}
   
	
	private void sendChatMSG()
	{
		String msg = JOptionPane.showInputDialog("请输入聊天信息");
		if(msg!=null)
		{
			 socket.sendMSG(JsonUtil.messagJson(player_name, msg.trim()));
		}
	}
    
    
    private void sendPlayerName()
    {
    	String name = JOptionPane.showInputDialog("请输入玩家名");
    	
		if((name!=null)&&!("".equals(name.trim())))
		{
			player_name = name;
		}else
		{
			try
			{
				player_name = InetAddress.getLocalHost().getHostName(); 
			} catch (UnknownHostException e)
			{   
				player_name = "玩家"+new Random().nextInt(1000);
				e.printStackTrace();
			}
		}
		//传到服务器端
		socket.sendMSG(JsonUtil.setNameJson(player_name));
    }
    
    class GameCallBack implements CallBack
    {

		@Override
		public void onMessageReceived(String msg)
		{
			Map<String,Object> map = JsonUtil.json2map(msg);
			String action = (String)map.get("action");
			
			if(JsonUtil.SETNAME.equals(action))
			{
				//设置名返回的结果
				boolean flag = (boolean)map.get("flag");
				if(!flag)
				{
					tip("玩家名已经被占用，请更换玩家名。");
					sendPlayerName();
				}else if(isAI)
				{
					socket.sendMSG(JsonUtil.AIModeJson());
				}else if(!haveOpponent)
				{
					socket.sendMSG(JsonUtil.findOpponentJson());
				}
				
			}else if(JsonUtil.FINDOPPO.equals(action))
			{
				boolean flag = (boolean)map.get("flag");
				if(flag)
				{
					String name = (String)map.get("data");
					setOppoName(name);
					//System.out.println("oppo: "+oppo_name);
					haveOpponent = true;
					if(!canPlay)
					   startGame();
				}else
				{
					tip("当前无空闲玩家，请稍等！");
				}
			}else if(JsonUtil.STARTGAME.equals(action))
			{
				
				if(!canPlay)
				{
					//boolean flag = (boolean)map.get("flag");
					//System.out.println(map.get("data"));
					int c = Integer.valueOf(map.get("data").toString());
					gmk.ini(c);
					if(c==1)
					{
						//本玩家指白子  白子先下
						setWhiteName(player_name);
						setBlackName(oppo_name);
						turn = true;
						//isBlack = false;
					}else if(c==0)
					{   
						turn = false;
						setWhiteName(oppo_name);
						setBlackName(player_name);
					}
					//gmk.ini();
					update(getGraphics());
					//System.out.println("show: "+player_name_black+" "+player_name_white);
					//System.out.println("local: "+player_name+" "+ oppo_name);
					tip("游戏开始!");
					canPlay = true;
				}
				
			}else if(JsonUtil.PLAYGAME.equals(action))
			{   
				//System.out.println("x:"+map.get("x").toString());
				x = (int)(double)(map.get("x"));
				y = (int)(double)(map.get("y"));
				chess();
			}else if(JsonUtil.SURRENDER.equals(action))
			{
				String type = (String)map.get("type");
				if(JsonUtil.DISPATCHER.equals(type))
				{
					tip("对方投降，恭喜你获得胜利！^_^");
					canPlay = false;
				}
			}else if(JsonUtil.AIMODE.equals(action))
			{
				//人机对弈部分
				if(!canPlay)
				{
					boolean flag = (boolean)map.get("flag");
					if(flag)
					{   
						isAI = true;
						gmk.ini(1);//玩家白棋，先走
						setWhiteName(player_name);
						setBlackName("AI机器人");
						turn = true;
						update(getGraphics());
						canPlay = true;
					}
				}
				//System.out.println("已经收到AI回应");
				
			}else if(JsonUtil.EXIT.equals(action))
			{   
				if(canPlay)
				{
					tip("对方退出了比赛， 你获得了胜利！囧rz");
				}
				haveOpponent = false;
				canPlay = false;
			}else if(JsonUtil.SEPARATEOPPO.equals(action))
			{
				haveOpponent = false;
			}else if(JsonUtil.MESSAGE.equals(action))
			{   
				String data = map.get("data").toString();
				String name = map.get("name").toString();
				if("AI".equals(name))
				{
					tip(data);
				}else
				{
					Object[] options ={ "回复", "关闭" };  
					int m = JOptionPane.showOptionDialog(null, name+": "+data, "消息",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);  
				    if(m==0)
				    {
				    	sendChatMSG();
				    }
				}
				
			}
			
		}

		@Override
		public void OnError(String msg)
		{
			tip(msg);
			//isError = true;
		}

		@Override
		public void doAction()
		{
		}
    	
    }
    
    public void tip(String msg)
    {
    	JOptionPane.showMessageDialog(this,msg);
    }
    public void chess()
    {
    	gmk.setLocation(x, y, 1-gmk.getColor());
    	turn = true;
    	update(getGraphics());
    	int c = gmk.judge(x, y);
    	if(c>=0)
    	{
    		if(c==gmk.getColor())
    		{
    			tip("恭喜你，获得胜利! ^_^");
    		}else if(c==2)
    		{
    			tip("棋力相当，平局！*_*");
    		}
    		else
    		{
    			tip("很遗憾，你输了! (︶︹︺)");
    		}
    		if(isAI)
    			isAI = false;
    		canPlay = false;
    	}
    }
}
