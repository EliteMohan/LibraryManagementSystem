package android.com.mohan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

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
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FragmentViewReqRes extends Fragment implements ViewReqResDialog.onAcceptSuccessListener {
    private FirestoreRecyclerOptions<BookReqRecModel> firestoreRecyclerOptions;
    private ReqResListAdapter reqResListAdapter;
    private FirebaseFirestore firebaseFirestore;
    private AppCompatEditText searchField;
    private Query query;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_req_res, container, false);
        TextView headingLabel = view.findViewById(R.id.heading_label);
        ContentLoadingProgressBar progressBar = view.findViewById(R.id.progressBarSend);
        MaterialToolbar materialToolbar = view.findViewById(R.id.toolbar_user_1);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(materialToolbar);
        progressBar.setVisibility(View.GONE);
        searchField = view.findViewById(R.id.search_field);
        final String tag;
        assert getTag() != null;
        tag = getTag();
        try {
            Log.d("info", getTag());
            String substring = tag.substring(0, tag.length() - 5);
            if (tag.equals("Requested BooksAdmin")) {
                headingLabel.setText(substring);
            } else if (tag.equals("Received BooksAdmin")) {
                headingLabel.setText(substring);
            } else {
                headingLabel.setText(tag);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().log("Crash Solved at getTag()");
        }
        AppCompatImageButton searchBtn = view.findViewById(R.id.search_btn);
        recyclerView = view.findViewById(R.id.recycler_view_list_books);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("REQREC").limit(10);
        if (tag.equals("Return Dates")) {
            FirestoreRecyclerOptions<BookReqRecModel> options = new FirestoreRecyclerOptions.Builder<BookReqRecModel>()
                    .setQuery(query, new SnapshotParser<BookReqRecModel>() {
                        @NonNull
                        @Override
                        public BookReqRecModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            if(snapshot.getString("imageurl")!=null){
                                return new BookReqRecModel(String.valueOf(snapshot.get("RetDate")), snapshot.getString("Bookname"),snapshot.getString("imageurl"));
                            }
                            return new BookReqRecModel(String.valueOf(snapshot.get("RetDate")), snapshot.getString("Bookname"),"noimage");
                        }
                    })
                    .setLifecycleOwner(getViewLifecycleOwner())
                    .build();
            reqResListAdapter = new ReqResListAdapter(options, getActivity(), getTag(), progressBar);
//            reqResListAdapter.setFragmentViewReqRes(this);//passing current fragment object to set it as listener from ViewReqResDialog fragment
            recyclerView.setAdapter(reqResListAdapter);//setups recycler view to get data from firestore
        } else {
            firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<BookReqRecModel>()
                    .setQuery(query, new SnapshotParser<BookReqRecModel>() {
                        @NonNull
                        @Override
                        public BookReqRecModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            if(snapshot.getString("imageurl")!=null){
                                return new BookReqRecModel(String.valueOf(snapshot.get("Rollno")), snapshot.getString("Bookname"), snapshot.getString("RecCode"),snapshot.getString("imageurl"));
                            }
                            return new BookReqRecModel(String.valueOf(snapshot.getString("Rollno")), snapshot.getString("Bookname"), snapshot.getString("RecCode"),"noimage");
                        }
                    })
                    .setLifecycleOwner(getViewLifecycleOwner())
                    .build();
            reqResListAdapter = new ReqResListAdapter(firestoreRecyclerOptions, getActivity(), getTag(), progressBar);
            reqResListAdapter.setFragmentViewReqRes(this);//passing current fragment object to set it as listener from ViewReqResDialog fragment
            recyclerView.setAdapter(reqResListAdapter);//setups recycler view to get data from firestore
        }
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                String str = Objects.requireNonNull(searchField.getText()).toString().toUpperCase();
                query = firebaseFirestore.collection("REQREC")
                        .orderBy("Rollno", Query.Direction.ASCENDING)
                        .startAt(str.toUpperCase())
                        .endAt(str.toUpperCase() + "\uf8ff").limit(10);
                if(tag.equals("Return Dates")){
                    FirestoreRecyclerOptions<BookReqRecModel> options = new FirestoreRecyclerOptions.Builder<BookReqRecModel>()
                            .setQuery(query, new SnapshotParser<BookReqRecModel>() {
                                @NonNull
                                @Override
                                public BookReqRecModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    if(snapshot.getString("imageurl")!=null){
                                        return new BookReqRecModel(String.valueOf(snapshot.get("RetDate")), snapshot.getString("Bookname"),snapshot.getString("imageurl"));
                                    }
                                    return new BookReqRecModel(String.valueOf(snapshot.get("RetDate")), snapshot.getString("Bookname"),"noimage");
                                }
                            })
                            .setLifecycleOwner(getViewLifecycleOwner())
                            .build();
                    reqResListAdapter.updateOptions(options);
                }else {
                    firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<BookReqRecModel>()
                            .setQuery(query, new SnapshotParser<BookReqRecModel>() {
                                @NonNull
                                @Override
                                public BookReqRecModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    if(snapshot.getString("imageurl")!=null){
                                        return new BookReqRecModel(String.valueOf(snapshot.get("Rollno")), snapshot.getString("Bookname"), snapshot.getString("RecCode"),snapshot.getString("imageurl"));
                                    }
                                    return new BookReqRecModel(String.valueOf(snapshot.getString("Rollno")), snapshot.getString("Bookname"), snapshot.getString("RecCode"),"noimage");
                                }
                            })
                            .setLifecycleOwner(getViewLifecycleOwner())
                            .build();
                    reqResListAdapter.updateOptions(firestoreRecyclerOptions);
                }

            }
        });
        return view;
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    @Override
    public void onAccept() {
        reqResListAdapter.setFragmentViewReqRes(this);//passing current fragment object to set it as listener from ViewReqResDialog fragment to refresh data in recycler view
        recyclerView.setAdapter(reqResListAdapter);//refreshes or gets updated data in recycler view
    }


}
