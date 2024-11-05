package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PracticalTest01BroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "PracticalTest01Receiver"; // Tag pentru loguri

    @Override
    public void onReceive(Context context, Intent intent) {
        // Task D.2: Implementarea unui ascultător pentru mesajele de difuzare (broadcast).
        // Primirea mesajelor de tip broadcast trimise de serviciu.

        // Obține acțiunea intentului primit
        String action = intent.getAction();

        // Preia timestamp-ul și valorile mediei aritmetice și geometrice transmise de serviciu
        String timestamp = intent.getStringExtra("timestamp");
        double arithmeticMean = intent.getDoubleExtra("arithmetic_mean", 0);
        double geometricMean = intent.getDoubleExtra("geometric_mean", 0);

        // Creăm un mesaj cu informațiile primite de la serviciu
        String message = "Action: " + action + "\nTime: " + timestamp +
                "\nArithmetic Mean: " + arithmeticMean +
                "\nGeometric Mean: " + geometricMean;

        // Task D.2: Logăm mesajul în consolă folosind un tag specific pentru debugging.
        // Afișăm mesajul în loguri pentru debug (folosind tag-ul definit)
        Log.d(TAG, message);

        // Task D.2: Afișăm mesajul într-un Toast pentru a fi vizibil utilizatorului.
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
