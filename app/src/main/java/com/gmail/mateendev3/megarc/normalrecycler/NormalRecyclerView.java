package com.gmail.mateendev3.megarc.normalrecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.mateendev3.megarc.MainActivity;
import com.gmail.mateendev3.megarc.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NormalRecyclerView extends AppCompatActivity {
    RecyclerView rcNormal;
    List<Car> mCarList;
    CarAdapter mCarAdapter;
    ConstraintLayout normalRootLayout;


    Car deletedObject;
    List<Car> mArchivedCarList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_recycler_view);

        //finding views
        rcNormal = findViewById(R.id.rc_normal);
        normalRootLayout = findViewById(R.id.normal_activity_root);

        //getting data
        generateData();

        //creating adapter
        mCarAdapter = new CarAdapter(mCarList);

        //setting OnItemLongClickListener to item
        mCarAdapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(NormalRecyclerView.this, "you choose: " + mCarList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        //setting onItemTextViewClickListener to item
        mCarAdapter.setOnItemTextViewClickListener(new CarAdapter.OnItemTextViewClickListener() {
            @Override
            public void onItemTextViewClick(TextView textView, int position) {
                Car car = mCarList.get(position);
                textView.setText(car.getTitle());
            }
        });

        //TODO: creating swiping items
        //Todo: creating ItemTouchHelper
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(mCarList, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(NormalRecyclerView.this, R.color.green))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_archive_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(NormalRecyclerView.this, R.color.red))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        if (mArchivedCarList == null) {
                            mArchivedCarList = new ArrayList<>();
                        }
                        deletedObject = mCarList.get(position);
                        mArchivedCarList.add(deletedObject);
                        mCarList.remove(position);
                        mCarAdapter.notifyItemRemoved(position);
                        Snackbar.make(normalRootLayout, "Item successfully archived.", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mCarList.add(position, deletedObject);
                                        mCarAdapter.notifyItemInserted(position);
                                        mArchivedCarList.remove(mArchivedCarList.lastIndexOf(deletedObject));
                                        deletedObject = null;
                                    }
                                })
                                .show();
                        break;

                    case ItemTouchHelper.RIGHT:
                        deletedObject = mCarList.get(position);
                        mCarList.remove(position);
                        mCarAdapter.notifyItemRemoved(position);
                        Snackbar.make(normalRootLayout, "Item successfully deleted.", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mCarList.add(position, deletedObject);
                                        mCarAdapter.notifyItemInserted(position);
                                        deletedObject = null;
                                    }
                                })
                                .show();
                        break;
                }
            }
        });
        helper.attachToRecyclerView(rcNormal);


        //setting onItemLongClickListener to item
        mCarAdapter.setOnItemLongClickListener(new CarAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(NormalRecyclerView.this)
                        .setTitle("Deletion")
                        .setMessage("Do you want to delete item?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletedObject = mCarList.get(position);
                                mCarList.remove(position);
                                mCarAdapter.notifyItemRemoved(position);
                                Snackbar.make(normalRootLayout, "Item successfully deleted.", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mCarList.add(position, deletedObject);
                                                mCarAdapter.notifyItemInserted(position);
                                                deletedObject = null;
                                            }
                                        })
                                        .show();
                            }
                        })
                        .create();
                dialog.show();
            }
        });


        //setting functionality to RecyclerView
        rcNormal.setLayoutManager(new LinearLayoutManager(this));
        rcNormal.setAdapter(mCarAdapter);
        rcNormal.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }


    //method to generate data (List<Car>)
    public void generateData() {
        mCarList = new ArrayList<>();
        mCarList.add(new Car("Civic", "Rs. 32,99,000"));
        mCarList.add(new Car("Corolla", "Rs. 32,99,000"));
        mCarList.add(new Car("Mehran", "Rs. 32,99,000"));
        mCarList.add(new Car("City", "Rs. 32,99,000"));
        mCarList.add(new Car("Alto", "Rs. 32,99,000"));
        mCarList.add(new Car("Cultus", "Rs. 32,99,000"));
        mCarList.add(new Car("Ford", "Rs. 32,99,000"));
    }
}