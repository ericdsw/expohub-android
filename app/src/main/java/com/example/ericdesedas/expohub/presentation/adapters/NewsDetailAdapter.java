package com.example.ericdesedas.expohub.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Comment;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_NEWS_DETAIL     = 0;
    public static final int VIEW_TYPE_NEWS_COMMENT    = 1;

    private ImageDownloader imageDownloader;
    private News news;

    public NewsDetailAdapter(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NEWS_DETAIL:
                View newsDetailView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sbv_cell_news_detail, parent, false);
                return new NewsDetailViewHolder(newsDetailView);
            case VIEW_TYPE_NEWS_COMMENT:
                View newsCommentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sbv_cell_news_comments, parent, false);
                return new CommentsViewHolder(newsCommentView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {
            ((NewsDetailViewHolder) holder).updateNews(news);
        } else {
            ((CommentsViewHolder) holder).updateComment(news.getComments().get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        if (news != null) {
            return news.getComments().size() + 1;
        } else {
            return 0;
        }
    }

    public void setNews(News news) {
        this.news = news;
    }

    public class NewsDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_image)          ImageView newsImage;
        @BindView(R.id.news_title)          TextView newsTitle;
        @BindView(R.id.news_creation_date)  TextView newsCreationDate;
        @BindView(R.id.news_content)        TextView newsContent;

        private News news;

        public NewsDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void updateNews(News news) {
            this.news = news;

            imageDownloader.setMaxImageSize(500)
                    .setImage(news.getImage(), newsImage);

            newsTitle.setText(news.title);
            newsCreationDate.setText(news.formattedCreatedAt());
            newsContent.setText(news.content);
        }
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_text) TextView commentText;

        private Comment comment;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void updateComment(Comment comment) {
            this.comment = comment;
            commentText.setText(comment.text);
        }
    }
}
