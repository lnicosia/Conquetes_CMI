package data.territory.resource;

import java.io.Serializable;

/**
 * Territory stock data
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class Stock implements Serializable{

	private static final long serialVersionUID = 1L;
	private int value;

	/**
	 * 
	 * @param value The value of stock
	 */
	public Stock(int value) {
		this.value = value;
	}
	
	public void setValue(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Decrease resource of stock
	 * @param value Maximal resource decreased
	 * @return Resource decreased (0 if the stock is empty)
	 */
	public int decrease(int value) {
		if (this.value - value >= 0) {
			this.value -= value;
			return value;
		} else {
			int endOfStock = this.value;
			this.value = 0;
			return endOfStock;
		}
	}

	public String toString() {
		String returnString = "Stock : ";
		if(value < 1000000){
			return returnString + Integer.toString(value);
		}
		else{
			return returnString + "oo";
		}
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
		Stock other = (Stock) obj;
		if (value != other.value)
			return false;
		return true;
	}

	

}
