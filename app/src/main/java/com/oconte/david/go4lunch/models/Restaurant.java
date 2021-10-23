package com.oconte.david.go4lunch.models;

public class Restaurant {

    public String urlPhoto;
    public String userName;
    public String idUser;
    public String idRestaurantClick;


    public Restaurant( String userName, String idUser,String urlPhoto, String idRestaurantClick) {
        this.urlPhoto = urlPhoto;
        this.userName = userName;
        this.idUser = idUser;
        this.idRestaurantClick = idRestaurantClick;
    }

    public Restaurant() {

    }


    // GETTERS
    public String getUrlPhoto() {
        return urlPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdRestaurantClick() {
        return idRestaurantClick;
    }

    //SETTERS
    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdRestaurantClick(String idRestaurantClick) {
        this.idRestaurantClick = idRestaurantClick;
    }

    /*private String restaurantUid;
    private List<String> likedRestaurants;


    public Restaurant(String restaurantUid, List<String> likedRestaurants) {
        this.restaurantUid = restaurantUid;
        this.likedRestaurants = likedRestaurants;
    }

    public String getRestaurantUid() {
        return restaurantUid;
    }

    public List<String> getLikedRestaurants() {
        return likedRestaurants;
    }

    public void addLikedRestaurant(String restaurantUid){
        if(likedRestaurants == null) {
            this.likedRestaurants = new ArrayList<>();
        }
        this.likedRestaurants.add(restaurantUid);
    }

    public void removeLikedRestaurant(String restaurantUid){
        if(likedRestaurants != null) {
            int position = 0;
            for (String uid : likedRestaurants) {
                if (uid.equals(restaurantUid)) likedRestaurants.remove(position);
                position += 1;
            }
        }
    }

    public void setRestaurantUid(String restaurantUid) {
        this.restaurantUid = restaurantUid;
    }*/
}
