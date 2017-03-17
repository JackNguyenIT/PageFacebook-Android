package com.jack.demopagefacebook.activites;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.facebook.GraphRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jack.demopagefacebook.R;
import com.jack.demopagefacebook.adapters.PostAdapter;
import com.jack.demopagefacebook.manager.FacebookManager;
import com.jack.demopagefacebook.objects.Page;
import com.jack.demopagefacebook.objects.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 3/13/17.
 */

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;

    private PostAdapter adapter;
    private Page page;
    private List<Post> items;
    private boolean loading = false;
    private GraphRequest nextPageRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        adapter = new PostAdapter(items);
        adapter.setCallback(new PostAdapter.Callback() {
            @Override
            public void onClickPostLink(PostAdapter.PostVH holder, String link) {
                if (!TextUtils.isEmpty(link)) {
                    openWeb(link);
                }
            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        final int padding = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(padding, padding, padding, padding);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (loading || nextPageRequest == null) return;
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (totalItemCount - visibleItemCount <= firstVisibleItem) {
                    loadMore();
                    adapter.setLoadMore(true);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                }
            }

        });

        getDataPage();
    }

    private void refreshData() {
        if (page == null) {
            getDataPage();
        } else {
            refreshLayout.setRefreshing(true);
            getDataPostPage(page.id);
        }
    }

    private void getDataPage() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        FacebookManager.getPageWithUrl(new FacebookManager.Callback() {
            @Override
            protected void onSuccess(String data) {
                page = Page.pareJson(data);
                if (page != null) {
                    adapter.setPage(page);
                    getDataPostPage(page.id);
                }

            }

            @Override
            protected void onFinish() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getDataPostPage(String id) {
        FacebookManager.getDataFeedByPageId(id, new FacebookManager.Callback() {
            @Override
            protected void onSuccess(String data) {
                items.clear();
                List<Post> list = getPostFromData(data);
                if (list != null) {
                    items.addAll(list);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onError(String error) {

            }

            @Override
            protected void onNextRequest(GraphRequest nextGraphRequest) {
                nextPageRequest = nextGraphRequest;
            }

            @Override
            protected void onFinish() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMore() {
        loading = true;

        nextPageRequest.setCallback(new FacebookManager.Callback() {
            @Override
            protected void onSuccess(String data) {
                List<Post> list = getPostFromData(data);
                if (list != null) {
                    items.addAll(list);
                }
            }

            @Override
            protected void onNextRequest(GraphRequest nextGraphRequest) {
                nextPageRequest = nextGraphRequest;
            }

            @Override
            protected void onFinish() {
                loading = false;
                adapter.setLoadMore(false);
                adapter.notifyDataSetChanged();
            }

        });
        nextPageRequest.executeAsync();
    }

    private List<Post> getPostFromData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String arrayPost = jsonObject.optString("data");
            return new Gson().fromJson(arrayPost, new TypeToken<ArrayList<Post>>() {
            }.getType());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void openWeb(String link) {
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setShowTitle(true)
                .build();
        intent.launchUrl(this, Uri.parse(link));
    }


}
