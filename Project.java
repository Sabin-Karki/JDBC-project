import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import  java.sql.SQLException;

 import javax.swing.ButtonGroup;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JRadioButton;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;



               public class Project {

               public class DatabaseConnection {
                   private static final String URL = "jdbc:postgresql://localhost:5432/nepathya";
                   private static final String USERNAME = "postgres";
                   private static final String PASSWORD = "123";

                   public static Connection getConnection() {
                       Connection connection = null;
                   try {             
                           Class.forName("org.postgresql.Driver");
                           connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                       } catch (ClassNotFoundException | SQLException e) {
                           e.printStackTrace();
                       }
                       return connection;
                   }
               }
               	
                   public static void main(String[] args) {                   
                               inputDisplay();
                           }                   
                   

                   
                   
                   
                   private static void inputDisplay() {
                       JFrame frame = new JFrame("Form");
                       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                       frame.setLayout(null);

                       // Label
                       JLabel label = new JLabel("Rollno: ");
                       frame.add(label);
                       label.setBounds(10, 0, 60, 20);

                       JLabel labelname = new JLabel("Name ");
                       frame.add(labelname);
                       labelname.setBounds(10, 38, 60, 20);

                       JLabel college = new JLabel("CollegeName ");
                       frame.add(college);
                       college.setBounds(10, 80, 80, 20);

                       // Text field
                       JTextField tf = new JTextField();
                       frame.add(tf);
                       tf.setBounds(10, 20, 60, 20);

                       JTextField tf1 = new JTextField();
                       frame.add(tf1);
                       tf1.setBounds(10, 60, 100, 20);

                       JTextField tf2 = new JTextField();
                       frame.add(tf2);
                       tf2.setBounds(10, 100, 150, 20);

                       
                       
                       
                       
                       // Radio buttons
                       JPanel p = new JPanel(new GridLayout(1, 2));
                       JRadioButton male = new JRadioButton("Male");
                       JRadioButton female = new JRadioButton("Female");
                       ButtonGroup group = new ButtonGroup();
                       group.add(female);
                       group.add(male);
                       p.add(female);
                       p.add(male);
                       frame.add(p);
                       p.setBounds(10, 150, 150, 30);
                       
                       

                       // Food
                       JLabel favouriteFood = new JLabel("Favourite Food");
                       frame.add(favouriteFood);
                       favouriteFood.setBounds(10, 200, 100, 20);

                       JPanel foodPanel = new JPanel(new GridLayout(2, 2));
                       JCheckBox pizza = new JCheckBox("Pizza");
                       JCheckBox momo = new JCheckBox("Momo");
                       JCheckBox pasta = new JCheckBox("Pasta");	
                       JCheckBox sushi = new JCheckBox("Sushi");
                       foodPanel.add(sushi);
                       foodPanel.add(pizza);
                       foodPanel.add(momo);
                       foodPanel.add(pasta);
                       frame.add(foodPanel);
                       foodPanel.setBounds(10, 220, 200, 30);

                       // Submit button
                       JButton submitButton = new JButton("Submit");
                       frame.add(submitButton);
                       submitButton.setBounds(10, 260, 80, 30);
                       
                       
                      

                       // Output area
                       JTextArea outputArea = new JTextArea(5, 30);
                       frame.add(outputArea);
                       outputArea.setBounds(220, 20, 800, 270);
                       
                       
                       
                       //RESET button
//                       JButton resetButton = new JButton("Reset");
//                       frame.add(resetButton);
//                       resetButton.setBounds(10, 300, 90, 27);
//                       
//                       resetButton.addActionListener(new ActionListener() {
//                           @Override
//                           public void actionPerformed(ActionEvent e) {
//                               resetCalculator();
//                           }
//
//					void resetCalculator() {
//							// TODO Auto-generated method stub
//							outputArea.setText() ="";
//						}
//                       });
//                       
                       
                    	   
                       
                     
                       
                 
                       
                      
                       

                       
                       
                       // Action listener for the submit button
                       
                       
                       submitButton.addActionListener(new ActionListener() {
                           public void actionPerformed(ActionEvent e) {
                              String rollNo = tf.getText();
                               String name = tf1.getText();
                               String college = tf2.getText();
                               
                               StringBuilder favouriteFoods = new StringBuilder();
                               String gender;
                               if (male.isSelected()) {
                                   gender = "Male";
                               } else if (female.isSelected()) {
                                   gender = "Female";
                               } else {
                                   gender = "Not specified";
                               }

                               if (pizza.isSelected()) {
                                   favouriteFoods.append("Pizza, ");
                               }
                               if (momo.isSelected()) {
                                   favouriteFoods.append("Momo, ");
                               }
                               if (pasta.isSelected()) {
                                   favouriteFoods.append("Pasta, ");
                               }
                               if (sushi.isSelected()) {
                                   favouriteFoods.append("Sushi, ");
                               }
                               
                              if (favouriteFoods.length() > 0) {
                                 favouriteFoods.setLength(favouriteFoods.length() - 2);
                               }
                              

                               try (Connection connection = DatabaseConnection.getConnection();
                            		    PreparedStatement selectStatement = connection.prepareStatement(
                                               "SELECT roll_no, name, gender,college, favourite_foods FROM record")) {
                                              ResultSet resultSet = selectStatement.executeQuery();
                                             
                                              while (resultSet.next()) {
                                                  String prevRollNo = resultSet.getString("roll_no");
                                                  String prevName = resultSet.getString("name");
                                                  String prevGender=resultSet.getString("gender");
                                                  String prevCollege = resultSet.getString("college");
                                                  String prevFavouriteFoods = resultSet.getString("favourite_foods");
                                                  String prevOutput = " Roll_no : " + prevRollNo + " Name : " + prevName + " Gender: " + prevGender + " College Name : " + prevCollege + " Favourite Foods : " + prevFavouriteFoods;
                                                  outputArea.setText(outputArea.getText() + prevOutput + "\n");
                                              }
                                              
                            		
                                      try( PreparedStatement statement = connection.prepareStatement(
                                           "INSERT INTO record (roll_no, name,gender, college, favourite_foods) VALUES (?, ?, ?, ?,?)")) {
                                      statement.setString(1, rollNo);
                                      statement.setString(2, name);
                                      statement.setString(3, gender);
                                      statement.setString(4, college);
                                      statement.setString(5, favouriteFoods.toString());
                                      statement.executeUpdate();
                                      String output = " Roll_no : " + rollNo + " Name : " + name + " Gender: " + gender + " College Name : " + college + " Favourite Foods : " + favouriteFoods.toString();
                                   
                                      
                                      outputArea.setText(outputArea.getText() +   output  + "\n" );
                                      
                                    
                                         }
                                  } catch (SQLException e1) {
                                      e1.printStackTrace();
                                      outputArea.setText(outputArea.getText() + "\nError saving data to the database.");
                                  }
                               
                             
                              }
                          });
                       frame.setSize(420, 425);
                       frame.setVisible(true);
                   }
               }
                   

