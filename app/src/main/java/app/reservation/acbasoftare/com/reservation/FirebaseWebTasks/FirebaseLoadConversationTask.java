package app.reservation.acbasoftare.com.reservation.FirebaseWebTasks;

import android.os.AsyncTask;

import app.reservation.acbasoftare.com.reservation.Interfaces.IMessagingMetaData;

/**
 * Created by user on 5/8/17.
 */

public class FirebaseLoadConversationTask extends AsyncTask<Void, Void, Void> {
    private IMessagingMetaData user,selectedUser;
    public FirebaseLoadConversationTask(IMessagingMetaData user, IMessagingMetaData selectedUser){
        this.user = user;
        this.selectedUser = selectedUser;
    }
    @Override
    protected void onPreExecute(){


    }
    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
