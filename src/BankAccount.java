public class BankAccount {
    private String accountNumber;
    private String name;
    private String pin;
    private double balance;

    public BankAccount(String accountNumber, String name, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pin = pin;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }
}
