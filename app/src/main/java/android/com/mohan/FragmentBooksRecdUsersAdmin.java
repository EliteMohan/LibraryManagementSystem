package android.com.mohan;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentBooksRecdUsersAdmin extends Fragment {
    private BookReqRecModel bookReqRecModel;
    private BooksModel booksModel;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    private EditText searchField;
    private ProgressBar progressBar;
    private MaterialTextView bookName, bookReqDate, bookAuthor, bookEdition, username,rollno, retDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_rec_users_admin, container, false);
        //TextViews
        bookName = view.findViewById(R.id.book_from_firestore);
        bookAuthor = view.findViewById(R.id.bookAuthor_from_firestore);
        bookEdition = view.findViewById(R.id.bookEdition_from_firestore);
        bookReqDate = view.findViewById(R.id.date_from_firestore);
        username = view.findViewById(R.id.username_from_firestore);
        rollno = view.findViewById(R.id.userRollNo);
        retDate = view.findViewById(R.id.return_date_from_firestore);
        //input field
        AppCompatButton searchButton = view.findViewById(R.id.Search_Button);
        AppCompatButton receive = view.findViewById(R.id.receive_book_request);

        bookReqRecModel = new BookReqRecModel();
        booksModel = new BooksModel();

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
                    if(TextUtils.isEmpty(searchField.getText().toString())){
                        Toast.makeText(getActivity(),"field is empty",Toast.LENGTH_SHORT).show();
                        searchField.setError("Enter Your Roll No");
                        return;
                    }
                    try {
                        hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    bookReqRecModel.setRollNo(Objects.requireNonNull(searchField.getText()).toString());
                    try {
                        Log.d(TAG,bookReqRecModel.getRollNo().toUpperCase());
                        docRef = firebaseFirestore.collection("REQREC").document(bookReqRecModel.getRollNo().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    try {
                        docRef.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            progressBar.setVisibility(View.GONE);
                                            DocumentSnapshot doc = task.getResult();
                                            assert doc != null;
                                            if(doc.exists() && Objects.equals(doc.get("RecCode"), "0")){
                                                bookReqRecModel.setBookName(String.valueOf(doc.get("Bookname")));
                                                bookReqRecModel.setBookAuthor(String.valueOf(doc.get("Bookauthor")));
                                                bookReqRecModel.setBookEdition(String.valueOf(doc.get("Bookedition")));
                                                bookReqRecModel.setRollNo(String.valueOf(doc.get("Rollno")));
                                                bookReqRecModel.setUserName(String.valueOf(doc.get("Username")));
                                                bookReqRecModel.setBookReqRecDate(String.valueOf(doc.get("Bookreqrecdate")));
                                                bookReqRecModel.setBookAbbr(String.valueOf(doc.get("Abbr")));
                                                bookReqRecModel.setRetDate(String.valueOf(doc.get("RetDate")));
                                                Toast.makeText(getActivity(),"Received Book",Toast.LENGTH_SHORT).show();
                                                firebaseFirestore.collection("Books")
                                                        .document(bookReqRecModel.getBookAbbr())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(task.isSuccessful()){
                                                                    DocumentSnapshot doc = task.getResult();
                                                                    assert doc != null;
                                                                    if(doc.exists()){
                                                                        booksModel.setBookcopies(Objects.requireNonNull(doc.getLong("Copies")).intValue());
                                                                    }
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Log.d(TAG, String.valueOf(doc.getData()));
                                                bookReqRecModel.setBookReqRecDate(null);
                                                bookReqRecModel.setUserName(null);
                                                bookReqRecModel.setRollNo(null);
                                                bookReqRecModel.setBookEdition(null);
                                                bookReqRecModel.setBookAuthor(null);
                                                bookReqRecModel.setBookName(null);
                                                bookReqRecModel.setRetDate(null);
                                                Toast.makeText(getActivity(), "Not Received", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(),"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                        bookName.setText(bookReqRecModel.getBookName());
                                        bookEdition.setText(bookReqRecModel.getBookEdition());
                                        bookAuthor.setText(bookReqRecModel.getBookAuthor());
                                        bookReqDate.setText(bookReqRecModel.getBookReqRecDate());
                                        username.setText(bookReqRecModel.getUserName());
                                        rollno.setText(bookReqRecModel.getRollNo());
                                        retDate.setText(bookReqRecModel.getRetDate());
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
        try {
            receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(searchField.getText().toString())){
                        Toast.makeText(getActivity(),"field is empty",Toast.LENGTH_SHORT).show();
                        searchField.setError("Enter Your Roll No");
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseFirestore.collection("REQREC").document(bookReqRecModel.getRollNo().toUpperCase())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(),"Book Received",Toast.LENGTH_SHORT).show();
                                    firebaseFirestore.collection("Books")
                                            .document(bookReqRecModel.getBookAbbr())
                                            .update("Copies",booksModel.getBookcopies()+1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG,"request code of book updated");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG,"Error updating Book");
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(),"Delete operation Failed",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } catch (Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
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
