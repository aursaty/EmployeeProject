import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;


public class driver extends JFrame {


	private JTable table;
	private static JPanel p;
	static JScrollPane scrollPane;
	private String[] columns = { "ID", "Name", "Phones"};
	private TableModelListener tableModelListener;

	private static List<Employee> mEmployees;
	private static boolean triangleShow;
	
    public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.

        Dimension d = getContentPane().getSize();
        if (triangleShow) {
        	Graphics2D g2 = (Graphics2D) g;
        	Line2D line1 = new Line2D.Float(0, 0, d.width/2, d.height);
        	g2.draw(line1);
        	Line2D line2 = new Line2D.Float(d.width, 0, d.width/2, d.height);
        	g2.draw(line2);
        	Line2D line3 = new Line2D.Float(0, 0, d.width, 0);
        	g2.draw(line3);
        }
    }
    
	public driver() {
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		table = new JTable(model);

		p = new JPanel();
		
		try {
			createSQL();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			mEmployees = fetchEmployees();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		showEmployees(table, mEmployees);

		table.setCellSelectionEnabled(true);

		table.setPreferredScrollableViewportSize(new Dimension(1000, 800));
		table.setFillsViewportHeight(true);

		scrollPane = new JScrollPane(table);
		scrollPane.setVisible(true);

		getContentPane().add(scrollPane, BorderLayout.PAGE_START);
	}
	
 	public void createSQL() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:myOffice.db");
		Statement stat = conn.createStatement();
		stat.executeUpdate("DROP TABLE IF EXISTS EMPLOYEE;");
        stat.executeUpdate("CREATE TABLE EMPLOYEE (EMPLOYEE_ID INTEGER PRIMARY KEY, NAME TEXT NOT NULL);");
		PreparedStatement prep = conn.prepareStatement("INSERT INTO EMPLOYEE VALUES (?, ?);");
		prep.setInt(1, 1);
		prep.setString(2, "Snake Python");
		prep.addBatch();
		prep.setInt(1, 2);
		prep.setString(2, "Pascal Blez");
		prep.addBatch();
		prep.setInt(1, 3);
		prep.setString(2, "Pythagor");
		prep.addBatch();
		prep.setInt(1, 4);
		prep.setString(2, "Lebedev");
		prep.addBatch();
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);
		

		stat.executeUpdate("DROP TABLE IF EXISTS PHONE;");
        stat.executeUpdate("CREATE TABLE PHONE (EMPLOYEE_ID INTEGER NOT NULL, NUMBER TEXT NOT NULL, FOREIGN KEY(EMPLOYEE_ID) REFERENCES EMPLOYEE(EMPLOYEE_ID));");
		prep = conn.prepareStatement("INSERT INTO PHONE VALUES (?, ?);");
		prep.setInt(1, 1);
		prep.setString(2, "+380999262142");
		prep.addBatch();
		prep.setInt(1, 1);
		prep.setString(2, "+141241290");
		prep.addBatch();
		prep.setInt(1, 1);
		prep.setString(2, "+100500");
		prep.addBatch();
		prep.setInt(1, 1);
		prep.setString(2, "+380441262100");
		prep.addBatch();
		prep.setInt(1, 2);
		prep.setString(2, "+1");
		prep.addBatch();
		prep.setInt(1, 4);
		prep.setString(2, "+380523573525");
		prep.addBatch();
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);
		conn.close();
	}

	private List<Employee> fetchEmployees() throws ClassNotFoundException, SQLException{
		ArrayList<Employee> listEmployees = new ArrayList<Employee>();
		 
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:myOffice.db");

		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("SELECT * FROM EMPLOYEE");
		while (rs.next()) {
			int employeeId = (Integer) rs.getObject(1);
			String employeeName = (String) rs.getObject(2);
			Employee e = new Employee(employeeId, employeeName);
			
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM PHONE WHERE EMPLOYEE_ID == (?);");
			prep.setInt(1, e.getEmployeeId());
			
			
			ResultSet phones = prep.executeQuery();

			while (phones.next()) {
				e.setPhone((String) phones.getObject(2));
			}
			listEmployees.add(e);
		}
		return listEmployees;
	}
	
	private static String fetchPhonesForEmployee(String name) throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:myOffice.db");

		PreparedStatement prep = conn.prepareStatement("SELECT * FROM PHONE P JOIN EMPLOYEE E ON E.EMPLOYEE_ID == P.EMPLOYEE_ID WHERE NAME == (?);");
		prep.setString(1, name);
			
			
		ResultSet phones = prep.executeQuery();
		String res = "";
		while (phones.next()) {
			res = res + ((String) phones.getObject(2)) + "\n";
		}

		return res;
	}
	
	public void showEmployees(JTable table, List<Employee> listEmployees) {
		int i = 0;
		for (Employee e: listEmployees) {
			Object[] row = new Object[columns.length];
			row[0] = e.getEmployeeId();
			row[1] = e.getName();
			row[2] = e.getPhonesString();

			((DefaultTableModel) table.getModel()).insertRow(i, row);
			i++;
		}
	}
	
	public static void main(String[] args) {
		triangleShow = true;
		final driver frame = new driver();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		String s = (String)JOptionPane.showInputDialog(
                frame,
                "Enter employee name:\n",
                "Search Phone Dialog",
                JOptionPane.PLAIN_MESSAGE);

		String phonesString = "";
		try {
			phonesString = (fetchPhonesForEmployee(s));			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		String title = "Phones for: " + s;
		if (phonesString != "")  {
			title += "\n" + phonesString;
		}
		else
			title += " not found";
		
		JDialog jdialog = new JDialog(frame, "Hello", true);
		JLabel label = new JLabel(title);
		JLabel titleLabel = new JLabel();
		
		jdialog.add(label);
		jdialog.setSize(350, 150);
        jdialog.addComponentListener(new ComponentListener() {
        	  public void componentHidden(ComponentEvent e)
        	  {
        			triangleShow = false;
        			driver framea = new driver();
        			framea.setDefaultCloseOperation(EXIT_ON_CLOSE);
        			framea.pack();
        			framea.setLocationRelativeTo(null);
        		  	frame.setVisible(false);
        			framea.setVisible(true);
        	  }

        	  public void componentMoved(ComponentEvent e) {}

        	  public void componentResized(ComponentEvent e) {}

        	  public void componentShown(ComponentEvent e) {}
        	});
        jdialog.setLocationRelativeTo(frame);
        jdialog.setVisible(true);
	}
	
}
