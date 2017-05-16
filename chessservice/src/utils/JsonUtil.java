package utils;

import java.util.HashMap;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.xml.ws.Dispatch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil
{ 
	
	//json 通用方法
	
	public static String obj2json(Object object)
	{
		Gson gson = new Gson();
		String json = gson.toJson(object);
		return json;
	}
	
	public static Map<String,Object> json2map(String json)
	{   
		JsonObject root = new JsonParser().parse(json).getAsJsonObject();
	    Gson gson = new Gson();
		return gson.fromJson(root, new TypeToken<Map<String,Object>>(){}.getType());
	}
	public static String map2json(Map<String, Object> map)
	{
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	
	//定义请求头action  确保服务器与客户端一致
	public static final String SETNAME = "setName";
	public static final String FINDOPPO = "findOpponent";
	public static final String PLAYGAME = "playGame";
	public static final String SURRENDER = "surrender";
	public static final String EXIT = "exit";
	public static final String AIMODE = "AIMode";
	public static final String STARTGAME = "startGame";
	public static final String DISPATCHER = "Dispatcher";
	public static final String RESPONSE = "response";
	public static final String SEPARATEOPPO = "separate";
	public static final String MESSAGE = "message";
	
	
	//服务器端专用方法
	public static String setNameJson(boolean flag)
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", SETNAME);
		map.put("type", RESPONSE);
		map.put("flag", flag);
		return map2json(map);
	}
	
	public static String findOppoJson(boolean flag, String name)
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", FINDOPPO);
		map.put("type", RESPONSE);
		map.put("flag", flag);
		map.put("data",name);
		return map2json(map);
	}
	public static String startGameJson(boolean flag, int data)
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", STARTGAME);
		map.put("type", RESPONSE);
		map.put("flag", flag);
		map.put("data",data+"");
		System.out.println(map2json(map));
		return map2json(map);
	}
	
	public static String playGameJson(boolean flag, int x,int y)
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", PLAYGAME);
		map.put("type", DISPATCHER);
		map.put("flag", flag);
		map.put("x",x);
		map.put("y",y);
		return map2json(map);
	}
	
	public static String surrenderJson()
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", SURRENDER);
		map.put("type", DISPATCHER);
		//map.put("data", data);
		return map2json(map);
	}
	
	public static String aiModeJson(boolean flag)
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", AIMODE);
		map.put("type", RESPONSE);
		map.put("flag", flag);
		return map2json(map);
	}
	
	public static String exitJson()
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", EXIT);
		map.put("type", DISPATCHER);
		return map2json(map);
	}
	public static String separteJson()
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", SEPARATEOPPO);
		return map2json(map);
	}
	public static String messageJson(String name,String msg)
	{   
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("action", MESSAGE);
		map.put("type", DISPATCHER);
		map.put("name", name);
		map.put("data", msg);
		return map2json(map);
	}
}
