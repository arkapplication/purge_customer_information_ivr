package purge_customer_information_ivr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author h.chaiyaporn
 */
public class Job_DeleteCustomerInfo {

    /**
     * @param args the command line arguments
     */
    
    String connection;
    
    Job_DeleteCustomerInfo()throws Exception{
        readConfig();
        purgeData();
    }
    
    private void readConfig() throws Exception {
        Properties config = new Properties();
        config.load(getClass().getResourceAsStream("config.properties"));
        connection = config.getProperty("connection");
    }
    
    private void purgeData()throws Exception{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //connection = "jdbc:sqlserver://10.2.18.13:1455;databaseName=IVR_SIT;password=devsit1234!;user=dev_sit";
        Connection  conn = DriverManager.getConnection(connection);
        PreparedStatement ps  = conn.prepareStatement("job_deleteCustomerInfo");
        ps.execute();
        
    }
   
    
    public static void main(String[] args) {
        try{
            Job_DeleteCustomerInfo job = new Job_DeleteCustomerInfo();
            System.out.println("Successfully");
            System.exit(0);
            JOptionPane.showMessageDialog(new JFrame(),"Successfully");
        }catch(Exception e){
            JOptionPane.showMessageDialog(new JFrame(),e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error : "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }finally{
          System.exit(0);
        }
    }
    
      

}
