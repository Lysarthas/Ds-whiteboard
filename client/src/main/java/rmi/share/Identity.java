package rmi.share;

import java.io.Serializable;

public class Identity implements Serializable{
	
	/**
     *
     */
    private static final long serialVersionUID = -5278576148196578914L;

    String myname;

    public Identity(String userName) {
        this.myname = userName;
    }
	
    public boolean equals(Object o) {
        return o != null && o.getClass() == this.getClass() && this.getName().equals(((Identity) o).getName());
	}
	
	public String getName() {
		return this.myname;
	}
	
	public void setName(String name) {
		this.myname = name;
	}
}