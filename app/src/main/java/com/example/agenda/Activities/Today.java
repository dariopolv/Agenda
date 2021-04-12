    package com.example.agenda.Activities;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.Switch;
    import android.widget.TextView;

    import com.example.agenda.Activities.InsertWork;
    import com.example.agenda.Activities.ModifyWork;
    import com.example.agenda.Helper.MainHelper;
    import com.example.agenda.R;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class Today extends Fragment {
        private Button next;
        private TextView today;
        private MainHelper mainHelper;
        private String currentDay;
        private ListView workListView, timeListView;
        private Switch modifyMode;
        private TextView modifyModeStatus;
        DatabaseReference databaseReference;



        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity, container, false);
            today = view.findViewById(R.id.todayTextView);
            workListView = view.findViewById(R.id.workListView);
            timeListView = view.findViewById(R.id.timeListView);
            modifyMode = view.findViewById(R.id.switchToModifyMode);
            modifyModeStatus = view.findViewById(R.id.modifyModeStatus);
            next = view.findViewById(R.id.nextButton);
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mainHelper = new MainHelper();
            mainHelper.setContext(getContext());
            mainHelper.setWorkListView(workListView);
            mainHelper.setTimeListView(timeListView);

            mainHelper.currentDaySetup();
            currentDay = mainHelper.getVarDate();
            today.setText(currentDay);

            modifyMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (modifyMode.isChecked()) {
                        mainHelper.setModifyMode();
                        modifyModeStatus.setText("ON");
                    } else {
                        mainHelper.exitModifyMode();
                        modifyModeStatus.setText("OFF");
                    }
                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("agenda");

            workListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mainHelper.getSetModifyMode() == false) {
                        new AlertDialog.Builder(getContext()).setTitle("Note").setMessage(mainHelper.getNoteList().get(i)).show();
                    }
                    else {
                        goToModifyWork(i);
                    }
                }
            });

            workListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                    new AlertDialog.Builder(getContext()).setMessage("Eliminare definitivamente?").setNegativeButton("No", null)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mainHelper.clickToRemoveJob(position);
                        }
                    }).show();



                    return true;
                }
            });

            mainHelper.deletePreviousAgenda();

            databaseReference.child(currentDay).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mainHelper.listLoading(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goNext();
                }
            });
            return view;
        }

        private void goToModifyWork(int index) {
            Intent intent = new Intent(getContext(), ModifyWork.class);
            mainHelper.goToModifyWork(index,currentDay, intent);
            startActivity(intent);
        }

        private void goNext() {
            Intent intent = new Intent(getContext(), InsertWork.class);
            startActivity(intent);
        }
    }


