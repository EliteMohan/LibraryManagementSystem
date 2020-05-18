package android.com.mohan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentBooksReturnDates extends Fragment {
    private FirestoreRecyclerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_return_dates_books, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list_ret_dates);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        //Query
        Query query = firebaseFirestore.collection("Books").orderBy("Copies",Query.Direction.ASCENDING);
        //RecyclerOptions
        FirestoreRecyclerOptions<BooksModel> options = new FirestoreRecyclerOptions.Builder<BooksModel>()
                .setQuery(query, new SnapshotParser<BooksModel>() {
                    @NonNull
                    @Override
                    public BooksModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        return new BooksModel(String.valueOf(snapshot.get("Book Name")),String.valueOf(snapshot.get("Book Author")));
                    }
                })
                .build();

        adapter = new FirestoreRecyclerAdapter<BooksModel, RetViewHolder>(options) {
            @NonNull
            @Override
            public RetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_book, parent, false);
                return new RetViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RetViewHolder holder, int position, @NonNull BooksModel model) {
                Log.d("Data",model.getBookname() + " "+model.getBookauthor());
                holder.bookName.setText(model.getBookname());
                holder.BookRetDate.setText(model.getBookauthor());
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        return view;
    }

    private static class RetViewHolder extends RecyclerView.ViewHolder{
        TextView bookName,BookRetDate;
        RetViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name_1);
            BookRetDate = itemView.findViewById(R.id.book_author_1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        Log.d("Checking",adapter.getItemCount()+"");
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
