package android.com.mohan.Modules.AdminModule.FragmentRecyclerViewComponents;

import android.com.mohan.Models.UsersModel;
import android.com.mohan.Modules.AdminModule.AdapterComponents.ViewAllUsersAdapter;
import android.com.mohan.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FragmentRecyclerViewAllUsers extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private AppCompatEditText searchField;
    private Query query;
    private ViewAllUsersAdapter viewAllUsersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_users, container, false);

        ContentLoadingProgressBar progressBar = view.findViewById(R.id.progressBarSend);
        MaterialToolbar materialToolbar = view.findViewById(R.id.toolbar_user_1);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(materialToolbar);
        progressBar.setVisibility(View.GONE);
        searchField = view.findViewById(R.id.search_field);
        AppCompatImageButton searchBtn = view.findViewById(R.id.search_btn);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_list_users);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("users").limit(10);
        FirestoreRecyclerOptions<UsersModel> options = new FirestoreRecyclerOptions.Builder<UsersModel>()
                .setQuery(query, new SnapshotParser<UsersModel>() {
                    @NonNull
                    @Override
                    public UsersModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        return new UsersModel(snapshot.getString("username"), snapshot.getString("rollno"));
                    }
                }).setLifecycleOwner(getViewLifecycleOwner())
                .build();
        viewAllUsersAdapter = new ViewAllUsersAdapter(options,getActivity(),progressBar);
        recyclerView.setAdapter(viewAllUsersAdapter);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = Objects.requireNonNull(searchField.getText()).toString().toUpperCase();
                query = firebaseFirestore.collection("users")
                        .orderBy("rollno", Query.Direction.ASCENDING)
                        .startAt(str.toUpperCase())
                        .endAt(str.toUpperCase() + "\uf8ff").limit(10);
                FirestoreRecyclerOptions<UsersModel> options1 = new FirestoreRecyclerOptions.Builder<UsersModel>()
                        .setQuery(query, new SnapshotParser<UsersModel>() {
                            @NonNull
                            @Override
                            public UsersModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                return new UsersModel(snapshot.getString("username"),snapshot.getString("rollno"));
                            }
                        })
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .build();
                viewAllUsersAdapter.updateOptions(options1);
            }
        });
        return view;
    }
}
