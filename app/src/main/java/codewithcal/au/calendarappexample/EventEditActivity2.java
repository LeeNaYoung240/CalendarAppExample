package codewithcal.au.calendarappexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventEditActivity2 extends AppCompatActivity {
    private DatabaseReference databaseReference;

    private EditText eventnameET;
    private TextView eventDateTV, eventTimeTV;
    // private CalendarView calendarView;
    //private DatabaseReference databaseReference;
    static int onButtonEvent = 0;
    public String format_yyyyMMdd = "yyyyMMdd";
    static String currentH;
    String getname="null", UID;
    private final
    Date currentTime = java.util.Calendar.getInstance().getTime();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit2);

        initWidgets();

        LocalTime time = LocalTime.now();
        eventDateTV.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " +CalendarUtils.formattedTime(time));

        SimpleDateFormat formatH = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        currentH = formatH.format(currentTime);

        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        UID=getIntent().getStringExtra("getID");

        if(UID.equals("8zTw2No6bcVF4gfrWTWu9xsG9e13"))
        {
            getname = "ChoiSeEun";
        }
        else if(UID.equals("Dz1qzULDQ3ZsYUkD1ah1l3aCdrO2"))
        {
            getname = "LeeNaYoung";
        }
        else if(UID.equals("1e6o978GpTOXBpunrGao5zL6ggr2"))
        {
            getname = "Admin";
        }

    }

    private void initWidgets() {
        eventnameET = findViewById(R.id.eventNameET);
        eventDateTV =findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }


    public void saveEventAction(View view) {
        onButtonEvent = onButtonEvent + 1;
        //String eventName = eventnameET.getText().toString();
        //Event newEvent = new Event(eventName,CalendarUtils.selectedDate,time);

        if(getname.equals("Admin"))
        {
            databaseReference.child("Admin").child(eventDateTV.getText().toString()).
                    child("event"+onButtonEvent).setValue(eventnameET.getText().toString());
        }
        else
        {

            //databaseReference.child("Admin").child(eventDateTV.getText().toString()).
            //        child("event"+onButtonEvent).setValue(eventnameET.getText().toString());

            databaseReference.child("Employee").child(getname).child(eventDateTV.getText().toString())
                    .child("PersonalEvents").child("event"+onButtonEvent).setValue(eventnameET.getText().toString());




        }
        //Event.eventsList.add(newEvent);
        finish();
    }
}