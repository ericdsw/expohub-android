package com.example.ericdesedas.expohub.presentation.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>{

    private ImageDownloader imageDownloader;
    private News[] newses;
    private Listener listener;

    /**
     * Constructor
     *
     * @param imageDownloader the {@link ImageDownloader} reference to present the news' image
     */
    public NewsListAdapter(ImageDownloader imageDownloader) {
        this.imageDownloader    = imageDownloader;
        this.newses             = new News[] {};
    }

    // Adapter Methods

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.updateNews(newses[position]);
    }

    @Override
    public int getItemCount() {
        return newses.length;
    }

    // Public Methods

    public void updateList(News[] newses) {
        this.newses = newses;
    }

    public boolean hasNews() {
        return newses.length > 0;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    // Internal Definitions

    public interface Listener {
        void onNewsCardClick(News news);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_image)          ImageView newsImage;
        @BindView(R.id.news_title)          TextView newsTitle;
        @BindView(R.id.news_creation_date)  TextView newsCreationDate;

        private News news;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void updateNews(News news) {

            this.news = news;

            newsTitle.setText(this.news.title);
            newsCreationDate.setText(this.news.formattedCreatedAt());

            imageDownloader.setMaxImageSize(500)
                    .setImage(this.news.getImage(), newsImage);
        }

        @OnClick(R.id.root_view)
        public void onCardClick() {
            if (listener != null) {
                listener.onNewsCardClick(news);
            }
        }
    }
}
