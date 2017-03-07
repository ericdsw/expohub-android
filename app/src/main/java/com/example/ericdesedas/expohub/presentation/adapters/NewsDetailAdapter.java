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

    private static final int VIEW_TYPE_NEWS_DETAIL      = 0;
    private static final int VIEW_TYPE_NEWS_COMMENT     = 1;
    private static final int VIEW_TYPE_NEWS_NO_COMMENT  = 2;

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
            case VIEW_TYPE_NEWS_NO_COMMENT:
                View newsNoCommentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sbv_cell_news_no_comments, parent, false);
                return new NoCommentsViewHolder(newsNoCommentView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {
            ((NewsDetailViewHolder) holder).updateNews(news);
        } else {
            if (news.getComments().size() <= 0) {
                // Nothing
            } else {
                ((CommentsViewHolder) holder).updateComment(news.getComments().get(position - 1));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (news != null) {
            if (news.getComments().size() <= 0) {
                return 2;
            } else {
                return news.getComments().size() + 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_NEWS_DETAIL;
        } else {
            if (news.getComments().size() <= 0) {
                return VIEW_TYPE_NEWS_NO_COMMENT;
            } else {
                return VIEW_TYPE_NEWS_COMMENT;
            }
        }
    }

    public void setNews(News news) {
        this.news = news;
    }

    class NewsDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_image)          ImageView newsImage;
        @BindView(R.id.news_title)          TextView newsTitle;
        @BindView(R.id.news_creation_date)  TextView newsCreationDate;
        @BindView(R.id.news_content)        TextView newsContent;

        private News news;

        NewsDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void updateNews(News news) {
            this.news = news;

            imageDownloader.setMaxImageSize(500)
                    .setImage(this.news.getImage(), newsImage);

            newsTitle.setText(this.news.title);
            newsCreationDate.setText(this.news.formattedCreatedAt());
            newsContent.setText(this.news.content);
        }
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_text)        TextView commentText;
        @BindView(R.id.comment_user_name)   TextView commentUserNameText;

        private Comment comment;

        CommentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void updateComment(Comment comment) {
            this.comment = comment;
            commentText.setText(this.comment.text);
            commentUserNameText.setText(this.comment.getUser().name);
        }
    }

    class NoCommentsViewHolder extends RecyclerView.ViewHolder {

        public NoCommentsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
