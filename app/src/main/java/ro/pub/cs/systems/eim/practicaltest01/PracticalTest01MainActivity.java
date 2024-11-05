package ro.pub.cs.systems.eim.practicaltest01;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest01MainActivity extends AppCompatActivity {
    private static final int THRESHOLD = 24; // D.1.b: Pragul de declanșare a serviciului dacă count1 + count2 > THRESHOLD

    private int count1 = 0; // Variabilă pentru numărul de apăsări pe primul buton
    private int count2 = 0; // Variabilă pentru numărul de apăsări pe al doilea buton

    private ActivityResultLauncher<Intent> activityResultLauncher; // C.2: Lansator pentru a gestiona rezultatul activității secundare
    private Intent serviceIntent; // Referință la intenția pentru serviciul PracticalTest01Service
    private PracticalTest01BroadcastReceiver receiver; // Referință la receiver-ul de tip broadcast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        // Inițializăm referințele la componentele din layout
        TextView counterTextView1 = findViewById(R.id.counterTextView1);
        TextView counterTextView2 = findViewById(R.id.counterTextView2);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button navigateButton = findViewById(R.id.navigateButton);

        receiver = new PracticalTest01BroadcastReceiver(); // Inițializare receiver

        // A.1: Adăugăm ascultători pentru evenimentele de click pe butoane
        button1.setOnClickListener(v -> {
            count1++; // Incrementăm count1
            counterTextView1.setText(String.valueOf(count1)); // Actualizăm textul pentru primul counter
            checkAndStartService(); // Verificăm și pornim serviciul dacă este cazul (D.1.b)
        });

        button2.setOnClickListener(v -> {
            count2++; // Incrementăm count2
            counterTextView2.setText(String.valueOf(count2)); // Actualizăm textul pentru al doilea counter
            checkAndStartService(); // Verificăm și pornim serviciul dacă este cazul (D.1.b)
        });

        // C.1: Declanșăm activitatea secundară la apăsarea butonului de navigare
        navigateButton.setOnClickListener(v -> {
            Intent intent = new Intent(PracticalTest01MainActivity.this, PracticalTest01SecondaryActivity.class);
            int totalCount = count1 + count2; // Calculăm suma celor două countere
            intent.putExtra("total_count", totalCount); // Transmitem suma către activitatea secundară
            activityResultLauncher.launch(intent); // Lansăm activitatea secundară și așteptăm rezultatul
        });

        // C.2: Gestionăm rezultatul activității secundare prin ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(this, "Result: OK", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "Result: Canceled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Apply system window insets for better layout adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.practicaltest01), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Salvăm datele (count1 și count2) în bundle pentru a le restaura ulterior
    // B.2.a și B.2.b: Salvarea și restaurarea manuală a stării activității
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("count1", count1); // Salvăm valoarea lui count1
        outState.putInt("count2", count2); // Salvăm valoarea lui count2
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count1 = savedInstanceState.getInt("count1", 0); // Restaurăm valoarea lui count1
        count2 = savedInstanceState.getInt("count2", 0); // Restaurăm valoarea lui count2

        TextView counterTextView1 = findViewById(R.id.counterTextView1);
        TextView counterTextView2 = findViewById(R.id.counterTextView2);

        counterTextView1.setText(String.valueOf(count1)); // Actualizăm interfața grafică pentru primul counter
        counterTextView2.setText(String.valueOf(count2)); // Actualizăm interfața grafică pentru al doilea counter
    }

    // D.1.b: Funcție care verifică dacă serviciul trebuie pornit și pornește serviciul
    private void checkAndStartService() {
        if (count1 + count2 > THRESHOLD && serviceIntent == null) {
            serviceIntent = new Intent(this, PracticalTest01Service.class);
            serviceIntent.putExtra("count1", count1); // Transmitem valoarea lui count1
            serviceIntent.putExtra("count2", count2); // Transmitem valoarea lui count2
            startService(serviceIntent); // Pornim serviciul PracticalTest01Service
        }
    }

    // D.2: Înregistrăm receiver-ul pentru a asculta acțiunile trimise de PracticalTest01Service
    // Inregistreaza pe starea "resume", cand nu exista activitati in derulare ca click, rotire,...
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PracticalTest01Service.ACTION_ARITHMETIC_MEAN);
        filter.addAction(PracticalTest01Service.ACTION_GEOMETRIC_MEAN);
        filter.addAction(PracticalTest01Service.ACTION_RANDOM);
        registerReceiver(receiver, filter); // Înregistrăm receiver-ul pentru a asculta acțiunile definite
    }

    // Dezregistrăm receiver-ul și oprim serviciul la distrugerea activității
    @Override
    protected void onDestroy() {
        if (serviceIntent != null) {
            stopService(serviceIntent); // Oprim serviciul dacă acesta este activ
        }
        if (receiver != null) {
            unregisterReceiver(receiver); // Dezregistrăm receiver-ul
            receiver = null;
        }
        super.onDestroy();
    }
}
