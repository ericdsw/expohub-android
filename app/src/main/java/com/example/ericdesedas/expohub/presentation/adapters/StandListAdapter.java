package com.example.ericdesedas.expohub.presentation.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StandListAdapter extends RecyclerView.Adapter<StandListAdapter.StandViewHolder> {

    private Stand[] stands;
    private ImageDownloader imageDownloader;
    private Listener listener;

    /**
     * Constructor
     *
     * @param imageDownloader the {@link ImageDownloader} reference to present the stand's image
     */
    public StandListAdapter(ImageDownloader imageDownloader) {
        this.imageDownloader    = imageDownloader;
        this.stands             = new Stand[] {};
    }

    // Adapter Methods

    @Override
    public StandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stand_small, parent, false);
        return new StandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StandViewHolder holder, int position) {
        holder.updateStand(stands[position]);
    }

    @Override
    public int getItemCount() {
        return stands.length;
    }

    // Public Methods

    public void updateList(Stand[] stands) {
        this.stands = stands;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean hasStands() {
        return stands.length > 0;
    }

    // Internal Definitions

    public interface Listener {
        void onStandCardClick(Stand stand);
    }

    public class StandViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stand_image)             ImageView standImage;
        @BindView(R.id.stand_name)              TextView standName;
        @BindView(R.id.stand_short_description) TextView standShortDescription;

        private Stand stand;

        public StandViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void updateStand(Stand stand) {

            this.stand = stand;

            standName.setText(stand.name);
            standShortDescription.setText(stand.shortDescription());

            imageDownloader.setMaxImageSize(500)
                    .setCircularImage(stand.getImage(), standImage);
        }

        @OnClick(R.id.root_view)
        public void onClick() {
            if (listener != null) {
                listener.onStandCardClick(stand);
            }
        }
    }
}
