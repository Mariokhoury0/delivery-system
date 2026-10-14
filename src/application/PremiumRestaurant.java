package application;

public class PremiumRestaurant extends Restaurant {
	private double minimumOrderAllowed;
    private double extraComissionPrecent;
    //fields 

    public PremiumRestaurant(int rescode, String resname, String kitchentype,
                             double rating, boolean open, double basedeliveryfee,
                             double minimumOrderAllowed, double extraComissionPrecent)
    {
        super(rescode, resname, kitchentype, rating, open, basedeliveryfee);
        this.minimumOrderAllowed = minimumOrderAllowed;
        this.extraComissionPrecent = extraComissionPrecent;
    }
    //constructor

    public double getMinimumOrderAllowed()
    {
        return minimumOrderAllowed;
    }

    public void setMinimumOrderAllowed(double minimumOrderAllowed)
    {
        this.minimumOrderAllowed = minimumOrderAllowed;
    }

    public double getExtraComissionPrecent()
    {
        return extraComissionPrecent;
    }

    public void setExtraComissionPrecent(double extraComissionPrecent)
    {
        this.extraComissionPrecent = extraComissionPrecent;
    }

    public boolean isAboveMinimumOrder(double basePrice)
    {
        return basePrice >= minimumOrderAllowed;
    }

    @Override
    public double CalculateFinalPrice(double base)
    {
        return base + basedeliveryfee + base * extraComissionPrecent / 100.0;
    }

    public String getRestaurantType()
    {
        return "Premium Restaurant";
    }
    //getters/setters
    @Override
    public String toString()
    {
        return super.toString()
                + " PremiumRestaurant [minimumOrderAllowed="
                + minimumOrderAllowed
                + ", extraComissionPrecent="
                + extraComissionPrecent
                + "]";
    }
    //toString
}
	
	
	


