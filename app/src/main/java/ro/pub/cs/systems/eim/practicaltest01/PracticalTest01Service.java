package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PracticalTest01Service extends Service {
    // D.1.a: Declaram acțiunile pentru fiecare tip de difuzare a mesajelor
    public static final String ACTION_ARITHMETIC_MEAN = "ro.pub.cs.systems.eim.practicaltest01.ARITHMETIC_MEAN";
    public static final String ACTION_GEOMETRIC_MEAN = "ro.pub.cs.systems.eim.practicaltest01.GEOMETRIC_MEAN";
    public static final String ACTION_RANDOM = "ro.pub.cs.systems.eim.practicaltest01.RANDOM";

    private final Handler handler = new Handler(); // Handler pentru a executa operațiuni cu întârziere
    private int count1; // Valoare pentru numărul de apăsări de pe primul buton
    private int count2; // Valoare pentru numărul de apăsări de pe al doilea buton

    // Runnable care trimite difuzări cu intervale regulate
    private final Runnable broadcastRunnable = new Runnable() {
        @Override
        public void run() {
            double arithmeticMean = (count1 + count2) / 2.0; // Calculăm media aritmetică
            double geometricMean = Math.sqrt(count1 * count2); // Calculăm media geometrică
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("timestamp", currentTime);
            broadcastIntent.putExtra("arithmetic_mean", arithmeticMean);
            broadcastIntent.putExtra("geometric_mean", geometricMean);

            // D.1.a: Alegem aleator o acțiune pentru difuzare
            String[] actions = {ACTION_ARITHMETIC_MEAN, ACTION_GEOMETRIC_MEAN, ACTION_RANDOM};
            broadcastIntent.setAction(actions[(int) (Math.random() * actions.length)]);

            // D.1.a: Trimitem difuzarea
            sendBroadcast(broadcastIntent);

            // Repetăm acest runnable la fiecare 10 secunde
            int delay = 10000;
            handler.postDelayed(this, delay);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            // Preia valorile din intentul cu care a fost pornit serviciul
            count1 = intent.getIntExtra("count1", 0);
            count2 = intent.getIntExtra("count2", 0);
        }
        // Pornim runnable-ul care va face difuzările
        handler.post(broadcastRunnable);
        return START_STICKY;
        // Indicație că dacă serviciul este ucis de sistem, acesta trebuie recreat cu ultimul intent
    }

    @Override
    public void onDestroy() {
        // Curățăm orice callback-uri și mesaje înainte de a distruge serviciul
        handler.removeCallbacks(broadcastRunnable);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Nu avem nevoie de binding pentru acest serviciu
        return null;
    }
}
