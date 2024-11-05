package ro.pub.cs.systems.eim.practicaltest01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTest01SecondaryActivity extends Activity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        // Task C.1: Create a secondary activity that shows the total count and provides OK and Cancel options.
        TextView totalCountTextView = findViewById(R.id.totalCountTextView);
        Button okButton = findViewById(R.id.okButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        // Task C.1: Retrieve the total count from the intent and display it in the TextView.
        // This is the number of times both buttons in the main activity were pressed in total.
        Intent intent = getIntent();
        int totalCount = intent.getIntExtra("total_count", 0);
        totalCountTextView.setText("Total Count: " + totalCount);

        // Task C.1.a: Set an onClickListener on the OK button to finish the activity with RESULT_OK.
        okButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);  // Task C.2: Sends a positive result back to MainActivity.
            finish();  // Closes the activity and returns to MainActivity.
        });

        // Task C.1.b: Set an onClickListener on the Cancel button to finish the activity with RESULT_CANCELED.
        cancelButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);  // Task C.2: Sends a negative result back to MainActivity.
            finish();  // Closes the activity and returns to MainActivity.
        });
    }
}
