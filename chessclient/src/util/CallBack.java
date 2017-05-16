package util;

public interface CallBack
{
	void onMessageReceived(String msg);
	void OnError(String msg);
	void doAction();
}
