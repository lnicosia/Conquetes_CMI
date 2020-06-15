package data.territory.resource;

import java.io.Serializable;

/**
 * Territory production data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Production implements Serializable{

	private static final long serialVersionUID = 1L;
	private int value;

	/**
	 * 
	 * @param value The value of production
	 */
	public Production(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value){
		this.value = value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Production other = (Production) obj;
		if (value != other.value)
			return false;
		return true;
	}

	

}
