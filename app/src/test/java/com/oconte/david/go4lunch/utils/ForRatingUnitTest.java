package com.oconte.david.go4lunch.utils;

import static com.oconte.david.go4lunch.util.ForRating.firstStar;
import static com.oconte.david.go4lunch.util.ForRating.secondStar;
import static com.oconte.david.go4lunch.util.ForRating.thirdStar;
import static org.junit.Assert.assertEquals;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.util.ForRating;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ForRatingUnitTest {

    @Test
    public void calculateRatingAndReturnCorrectValue() throws Exception {
        assertEquals(0, ForRating.calculateRating(0.0));
        assertEquals(1, ForRating.calculateRating(0.8));
        assertEquals(2, ForRating.calculateRating(1.5));
        assertEquals(3, ForRating.calculateRating(2.0));
        assertEquals(4, ForRating.calculateRating(3.0));
        assertEquals(5, ForRating.calculateRating(4.0));
        assertEquals(6, ForRating.calculateRating(5.0));
    }

    @Test
    public void valueRatingAndReturnRightValueStar() throws Exception {
        int rating = 0;
        Assert.assertEquals(R.drawable.star_border, firstStar(rating));
        assertEquals(R.drawable.star_border, secondStar(rating));
        assertEquals(R.drawable.star_border, thirdStar(rating));

        rating = 1;
        assertEquals(R.drawable.star_half, firstStar(rating));
        assertEquals(R.drawable.star_border, secondStar(rating));
        assertEquals(R.drawable.star_border, thirdStar(rating));

        rating = 2;
        assertEquals(R.drawable.star, firstStar(rating));
        assertEquals(R.drawable.star_border, secondStar(rating));
        assertEquals(R.drawable.star_border, thirdStar(rating));

        rating = 3;
        assertEquals(R.drawable.star, firstStar(rating));
        assertEquals(R.drawable.star_half, secondStar(rating));
        assertEquals(R.drawable.star_border, thirdStar(rating));

        rating= 4;
        assertEquals(R.drawable.star, firstStar(rating));
        assertEquals(R.drawable.star, secondStar(rating));
        assertEquals(R.drawable.star_border, thirdStar(rating));

        rating = 5;
        assertEquals(R.drawable.star, firstStar(rating));
        assertEquals(R.drawable.star, secondStar(rating));
        assertEquals(R.drawable.star_half, thirdStar(rating));

        rating = 6;
        assertEquals(R.drawable.star, firstStar(rating));
        assertEquals(R.drawable.star, secondStar(rating));
        assertEquals(R.drawable.star, thirdStar(rating));
    }
}
