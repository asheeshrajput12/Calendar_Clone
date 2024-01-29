package com.personalproject.calendarclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private boolean isToolbarVisible = true;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar=findViewById(R.id.toolbar);
        appBarLayout=findViewById(R.id.app_bar_layout);
        collapsingToolbar=findViewById(R.id.collapsing_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation item clicks here
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
        // Assuming you have a List<ItemModel> itemList with 50 items
        List<ItemModel> itemList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            itemList.add(new ItemModel("Item " + i));
        }


// Find RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

// Set up RecyclerView with LinearLayoutManager
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = 0;
                    // Assuming you want to load more items when there are 5 or fewer items left to be visible
                    int threshold = 5;

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - threshold) {
                        // Load more data (replace this with your actual logic)
                    }

                }
            }
        });

// Create and set the adapter
        MyAdapter adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);
        // Add offset changed listener to control collapsing behavior
        addOffsetChangedListener();
    }
    private void addOffsetChangedListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int range = appBarLayout.getTotalScrollRange();

                // Calculate the fraction of toolbar visibility
                float fraction = (float) (range + verticalOffset) / range;

                // You can adjust the threshold as needed
                float threshold = 0.5f;

                if (fraction <= threshold && isToolbarVisible) {
                    // Toolbar is fully collapsed, hide it
                    hideToolbar();
                } else if (fraction > threshold && !isToolbarVisible) {
                    // Toolbar is expanded, show it
                    showToolbar();
                }
            }
        });
    }

    private void hideToolbar() {
        // Hide the toolbar programmatically
        isToolbarVisible = false;
        collapsingToolbar.setTitleEnabled(false); // Disable title to prevent overlap with the CalendarView
        appBarLayout.setExpanded(false, true);
    }

    private void showToolbar() {
        // Show the toolbar programmatically
        isToolbarVisible = true;
        collapsingToolbar.setTitleEnabled(true); // Enable title
        appBarLayout.setExpanded(true, true);
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<ItemModel> itemList;

        public MyAdapter(List<ItemModel> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemModel item = itemList.get(position);
            Log.d("onBindViewHolder", "onBindViewHolder position is: " + position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView itemNameTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            }

            public void bind(ItemModel item) {
                itemNameTextView.setText(item.getItemName());
            }
        }
    }
    public class ItemModel {
        private String itemName;

        public ItemModel(String itemName) {
            this.itemName = itemName;
        }

        public String getItemName() {
            return itemName;
        }
    }
}