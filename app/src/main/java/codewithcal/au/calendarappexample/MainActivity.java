package codewithcal.au.calendarappexample;

import static codewithcal.au.calendarappexample.CalendarUtils.daysInMonthArray;
import static codewithcal.au.calendarappexample.CalendarUtils.monthYearFromDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText,CalendarName;
    private RecyclerView calendarRecyclerView;
    Map<LocalDate, Boolean> localLatenessMap = new HashMap<>();
    static String UID,getname;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UID = getIntent().getStringExtra("getID");
        initWidgets();


        CalendarName = findViewById(R.id.MonthlyButton);

        if(UID.equals("8zTw2No6bcVF4gfrWTWu9xsG9e13"))
        {
            getname = "ChoiSeEun";
           CalendarName.setText("ChoiSeEun's\nCalendar");
        }
        //else if(UID.equals("1e6o978GpTOXBpunrGao5zL6ggr2"))
        else if(UID.equals("Dz1qzULDQ3ZsYUkD1ah1l3aCdrO2"))
        {
            getname = "LeeNaYoung";
            //CalendarName.setText("Admin's\nCalendar");
            CalendarName.setText("LeeNaYoung's\nCalendar");
        }
        else if(UID.equals("1e6o978GpTOXBpunrGao5zL6ggr2"))
        {
            CalendarName.setText("Admin's\nCalendar");
        }

        //loadFromDBRoMemory();
        CalendarUtils.selectedDate = LocalDate.now();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference latenessRef = database.getReference("Calendar").child("Employee")
                .child(getname);

        latenessRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   for(DataSnapshot dateSnapshot : snapshot.getChildren())
                   {
                       String dateString = dateSnapshot.getKey();
                       String latenessString = dateSnapshot.child("PersonalInfo")
                               .child("지각 여부").getValue(String.class);

                       if(latenessString != null && latenessString.contains("true"))
                       {
                           LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
                           localLatenessMap.put(date,true);
                       }
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        setMonthView();
    }
    /*
        private void loadFromDBRoMemory() {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.populateNoteListArray();
        }
    */
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate( CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray( CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this,localLatenessMap);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate =  CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate =  CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date!=null) {
            CalendarUtils.selectedDate = date;
            setMonthView();


        }
    }
    public void weeklyAction(View view) {

        //startActivity(new Intent(this,WeekViewActivity.class));

        Intent intent = new Intent(this, WeekViewActivity.class);
        intent.putExtra("latenessMap", (Serializable) localLatenessMap);
        intent.putExtra("getID",UID);
        startActivity(intent);
    }

    public void previousWeeklyAction(View view) {
    }

    public void nextWeekAction(View view) {
    }

    public void nextEventAction(View view) {
    }



}







