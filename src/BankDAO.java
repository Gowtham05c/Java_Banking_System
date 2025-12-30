import java.sql.*;
import java.util.*;

public class BankDAO {

    Connection con = DBConnection.getConnection();

    public boolean createAccount(String name, String pin){
        try{
            String accNo = "ACC" + System.currentTimeMillis();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO accounts(account_number,name,pin,balance) VALUES(?,?,?,0)"
            );

            ps.setString(1, accNo);
            ps.setString(2, name);
            ps.setString(3, pin);

            if(ps.executeUpdate() > 0){
                System.out.println("Account Created Successfully!");
                System.out.println("Your Account Number : " + accNo);
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public BankAccount login(String accNo, String pin){
        try{
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number=? AND pin=?"
            );
            ps.setString(1, accNo);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new BankAccount(
                        rs.getString("account_number"),
                        rs.getString("name"),
                        rs.getString("pin"),
                        rs.getDouble("balance")
                );
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public double getBalance(String accNo){
        try{
            PreparedStatement ps = con.prepareStatement(
                    "SELECT balance FROM accounts WHERE account_number=?"
            );
            ps.setString(1, accNo);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getDouble(1);

        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    public void deposit(String accNo, double amount){
        try{
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_number=?"
            );
            ps.setDouble(1, amount);
            ps.setString(2, accNo);
            ps.executeUpdate();

            addTransaction(accNo,"DEPOSIT",amount);

            System.out.println("₹"+amount+" Deposited Successfully!");

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void withdraw(String accNo, double amount){
        try{
            double bal = getBalance(accNo);

            if(amount > bal){
                System.out.println("Insufficient Balance!");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number=?"
            );
            ps.setDouble(1, amount);
            ps.setString(2, accNo);
            ps.executeUpdate();

            addTransaction(accNo,"WITHDRAW",amount);

            System.out.println("₹"+amount+" Withdrawn Successfully!");

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void transfer(String fromAcc, String toAcc, double amount){
        try{
            double bal = getBalance(fromAcc);

            if(amount > bal){
                System.out.println("Insufficient Balance!");
                return;
            }

            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number=?"
            );
            ps1.setDouble(1, amount);
            ps1.setString(2, fromAcc);
            ps1.executeUpdate();


            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_number=?"
            );
            ps2.setDouble(1, amount);
            ps2.setString(2, toAcc);
            ps2.executeUpdate();

            addTransaction(fromAcc,"TRANSFER OUT",amount);
            addTransaction(toAcc,"TRANSFER IN",amount);

            con.commit();
            System.out.println("Transfer Successful!");

        }catch(Exception e){
            try { con.rollback(); } catch (Exception ex){}
            e.printStackTrace();
        }
    }


    public void addTransaction(String accNo,String type,double amount){
        try{
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO transactions(account_number,type,amount) VALUES(?,?,?)"
            );
            ps.setString(1, accNo);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void viewTransactions(String accNo){
        try{
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE account_number=? ORDER BY id DESC"
            );
            ps.setString(1, accNo);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Transaction History ---");
            while(rs.next()){
                System.out.println(
                        rs.getString("type")+"  ₹"+rs.getDouble("amount")+"  "+rs.getString("date")
                );
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
