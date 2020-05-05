package android.com.mohan;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentBooksReceived extends Fragment {
    private BookReqRecModel bookReqRecModel;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    private EditText searchField;
    private ProgressBar progressBar;
    private MaterialTextView bookName, bookReqDate, bookAuthor, bookEdition, username,rollno;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_books_recieved, container, false);
        //TextViews
        bookName = view.findViewById(R.id.book_from_firestore);
        bookAuthor = view.findViewById(R.id.bookAuthor_from_firestore);
        bookEdition = view.findViewById(R.id.bookEdition_from_firestore);
        bookReqDate = view.findViewById(R.id.date_from_firestore);
        username = view.findViewById(R.id.username_from_firestore);
        rollno = view.findViewById(R.id.userRollNo);
        //input field
        AppCompatButton searchButton = view.findViewById(R.id.Search_Button);
        AppCompatButton deleteRequest = view.findViewById(R.id.delete_book_request);
        bookReqRecModel = new BookReqRecModel();
        //searchfield
        searchField = view.findViewById(R.id.Search_RollNo);
        //firestore Instances
        firebaseFirestore = FirebaseFirestore.getInstance();
        //progress bar
        progressBar = view.findViewById(R.id.progressBarDelete);
        progressBar.setVisibility(View.GONE);
        try {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    bookReqRecModel.setRollNo(Objects.requireNonNull(searchField.getText()).toString());
                    try {
                        Log.d(TAG, bookReqRecModel.getRollNo().toUpperCase());
                        docRef = firebaseFirestore.collection("REQREC").document(bookReqRecModel.getRollNo().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    try {
                        docRef.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            DocumentSnapshot doc = task.getResult();
                                            assert doc != null;
                                            if (doc.exists() && Objects.equals(doc.get("RecCode"), "0")) {
                                                bookReqRecModel.setBookName(String.valueOf(doc.get("Bookname")));
                                                bookReqRecModel.setBookAuthor(String.valueOf(doc.get("Bookauthor")));
                                                bookReqRecModel.setBookEdition(String.valueOf(doc.get("Bookedition")));
                                                bookReqRecModel.setRollNo(String.valueOf(doc.get("Rollno")));
                                                bookReqRecModel.setUserName(String.valueOf(doc.get("Username")));
                                                bookReqRecModel.setBookReqRecDate(String.valueOf(doc.get("Bookreqrecdate")));
                                                Toast.makeText(getActivity(), "Received Book", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d(TAG, String.valueOf(doc.getData()));
                                                bookReqRecModel.setBookReqRecDate(null);
                                                bookReqRecModel.setUserName(null);
                                                bookReqRecModel.setRollNo(null);
                                                bookReqRecModel.setBookEdition(null);
                                                bookReqRecModel.setBookAuthor(null);
                                                bookReqRecModel.setBookName(null);
                                                Toast.makeText(getActivity(), "Not Received", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                        bookName.setText(bookReqRecModel.getBookName());
                                        bookEdition.setText(bookReqRecModel.getBookEdition());
                                        bookAuthor.setText(bookReqRecModel.getBookAuthor());
                                        bookReqDate.setText(bookReqRecModel.getBookReqRecDate());
                                        username.setText(bookReqRecModel.getUserName());
                                        rollno.setText(bookReqRecModel.getRollNo());
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
