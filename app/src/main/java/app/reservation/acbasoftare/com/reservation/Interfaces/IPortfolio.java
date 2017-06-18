package app.reservation.acbasoftare.com.reservation.Interfaces;

import android.support.v7.widget.RecyclerView;

import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;

/**
 * Created by user on 6/13/17.
 */

public interface IPortfolio {
    void fetchPortfolioImages(Stylist stylist, RecyclerView rv);
    void fetchStylistImages(Stylist stylist, RecyclerView rv);
}
