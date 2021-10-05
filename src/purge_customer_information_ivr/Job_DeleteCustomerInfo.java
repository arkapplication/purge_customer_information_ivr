package purge_customer_information_ivr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 *
 * @author h.chaiyaporn
 */
public class Job_DeleteCustomerInfo {

    /**
     * @param args the command line arguments
     */
    String connection;
    int rowDelete;
    Connection conn;

    Job_DeleteCustomerInfo() throws Exception {
        readConfig();
        purgeData();
    }

    private void readConfig() throws Exception {
        Properties config = new Properties();
        config.load(getClass().getResourceAsStream("config.properties"));
        connection = config.getProperty("connection");
        rowDelete = Integer.parseInt(config.getProperty("rowDelete"));
        System.out.println("connection " + connection);
        System.out.println("rowDelete " + rowDelete);
    }

    private void purgeData() throws Exception {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          //  connection = "jdbc:sqlserver://10.2.18.13:1455;databaseName=IVR_SIT;password=devsit1234!;user=dev_sit";
            conn = DriverManager.getConnection(connection);
            PreparedStatement ps = conn.prepareStatement("job_deleteCustomerInfo @rowCount=" + rowDelete);
            ps.execute();
            boolean hasData = checkRowCount(conn);
            while (hasData) {
                ps.execute();
                hasData = checkRowCount(conn);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    private boolean checkRowCount(Connection conn) throws Exception {
        String command = "select count(0) as row_count from customer_information_ivr where last_update <  Dateadd(m,-6, GETDATE()) and admin_edit=0";
        PreparedStatement ps = conn.prepareStatement(command);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("data delete " + rs.getInt("row_count"));
            if (rs.getInt("row_count") > 0) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            Job_DeleteCustomerInfo job = new Job_DeleteCustomerInfo();
            System.out.println("Successfully");
            System.exit(0);
            //JOptionPane.showMessageDialog(new JFrame(),"Successfully");
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(new JFrame(),e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error : " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        } 
    }

}
