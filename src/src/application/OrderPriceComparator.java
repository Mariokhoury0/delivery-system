package application;
import java.util.Comparator;
public class OrderPriceComparator implements Comparator<Order>{
	// this is the Comparator class for Order prices 
	public int compare(Order o1, Order o2) {
        return Double.compare(o2.getFinalPrice(), o1.getFinalPrice());
    }
}

