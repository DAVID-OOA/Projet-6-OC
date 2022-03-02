package com.oconte.david.go4lunch;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.RestaurantDetailRepository;
import com.oconte.david.go4lunch.repositories.UserRepository;
import com.oconte.david.go4lunch.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class AlarmWorker extends Worker {

    SharedPreferences preferences;
    String nameRestaurantSelected;
    String adressRestaurant;
    String listOfWorkmates;

    public FirebaseAuth firebaseAuth;
    public FirebaseFirestore firebaseFirestore;

    private Context context;

    Restaurant restaurant;
    UserRepository userRepository;
    RestaurantDetailRepository restaurantDetailRepository;
    User currentUser;
    List<User> users;

    public static final String CHANNEL_ID = "channel";
    //private User result;

    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }


    /**
     * It's the action do by the Worker when notification is check.
     * @return Result.
     */
    @NonNull
    @Override
    public Worker.Result doWork() {

        restaurantDetailRepository = Injection.provideRestaurantDetailsRepository(Injection.provideFireBaseAuth(), Injection.provideFireStore());

        userRepository = Injection.getUserRepository(Injection.provideFireBaseAuth(), Injection.provideFireStore());

        executeFirebaseRequest();


        return Worker.Result.success();

    }

    /**
     * It's the notification with different elements:
     */
    private void displayNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        String messageTitle;
        String messageBody;
        if (listOfWorkmates != null && listOfWorkmates.length() > 0) {
            messageTitle = context.getString(R.string.notification_message);
            messageBody = String.format(messageTitle, nameRestaurantSelected, adressRestaurant, listOfWorkmates);
        } else {
            messageTitle = context.getString(R.string.message_notification_alone);
            messageBody = String.format(messageTitle, nameRestaurantSelected, adressRestaurant);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(context.getString(R.string.title_notification))
                .setContentText(context.getString(R.string.subtitle_notification))
                .setSmallIcon(R.drawable.go4lunch_icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));

        notificationManager.notify(1, notification.build());
    }


    /**
     * It's the Http request for notification.
     */
    private void executeFirebaseRequest() {

        //String userIdRestaurantPicked = String.valueOf(restaurantDetailRepository.getPickedUsersFromRestaurant(Objects.requireNonNull(user).getIdRestaurantPicked()));

        users = new ArrayList<>();

        userRepository.getAllUserFromFirebase().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                    User user = documentSnapshot.toObject(User.class);

                    if (user != null && user.getIdRestaurantPicked() != null) {
                        if (user.getUid().equals("FheiJWiHyMX7wRcc9wvPTgBQ7FH3")) {
                            currentUser = user;
                        } else {
                            users.add(user);
                        }
                    }
                }
                listUserGoing();
            }
        });
    }

    private void listUserGoing() {
        if (currentUser != null) {
            List<String> usersName = new ArrayList<>();
            for (User user : users) {
                if (user.getIdRestaurantPicked().equals(currentUser.getIdRestaurantPicked())) {
                    usersName.add(user.getUsername());
                }
            }
            nameRestaurantSelected = currentUser.getNameRestaurantPicked();
            adressRestaurant = currentUser.getAdressRestaurantPicked();
            listOfWorkmates = TextUtil.convertListToString(usersName);
            displayNotification();
        } else {
            noRestaurantPicked();
        }
    }

    @Nullable
    private FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * It's for Error Message.
     */
    private void noRestaurantPicked() {
        AlertDialog.Builder myAlertDialogue = new AlertDialog.Builder(context);
        myAlertDialogue.setTitle("Alert ! ");
        myAlertDialogue.setMessage("No restaurant picked");

        myAlertDialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        myAlertDialogue.show();
    }
}
