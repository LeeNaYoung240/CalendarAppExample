package codewithcal.au.calendarappexample;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private Map<LocalDate,Boolean>latenessMap;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener,Map<LocalDate,Boolean>l)
    {
        latenessMap = l;
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size()>15)
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else
            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener,days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        LocalDate date = days.get(position);
        int color = Color.argb(128, 255, 153, 153);

        if(date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

            if(date.getDayOfWeek() == DayOfWeek.SUNDAY)
                holder.dayOfMonth.setTextColor(Color.RED);
            else if(date.getDayOfWeek()==DayOfWeek.SATURDAY)
                holder.dayOfMonth.setTextColor(Color.BLUE);

            if(latenessMap.containsKey(date) && latenessMap.get(date))
                holder.itemView.setBackgroundColor(color);

            if(date.equals(CalendarUtils.selectedDate))
                holder.parentView.setBackgroundColor(Color.LTGRAY);


        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date );
    }
}