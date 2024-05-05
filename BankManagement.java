package BankingManagementSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class BankManagement {
    
    private static final int NULL = 0;
    private static Connection conn = BankDB.getConnection();

    public static boolean createAccount(String name, int passCode) {
        try {
            if (name.isEmpty() || passCode == NULL) {
                System.out.println("All Fields Required!");
                return false;
            }

            String sql = "INSERT INTO customer(cname, balance, pass_code) VALUES (?, 1000, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, passCode);

            if (ps.executeUpdate() == 1) {
                System.out.println(name + ", Account Created Successfully!");
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username Not Available!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginAccount(String name, int passCode) {
        try {
            if (name.isEmpty() || passCode == NULL) {
                System.out.println("All Fields Required!");
                return false;
            }

            String sql = "SELECT * FROM customer WHERE cname = ? AND pass_code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, passCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int senderAc = rs.getInt("ac_no");
                BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
                int ch = 5;

                while (true) {
                    System.out.println("Hallo, " + rs.getString("cname"));
                    System.out.println("1) Transfer Money");
                    System.out.println("2) View Balance");
                    System.out.println("5) LogOut");
                    System.out.print("Enter Choice: ");
                    ch = Integer.parseInt(sc.readLine());

                    if (ch == 1) {
                        System.out.print("Enter Receiver A/c No: ");
                        int receiverAc = Integer.parseInt(sc.readLine());
                        System.out.print("Enter Amount: ");
                        int amount = Integer.parseInt(sc.readLine());

                        if (transferMoney(senderAc, receiverAc, amount)) {
                            System.out.println("MSG: Money Sent Successfully!\n");
                        } else {
                            System.out.println("ERR: Failed!\n");
                        }
                    } else if (ch == 2) {
                        getBalance(senderAc);
                    } else if (ch == 5) {
                        break;
                    } else {
                        System.out.println("Err: Enter Valid input!\n");
                    }
                }
            } else {
                return false;
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username Not Available!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void getBalance(int acNo) {
        try {
            String sql = "SELECT * FROM customer WHERE ac_no = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, acNo);
            ResultSet rs = ps.executeQuery();

            System.out.println("-----------------------------------------------------------");
            System.out.printf("%12s %10s %10s\n", "Account No", "Name", "Balance");

            while (rs.next()) {
                System.out.printf("%12d %10s %10d.00\n", rs.getInt("ac_no"), rs.getString("cname"), rs.getInt("balance"));
            }

            System.out.println("-----------------------------------------------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean transferMoney(int senderAc, int receiverAc, int amount) {
        try {
            conn.setAutoCommit(false); // Disable auto-commit mode
    
            String selectSql = "SELECT balance FROM customer WHERE ac_no = ?";
            PreparedStatement selectPs = conn.prepareStatement(selectSql);
            selectPs.setInt(1, senderAc);
            ResultSet rs = selectPs.executeQuery();
    
            if (rs.next()) {
                int balance = rs.getInt("balance");
                if (balance < amount) {
                    System.out.println("Insufficient Balance!");
                    return false;
                }
    
                String updateSenderSql = "UPDATE customer SET balance = balance - ? WHERE ac_no = ?";
                PreparedStatement updateSenderPs = conn.prepareStatement(updateSenderSql);
                updateSenderPs.setInt(1, amount);
                updateSenderPs.setInt(2, senderAc);
                updateSenderPs.executeUpdate();
    
                String updateReceiverSql = "UPDATE customer SET balance = balance + ? WHERE ac_no = ?";
                PreparedStatement updateReceiverPs = conn.prepareStatement(updateReceiverSql);
                updateReceiverPs.setInt(1, amount);
                updateReceiverPs.setInt(2, receiverAc);
                updateReceiverPs.executeUpdate();
    
                conn.commit(); // Manually commit the transaction
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true); // Re-enable auto-commit mode
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}