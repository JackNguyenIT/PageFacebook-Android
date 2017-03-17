package com.jack.demopagefacebook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jack.demopagefacebook.R;
import com.jack.demopagefacebook.objects.Page;
import com.jack.demopagefacebook.objects.Post;

import java.util.List;

/**
 * Created by Jack on 3/14/17.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_LOAD_MORE = 1;

    private Page page;
    private List<Post> items;
    private boolean loadMore = false;
    private Callback callback;


    public PostAdapter(List<Post> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_LOAD_MORE)
            return new LoadMoreVH(inflater.inflate(R.layout.list_item_load_more, parent, false));
        return new PostVH(inflater.inflate(R.layout.list_item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostVH) {
            ((PostVH) holder).setData(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return loadMore ? items.size() + 1 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (loadMore && position == items.size()) return TYPE_LOAD_MORE;
        return super.getItemViewType(position);
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onClickPostLink(PostVH holder, String link);
    }

    public class PostVH extends RecyclerView.ViewHolder {

        private SimpleDraweeView ivPageAvatar, ivPostThumbnail;
        private TextView tvPageName, tvPostTime, tvPostMessage, tvLinkTitle, tvLinkDescription, tvCaption;

        private String link;

        public PostVH(View itemView) {
            super(itemView);
            ivPageAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_page_avatar);
            ivPostThumbnail = (SimpleDraweeView) itemView.findViewById(R.id.iv_post_thumbnail);
            tvPageName = (TextView) itemView.findViewById(R.id.tv_page_name);
            tvPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            tvPostMessage = (TextView) itemView.findViewById(R.id.tv_post_message);
            tvLinkTitle = (TextView) itemView.findViewById(R.id.tv_post_link_title);
            tvLinkDescription = (TextView) itemView.findViewById(R.id.tv_post_link_description);
            tvCaption = (TextView) itemView.findViewById(R.id.tv_post_link_caption);

            itemView.findViewById(R.id.layout_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onClickPostLink(PostVH.this, link);
                    }
                }
            });
        }

        public void setData(Post post) {
            if (page != null) {
                ivPageAvatar.setImageURI(page.url);
                tvPageName.setText(page.name);
            }
            tvPostTime.setText(post.getTimeCreated());
            tvPostMessage.setText(post.message);
            ivPostThumbnail.setImageURI(post.full_picture);
            tvCaption.setText(post.caption);
            tvLinkDescription.setText(post.description);
            tvLinkTitle.setText(post.name);
            link = post.link;

        }
    }

    public class LoadMoreVH extends RecyclerView.ViewHolder {

        public LoadMoreVH(View itemView) {
            super(itemView);
        }
    }
}
