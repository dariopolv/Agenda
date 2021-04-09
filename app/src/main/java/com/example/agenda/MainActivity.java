    package com.example.agenda;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentPagerAdapter;
    import androidx.viewpager.widget.ViewPager;

    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.tabs.TabLayout;
    import com.google.firebase.database.ChildEventListener;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.regex.Pattern;

    public class MainActivity extends Fragment {
        private Button next;
        private TextView today;
        private MainHelper mainHelper;
        private String currentDay;
        private ListView workListView, timeListView;
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
            next = view.findViewById(R.id.nextButton);

            mainHelper = new MainHelper();
            mainHelper.setContext(getContext());
            mainHelper.setWorkListView(workListView);
            mainHelper.setTimeListView(timeListView);

            mainHelper.currentDaySetup();
            currentDay = mainHelper.getVarDate();
            today.setText(currentDay);

            databaseReference = FirebaseDatabase.getInstance().getReference();


            workListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   mainHelper.clickToRemoveJob(i);
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

        private void goNext() {
            Intent intent = new Intent(getContext(), Test.class);
            startActivity(intent);
        }
    }


