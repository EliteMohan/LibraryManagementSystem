package android.com.mohan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FragmentViewAllBooks extends Fragment {
    private FirestoreRecyclerOptions<BooksModel> booksOptions;
    private BookListAdapter bookListAdapter;
    private FirebaseFirestore firebaseFirestore;
    private AppCompatEditText searchField;
    private Query query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_books, container, false);
        searchField = view.findViewById(R.id.search_field);
        AppCompatImageButton searchBtn = view.findViewById(R.id.search_btn);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_list_books);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("Books").limit(5);
        booksOptions = new FirestoreRecyclerOptions.Builder<BooksModel>()
                .setQuery(query, new SnapshotParser<BooksModel>() {
                    @NonNull
                    @Override
                    public BooksModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        return new BooksModel(String.valueOf(snapshot.getString("Book Name")), String.valueOf(snapshot.get("Book Author")));
                    }
                })
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        bookListAdapter = new BookListAdapter(booksOptions,getActivity());
        bookListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(bookListAdapter);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = Objects.requireNonNull(searchField.getText()).toString();
                query = firebaseFirestore.collection("Books")
                        .orderBy("Book Name", Query.Direction.ASCENDING)
                        .startAt(str.toUpperCase())
                        .endAt(str.toUpperCase() + "\uf8ff").limit(5);
                booksOptions = new FirestoreRecyclerOptions.Builder<BooksModel>()
                        .setQuery(query, new SnapshotParser<BooksModel>() {
                            @NonNull
                            @Override
                            public BooksModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                return new BooksModel(String.valueOf(snapshot.getString("Book Name")), String.valueOf(snapshot.get("Book Author")));
                            }
                        })
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .build();
                bookListAdapter.updateOptions(booksOptions);
                bookListAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

}
