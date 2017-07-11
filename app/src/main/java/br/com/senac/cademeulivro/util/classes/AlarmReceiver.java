package br.com.senac.cademeulivro.util.classes;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import br.com.senac.cademeulivro.util.constante.Constantes;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static int NOTIFICATION_ID_VALUE = 12;
    public static int idNotificacao;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int acao = intent.getExtras().getInt("action");
        //verifica se o usuário quer receber notificação
        String PREF_NAME = "MyPrefsFile";
        SharedPreferences settingsShared = context.getSharedPreferences(PREF_NAME, 0);

        //se estiver como false, ele cancela todas as notificações
        if (!settingsShared.getBoolean("receberNotificacao", false)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    Constantes.BROADCAST_NOTIFICAR,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarm.cancel(pendingIntent);
        } else if(acao == Constantes.BROADCAST_NOTIFICAR) {
            Notification n = intent.getParcelableExtra(NOTIFICATION);
            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
            manager.notify(id, n);
        }
    }
}
