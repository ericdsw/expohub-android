package com.example.ericdesedas.expohub.presentation.adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FairListAdapter extends RecyclerView.Adapter<FairListAdapter.FairViewHolder> {

    public static final int VIEW_TYPE_LARGE     = 0;
    public static final int VIEW_TYPE_CONDENSED = 1;

    private Fair[] fairs;
    private ImageDownloader imageDownloader;
    private Listener listener;
    private int viewSize = VIEW_TYPE_LARGE;

    /**
     * Constructor
     *
     * @param imageDownloader the {@link ImageDownloader} reference to present the fair's image
     */
    public FairListAdapter(ImageDownloader imageDownloader) {
        this.fairs              = new Fair[]{};
        this.imageDownloader    = imageDownloader;
    }

    // Adapter Methods

    @Override
    public FairViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewSize == VIEW_TYPE_LARGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fair, parent, false);
            return new FairViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fair_condensed, parent, false);
            return new FairViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(FairViewHolder holder, int position) {
        holder.updateFair(fairs[position]);
    }

    @Override
    public int getItemCount() {
        return fairs.length;
    }

    // Public Methods

    public void updateFairList(Fair[] fairs) {
        this.fairs = fairs;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setViewSize(int viewSize) {
        this.viewSize = viewSize;
    }

    public boolean hasFairs() {
        return fairs.length > 0;
    }

    // Internal definitions

    public interface Listener {
        void onFairCardClick(Fair fair, List<Pair<View, String>> transitioningElements);
    }

    /**
     * FairViewHolder class
     * Binds UI logic to fair card
     */
    public class FairViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fair_image)              ImageView fairImage;
        @BindView(R.id.fair_name)               TextView fairName;
        @BindView(R.id.fair_short_description)  TextView fairShortDescription;
        @BindView(R.id.fair_dates)              TextView fairDates;
        @BindView(R.id.card_content_wrapper)    View cardContentWrapper;

        private Fair fair;
        private Context context;

        public FairViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

            context = itemView.getContext();
        }

        public void updateFair(Fair fair) {

            this.fair = fair;

            fairName.setText(fair.name);
            fairShortDescription.setText(fair.getShortDescription());

            fairDates.setText(String.format(context.getString(R.string.label_dates), fair.parsedStartingDate(), fair.parsedEndingDate()));

            imageDownloader.setMaxImageSize(500)
                    .setImage(fair.getImage(), fairImage);
        }

        @OnClick(R.id.card_view)
        public void onCardClick() {

            if (listener != null) {

                List<Pair<View, String>> transitioningElements = new ArrayList<>();
                transitioningElements.add(new Pair<View, String>(fairImage, context.getString(R.string.transition_fair_card_image)));
                transitioningElements.add(new Pair<>(cardContentWrapper, context.getString(R.string.transition_fair_card_content)));

                listener.onFairCardClick(fair, transitioningElements);
            }
        }
    }
}
