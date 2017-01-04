package com.example.ericdesedas.expohub.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    private FairEvent[] fairEvents;
    private ImageDownloader imageDownloader;
    private Listener listener;

    /**
     * Constructor
     *
     * @param imageDownloader the {@link ImageDownloader} instance to present the event's image
     */
    public EventListAdapter(ImageDownloader imageDownloader) {
        this.imageDownloader    = imageDownloader;
        this.fairEvents         = new FairEvent[] {};
    }

    // Adapter Methods

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_small, parent, false);
        return new EventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        holder.updateFairEvent(fairEvents[position]);
    }

    @Override
    public int getItemCount() {
        return fairEvents.length;
    }

    // Public Methods

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean hasFairEvents() {
        return fairEvents.length > 0;
    }

    public void swapList(FairEvent[] fairEvents) {
        this.fairEvents = fairEvents;
    }

    // Internal definitions

    /**
     * Listener interface
     */
    public interface Listener {
        void onEventCellClick(FairEvent fairEvent);
    }

    /**
     * Custom ViewHolder class
     */
    public class EventListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_title)     TextView eventTitle;
        @BindView(R.id.event_date)      TextView eventDate;
        @BindView(R.id.event_image)     ImageView eventImage;
        @BindView(R.id.event_type_name) TextView eventTypeName;

        private FairEvent fairEvent;
        private Context context;

        public EventListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.context = itemView.getContext();
        }

        public void updateFairEvent(FairEvent fairEvent) {

            this.fairEvent = fairEvent;

            eventTitle.setText(this.fairEvent.title);
            eventDate.setText(this.fairEvent.parsedDate());
            eventTypeName.setText(String.format(context.getString(R.string.label_event_type), this.fairEvent.eventType.get().name));

            imageDownloader.setMaxImageSize(500)
                    .setImage(this.fairEvent.image, eventImage);
        }

        @OnClick(R.id.root_view)
        public void onCellClick() {
            if (listener != null) {
                listener.onEventCellClick(fairEvent);
            }
        }
    }
}
