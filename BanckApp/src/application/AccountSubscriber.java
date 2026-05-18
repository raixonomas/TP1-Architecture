package application;

public interface AccountSubscriber {
	void onEvent(String type, double amount, double balance);
}
