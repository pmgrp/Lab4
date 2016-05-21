package a15.group.lab4;

import java.util.Comparator;

/**
 * Created by eugeniosorbellini on 27/04/16.
 */
public class OfferPriceComparator implements Comparator<DailyOffer> {

    public int compare(DailyOffer lhs, DailyOffer rhs){

        return lhs.getPrice() - rhs.getPrice();
    }
}
