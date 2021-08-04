package com.oconte.david.go4lunch.restoDetails;

import com.oconte.david.go4lunch.models.DetailsRestaurant;
import com.oconte.david.go4lunch.workMates.UserRepository;

import static com.squareup.okhttp.internal.Internal.instance;

public final class DetailsRestaurantRepositoryTest {

    private static volatile DetailsRestaurantRepositoryTest instance;
    private DetailsRestaurant detailsRestaurant;

    public DetailsRestaurantRepositoryTest() {
    }

    public static DetailsRestaurantRepositoryTest getInstance(){
        DetailsRestaurantRepositoryTest result = instance;
        if(result != null){
            return result;
        }
        synchronized (DetailsRestaurantRepositoryTest.class) {
            if (instance == null) {
                instance = new DetailsRestaurantRepositoryTest();
            }
            return instance;
        }
    }

    public void detailsRestaurant() {
        String nameRestaurant = detailsRestaurant.getNameRestaurant();

        //DetailsRestaurant detailsRestaurantRepositoryCreate = new DetailsRestaurant(nameRestaurant);

    }
}
