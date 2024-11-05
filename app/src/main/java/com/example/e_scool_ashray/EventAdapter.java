package com.example.e_scool_ashray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEvents(List<Event> newEvents) {
        this.eventList = newEvents;
        notifyDataSetChanged();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventTextView;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.eventText);
        }

        public void bind(Event event) {
            eventTextView.setText(event.getEventName() + " on " + event.getDate() + " by " + event.getUser());
        }
    }
}
