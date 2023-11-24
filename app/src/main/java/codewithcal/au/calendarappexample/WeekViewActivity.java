package codewithcal.au.calendarappexample;

import static codewithcal.au.calendarappexample.CalendarUtils.daysInMonthArray;
import static codewithcal.au.calendarappexample.CalendarUtils.daysInWeekArray;
import static codewithcal.au.calendarappexample.CalendarUtils.monthYearFromDate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView,adminListView;
    public String format_yyyyMMdd = "yyyyMMdd";
    Date currentTime = java.util.Calendar.getInstance().getTime();
    FirebaseDatabase database;
    //DatabaseReference mRef;
    DatabaseReference AdminEvent,PersonalEvent;
    String UID ,getname="";
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayListAdmin = new ArrayList<>();
    Map<LocalDate, Boolean> receivedMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        SimpleDateFormat formatH = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        String currentH = formatH.format(currentTime);

        UID = getIntent().getStringExtra("getID");

        Intent intent = getIntent();
        receivedMap = (Map<LocalDate, Boolean>) intent.getSerializableExtra("latenessMap");

        //mRef = FirebaseDatabase.getInstance().getReference("Calendar");

        database = FirebaseDatabase.getInstance();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(WeekViewActivity.this, android.R.layout.simple_list_item_1
        ,arrayList);

        final ArrayAdapter<String> adapter_admin = new ArrayAdapter<String>(WeekViewActivity.this, android.R.layout.simple_list_item_1
                ,arrayListAdmin);

        eventListView = findViewById(R.id.eventListView);
        eventListView.setAdapter(adapter);

        adminListView = findViewById(R.id.AdminListView);
        adminListView.setAdapter(adapter_admin);

        initWidgets();
        setWeekView();

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

        //getValue();

        AdminEvent = database.getReference("Calendar").child("Admin")
                .child(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        AdminEvent.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String v = snapshot.getValue(String.class);
                arrayListAdmin.add(v);
                adapter_admin.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter_admin.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(getname.equals("ChoiSeEun") | getname.equals("LeeNaYoung"))
        {
            Toast.makeText(WeekViewActivity.this," "+CalendarUtils.formattedDate(CalendarUtils.selectedDate),
                    Toast.LENGTH_SHORT).show();
            PersonalEvent = database.getReference("Calendar").child("Employee").child(getname)
                    .child(CalendarUtils.formattedDate(CalendarUtils.selectedDate)).child("PersonalEvents");
            PersonalEvent.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String v = snapshot.getValue(String.class);
                    arrayList.add(v);
                    adapter.notifyDataSetChanged();

                    //arrayListAdmin.add(v);
                    //adapter_admin.notifyDataSetChanged();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate( CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray( CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this,receivedMap);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        //setEventAdpater();
        //getValue();
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate =  CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate =  CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate =date;
        setWeekView();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        //getValue();
        //setEventAdpater();
    }
    public void nextMonthAction(View view)
    {
        startActivity(new Intent(this,MainActivity.class));
    }
    /*
    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(),dailyEvents);
        eventListView.setAdapter(eventAdapter);

    }

     */

    //◆◆◆◆◆◆◆◆◆◆◆◆◆ 내가 씀
    /*
    private void getValue()
    {

    }

     */

    public void newEventAction(View view) {
        //startActivity(new Intent(this,EventEditActivity2.class));
        Intent intent = new Intent(this, EventEditActivity2.class);
        intent.putExtra("getID",UID);
        startActivity(intent);
    }

}