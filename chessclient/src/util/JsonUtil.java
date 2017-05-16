package util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JsonUtil
{ 
	
	//json ͨ�÷���
	
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
	
	
	public static final String SETNAME = "setName";
	public static final String FINDOPPO = "findOpponent";
	public static final String PLAYGAME = "playGame";
	public static final String SURRENDER = "surrender";
	public static final String EXIT = "exit";
	public static final String AIMODE = "AIMode";
	public static final String STARTGAME = "startGame";
	public static final String DISPATCHER = "Dispatcher";
	public static final String SEPARATEOPPO = "separate";
	public static final String MESSAGE = "message";
	//�ͻ���ר�÷���
	public static String setNameJson(String name)
	{   
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", SETNAME);
		map.put("data", name);
		return map2json(map);
	}
	public static String findOpponentJson()
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", FINDOPPO);
		return map2json(map);
	}
	public static String playGameJson(boolean flag,int x,int y)
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", PLAYGAME);
		map.put("flag", flag);
		map.put("x", x);
		map.put("y", y);
		return map2json(map);
	}
	public static String surrenderJson()
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", SURRENDER);
		return map2json(map);
	}
	public static String exitJson()
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", EXIT);
		return map2json(map);
	}
	public static String AIModeJson()
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", AIMODE);
		return map2json(map);
	}
	public static String startGameJson()
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", STARTGAME);
		return map2json(map);
	}
	public static String messagJson(String name,String data)
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("action", MESSAGE);
		map.put("name", name);
		map.put("data", data);
		return map2json(map);
	}
}
