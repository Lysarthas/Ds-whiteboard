package manager.share;

import java.io.Serializable;

public class Identity implements Serializable{
	
	/**
     *
     */
    private static final long serialVersionUID = -5278576148196578914L;

    String myname;
	int myIP;
	
	public boolean equals(Object o) {
		boolean equal = false;
		if(o != null && o.getClass() == this.getClass()) {
			Identity io = (Identity)o;
			int targetIP = io.getIP();
			String targetName = io.getName();
			if(this.getIP() == targetIP && this.getName() == targetName) {
				equal = true;
			}
		}
		return equal;
	}
	
	public String getName() {
		return this.myname;
	}
	
	public int getIP() {
		return this.myIP;
	}
	
	public void setName(String name) {
		this.myname = name;
	}
	
	public void setIP(int IPaddress) {
		this.myIP = IPaddress;
	}
}