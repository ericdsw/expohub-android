package com.example.ericdesedas.expohub.presentation.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Sponsor;
import com.example.ericdesedas.expohub.helpers.constants.SponsorRankTypes;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SponsorListAdapter extends RecyclerView.Adapter<SponsorListAdapter.SponsorListViewHolder> {

    private Sponsor[] sponsors;
    private ImageDownloader imageDownloader;
    private Listener listener;

    public SponsorListAdapter(ImageDownloader imageDownloader) {
        this.imageDownloader    = imageDownloader;
        this.sponsors           = new Sponsor[] {};
    }

    @Override
    public SponsorListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sponsor, parent, false);
        return new SponsorListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SponsorListViewHolder holder, int position) {
        holder.updateSponsor(sponsors[position]);
    }

    @Override
    public int getItemCount() {
        return sponsors.length;
    }

    // Public Methods

    public void updateList(Sponsor[] sponsors) {
        this.sponsors = sponsors;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean hasSponsors() {
        return sponsors.length > 0;
    }

    // Interface

    public interface Listener {
        void onSponsorCellClick(Sponsor sponsor);
    }

    // VieWHolder

    public class SponsorListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sponsor_logo) ImageView sponsorLogo;
        @BindView(R.id.sponsor_name) TextView sponsorName;
        @BindView(R.id.sponsor_type) TextView sponsorType;

        private Sponsor sponsor;
        private Context context;

        public SponsorListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.context = itemView.getContext();
        }

        public void updateSponsor(Sponsor sponsor) {

            this.sponsor = sponsor;

            sponsorName.setText(sponsor.name);
            sponsorType.setText(String.format(context.getString(R.string.label_sponsor_type), sponsor.getSponsorRank().name));

            switch (sponsor.getSponsorRank().getId()) {
                case SponsorRankTypes.DIAMOND:
                    sponsorType.setTextColor(ContextCompat.getColor(context, R.color.diamondSponsorColor));
                    break;
                case SponsorRankTypes.GOLD:
                    sponsorType.setTextColor(ContextCompat.getColor(context, R.color.goldSponsorColor));
                    break;
                default:
                    sponsorType.setTextColor(ContextCompat.getColor(context, R.color.silverSponsorColor));
                    break;
            }

            imageDownloader.setMaxImageSize(500)
                    .setImage(sponsor.logo, sponsorLogo);
        }

        @OnClick(R.id.card_view)
        public void onCellClick() {
            if (listener != null) {
                listener.onSponsorCellClick(sponsor);
            }
        }
    }
}
