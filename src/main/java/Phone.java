public class Phone {

    String phoneNumber;
    int ownerId;
    
    public Phone(String phoneNumber, int ownerId) {
    	this.phoneNumber = phoneNumber;
    	this.ownerId = ownerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}