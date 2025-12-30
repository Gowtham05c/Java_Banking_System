import java.util.*;

public class BankingApp {

    static Scanner sc = new Scanner(System.in);
    static BankDAO dao = new BankDAO();

    public static void main(String[] args) {

        while(true){
            System.out.println("\n===== BANK SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch(ch){
                case 1: create(); break;
                case 2: login(); break;
                case 3: System.exit(0);
                default: System.out.println("Invalid Choice!");
            }
        }
    }

    static void create(){
        System.out.print("Enter Name: ");
        String name = sc.next();

        System.out.print("Create PIN: ");
        String pin = sc.next();

        dao.createAccount(name,pin);
    }

    static void login(){
        System.out.print("Enter Account Number: ");
        String acc = sc.next();

        System.out.print("Enter PIN: ");
        String pin = sc.next();

        var user = dao.login(acc,pin);

        if(user == null){
            System.out.println("Invalid Credentials!");
            return;
        }

        dashboard(acc,user.getName());
    }

    static void dashboard(String acc,String name){
        while(true){
            System.out.println("\n===== Welcome "+name+" =====");
            System.out.println("1. Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Transactions");
            System.out.println("6. Logout");

            int ch = sc.nextInt();

            switch(ch){
                case 1:
                    System.out.println("Balance : ₹"+dao.getBalance(acc));
                    break;

                case 2:
                    System.out.print("Amount: ");
                    dao.deposit(acc, sc.nextDouble());
                    break;

                case 3:
                    System.out.print("Amount: ");
                    dao.withdraw(acc, sc.nextDouble());
                    break;

                case 4:
                    System.out.print("Enter Receiver Acc No: ");
                    String r = sc.next();
                    System.out.print("Amount: ");
                    dao.transfer(acc,r,sc.nextDouble());
                    break;

                case 5:
                    dao.viewTransactions(acc);
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
