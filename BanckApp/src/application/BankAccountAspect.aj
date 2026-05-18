package application;

import java.util.ArrayList;
import java.util.List;

public aspect BankAccountAspect {

    private List<AccountSubscriber> subscribers = new ArrayList<>();

    public void addSubscriber(AccountSubscriber s) {
        subscribers.add(s);
    }

    // ---- deposit interception ----
    pointcut depositCall(BankAccount acc, double amount) :
        execution(* BankAccount.deposit(double)) &&
        args(amount) &&
        target(acc);

    after(BankAccount acc, double amount) : depositCall(acc, amount) {
        notifyAll("DEPOSIT", amount, acc.getBalance());
    }

    // ---- withdraw interception ----
    pointcut withdrawCall(BankAccount acc, double amount) :
        execution(* BankAccount.withdraw(double)) &&
        args(amount) &&
        target(acc);

    after(BankAccount acc, double amount) : withdrawCall(acc, amount) {
        notifyAll("WITHDRAW", amount, acc.getBalance());
    }

    // ---- notify UI ----
    private void notifyAll(String type, double amount, double balance) {
        for (AccountSubscriber s : subscribers) {
            s.onEvent(type, amount, balance);
        }
    }
}