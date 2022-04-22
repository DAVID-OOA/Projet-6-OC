package com.oconte.david.go4lunch.settings;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.injection.Injection;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.repositories.UserRepositoryImpl;
import com.oconte.david.go4lunch.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class AlarmWorker extends Worker {

    public String nameRestaurantSelected;
    public String adressRestaurant;
    public String listOfWorkmates;

    private Context context;

    public UserRepositoryImpl userRepositoryImpl;
    public User currentUser;
    public List<User> users;

    public static final String CHANNEL_ID = "channel";

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

        userRepositoryImpl = Injection.getUserRepository(Injection.provideFireBaseAuth(), Injection.provideFireStore());

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
     * It's the firebase request for notification.
     */
    private void executeFirebaseRequest() {
        users = new ArrayList<>();

        userRepositoryImpl.getAllUserFromFirebase().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                    User user = documentSnapshot.toObject(User.class);

                    if (user != null && user.getIdRestaurantPicked() != null) {
                        if (user.getUid() != null) {
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
