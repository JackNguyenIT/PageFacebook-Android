package com.jack.demopagefacebook.adapters;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jack.demopagefacebook.R;
import com.jack.demopagefacebook.abstracts.AViewHolder;
import com.jack.demopagefacebook.objects.Page;
import com.jack.demopagefacebook.objects.Post;

import java.util.List;

/**
 * Created by Jack on 3/18/17.
 */

public class FacebookPostAdapter extends RecyclerView.Adapter<AViewHolder> {

    private static final int TYPE_POST_NORMAL = 0;
    private static final int TYPE_POST_VIDEO = 1;
    private static final int TYPE_POST_LINK = 2;
    private static final int TYPE_LOAD_MORE = 3;

    private List<Post> items;
    private Page page;
    private boolean loadMore = false;

    private Callback callback;

    public FacebookPostAdapter(List<Post> items) {
        this.items = items;
    }

    @Override
    public AViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_POST_VIDEO:
                return new VideoPostVH(this, parent);
            case TYPE_POST_LINK:
                return new LinkPostVH(this, parent);
            case TYPE_LOAD_MORE:
                return new LoadMoreVH(this, parent);
        }
        return new NormalPostVH(this, parent);
    }

    @Override
    public void onBindViewHolder(AViewHolder holder, int position) {
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
        Post post = items.get(position);
        if (Post.TYPE_LINK.equals(post.type))
            return TYPE_POST_LINK;
        if (Post.TYPE_VIDEO.equals(post.type))
            return TYPE_POST_VIDEO;
        return TYPE_POST_NORMAL;
    }

    private void openLink(AViewHolder holder, String link) {
        if (callback != null) {
            callback.onOpenLink(holder, link);
        }
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
        void onOpenLink(AViewHolder holder, String link);
    }

    public class PostVH extends AViewHolder {
        private SimpleDraweeView ivPageAvatar;
        private TextView tvPageName, tvPostTime;

        public PostVH(RecyclerView.Adapter<? extends AViewHolder> adapter, ViewGroup parent, @LayoutRes int layout) {
            super(adapter, parent, layout);
        }

        @Override
        protected void initUI() {
            ivPageAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_page_avatar);
            tvPageName = (TextView) itemView.findViewById(R.id.tv_page_name);
            tvPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
        }

        public void setData(Post post) {
            if (page != null) {
                ivPageAvatar.setImageURI(page.url);
                tvPageName.setText(page.name);
            }
            tvPostTime.setText(post.getTimeCreated());
        }
    }

    public class LinkPostVH extends PostVH {
        private SimpleDraweeView ivPostThumbnail;
        private TextView tvPostMessage, tvLinkTitle, tvLinkDescription, tvCaption;
        private String link;

        public LinkPostVH(RecyclerView.Adapter<? extends AViewHolder> adapter, ViewGroup parent) {
            super(adapter, parent, R.layout.list_item_post_link);
        }

        @Override
        protected void initUI() {
            super.initUI();
            ivPostThumbnail = (SimpleDraweeView) itemView.findViewById(R.id.iv_post_thumbnail);
            tvPostMessage = (TextView) itemView.findViewById(R.id.tv_post_message);
            tvLinkTitle = (TextView) itemView.findViewById(R.id.tv_post_link_title);
            tvLinkDescription = (TextView) itemView.findViewById(R.id.tv_post_link_description);
            tvCaption = (TextView) itemView.findViewById(R.id.tv_post_link_caption);
            itemView.findViewById(R.id.layout_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(LinkPostVH.this, link);
                }
            });
        }

        @Override
        public void setData(Post post) {
            super.setData(post);
            this.link = post.link;
            tvPostMessage.setText(post.message);
            if (post.full_picture != null) {
                ivPostThumbnail.setImageURI(post.full_picture);
            }
            tvCaption.setText(post.caption);
            tvLinkDescription.setText(post.description);
            tvLinkTitle.setText(post.name);
        }
    }

    public class VideoPostVH extends PostVH {
        private SimpleDraweeView ivVideoThumbnail;
        private TextView tvPostMessage;
        private String link;

        public VideoPostVH(RecyclerView.Adapter<? extends AViewHolder> adapter, ViewGroup parent) {
            super(adapter, parent, R.layout.list_item_post_video);
        }

        @Override
        protected void initUI() {
            super.initUI();
            ivVideoThumbnail = (SimpleDraweeView) itemView.findViewById(R.id.iv_video_thumbnail);
            tvPostMessage = (TextView) itemView.findViewById(R.id.tv_post_message);
            ivVideoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(VideoPostVH.this, link);
                }
            });
        }

        @Override
        public void setData(Post post) {
            super.setData(post);
            if (post.full_picture != null) {
                ivVideoThumbnail.setImageURI(post.full_picture);
            }
            tvPostMessage.setText(post.message);
            this.link = post.link;
        }
    }

    public class NormalPostVH extends PostVH {
        private TextView tvMessage;

        public NormalPostVH(RecyclerView.Adapter<? extends AViewHolder> adapter, ViewGroup parent) {
            super(adapter, parent, R.layout.list_item_post_normal);
        }

        @Override
        protected void initUI() {
            super.initUI();
            tvMessage = (TextView) itemView.findViewById(R.id.tv_post_message);
        }

        @Override
        public void setData(Post post) {
            super.setData(post);
            tvMessage.setText(post.message);
        }
    }

    public class LoadMoreVH extends AViewHolder {

        public LoadMoreVH(RecyclerView.Adapter<? extends AViewHolder> adapter, ViewGroup parent) {
            super(adapter, parent, R.layout.list_item_load_more);
        }

        @Override
        protected void initUI() {

        }
    }
}
