import java.util.ArrayList;
import java.util.List;

public class Employee {
	int employeeId;
	String name;
	List<Phone> phones;
	
    public Employee(int employeeId, String name) {
        this.employeeId = employeeId;
        this.name = name;
        this.phones = new ArrayList<Phone>();
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int workerId) {
        this.employeeId = workerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phones.add(new Phone(phone, this.getEmployeeId()));
    }

    public List<Phone> getPhones() {
    	return this.phones;
    }
     
    public String getPhonesString() {
    	String res = "";
    	for (Phone ph: this.getPhones()) {
    		res = res + " " + ph.getPhoneNumber();
    	}
    	return res;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                '}';
    }
}
