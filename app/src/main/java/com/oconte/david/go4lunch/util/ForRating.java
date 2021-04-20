package com.oconte.david.go4lunch.util;

import com.oconte.david.go4lunch.R;

public abstract class ForRating {

    public static int calculateRating(Double rating) {
        if (rating == 0) return 0;
        if (rating < 0.84) return 1;
        if (rating < 1.68) return 2;
        if (rating < 2.52) return 3;
        if (rating < 3.36) return 4;
        if (rating < 4.7) return 5;
        return 6;
    }

    public static int firstStar (int rating) {
        if (rating <= 0) return R.drawable.star_border;
        if (rating ==1) return R.drawable.star_half;
        return R.drawable.star;
    }


    public static int secondStar(int rating) {
        if (rating <= 2) return R.drawable.star_border;
        if (rating == 3) return R.drawable.star_half;
        return R.drawable.star;
    }

    public static int thirdStar(int rating) {
        if (rating <= 4) return R.drawable.star_border;
        if (rating == 5) return R.drawable.star_half;
        return R.drawable.star;
    }

}
