package android.com.mohan;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentSearchBook extends Fragment {
    private BooksModel booksModel;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    private EditText searchField;
    private ProgressBar progressBar;
    private MaterialTextView bookName, bookAbbr, bookAuthor, bookEdition, bookISBNCode, bookAvailability;
    private String ReqRecColID = "REQREC";
    private String UsersID = "users";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_book, container, false);
        //Textviews
        bookName = view.findViewById(R.id.book_from_firestore);
        bookAbbr = view.findViewById(R.id.bookAbbr_from_firestore);
        bookAuthor = view.findViewById(R.id.bookAuthor_from_firestore);
        bookEdition = view.findViewById(R.id.bookEdition_from_firestore);
        bookISBNCode = view.findViewById(R.id.bookISBN_from_firestore);
        bookAvailability = view.findViewById(R.id.bookAvail_from_firestore);
        //Buttons
        AppCompatButton searchButton = view.findViewById(R.id.Search_Book_Button);
        AppCompatButton sendRequest = view.findViewById(R.id.send_book_request);
        //searchfield
        searchField = view.findViewById(R.id.Search_Book);
        //booksmodel object creation
        booksModel = new BooksModel();
        //progress bar
        progressBar = view.findViewById(R.id.progressBarSend);
        progressBar.setVisibility(View.GONE);
        //firestore Instances
        firebaseFirestore = FirebaseFirestore.getInstance();
        //Read Data From Firestore
        try {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(searchField.getText().toString())) {
                        Toast.makeText(getActivity(), "field is empty", Toast.LENGTH_SHORT).show();
                        searchField.setError("Enter Book Abbreviation");
                        return;
                    }
                    try {
                        hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    booksModel.setBookabbr(Objects.requireNonNull(searchField.getText()).toString());
                    try {
                        docRef = firebaseFirestore.collection("Books").document(booksModel.getBookabbr().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    //Getting data from fire store server
                    try {
                        docRef.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);

                                            DocumentSnapshot doc = task.getResult();
                                            assert doc != null;
                                            if (doc.exists()) {
                                                booksModel.setBookname(String.valueOf(doc.get("Book Name")));
                                                booksModel.setBookabbr(String.valueOf(doc.get("Book Abbr")));
                                                booksModel.setBookauthor(String.valueOf(doc.get("Book Author")));
                                                booksModel.setBookedition(String.valueOf(doc.get("Book Edition")));
                                                booksModel.setBookisbncode(String.valueOf(doc.get("Book ISBN Code")));
                                                booksModel.setBookcopies(Objects.requireNonNull(doc.getLong("Copies")).intValue());
                                                Toast.makeText(getActivity(), "Book Available", Toast.LENGTH_SHORT).show();
                                            } else {
                                                try {
                                                    booksModel.setBookisbncode(null);
                                                    booksModel.setBookedition(null);
                                                    booksModel.setBookauthor(null);
                                                    booksModel.setBookname(null);
                                                    booksModel.setBookabbr(null);
                                                    booksModel.setBookcopies(0);
                                                } catch (NullPointerException e) {
                                                    Log.d("Error: ", Objects.requireNonNull(e.getMessage()));
                                                }
                                                Toast.makeText(getActivity(), "Book not found", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                        bookName.setText(booksModel.getBookname());
                                        bookAbbr.setText(booksModel.getBookabbr());
                                        bookAuthor.setText(booksModel.getBookauthor());
                                        bookEdition.setText(booksModel.getBookedition());
                                        bookISBNCode.setText(booksModel.getBookisbncode());
                                        String bookAvail;
                                        if (booksModel.getBookcopies() > 1) {
                                            bookAvail = booksModel.getBookcopies() + " Books Available";
                                        } else if (booksModel.getBookcopies() == 1) {
                                            bookAvail = booksModel.getBookcopies() + "Book Available";
                                        } else {
                                            bookAvail = "No Book Available";
                                        }
                                        bookAvailability.setText(bookAvail);
                                    }
                                });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sendRequest.setOnClickListener(new View.OnClickListener() {
                String RollNo, Username;

                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(searchField.getText().toString())) {
                        Toast.makeText(getActivity(), "field is empty", Toast.LENGTH_SHORT).show();
                        searchField.setError("Enter Book Abbreviation");
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
//                    dateStr = df.format(new Date());
                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    String userID;
                    userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
//                    String print = readData(UsersID, userID);
                    docRef = firebaseFirestore.collection("users").document(userID);
//                    Log.d("Info",print);
                    Log.d("INFO", "Working");
                    docRef = firebaseFirestore.collection(UsersID).document(userID);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                DocumentSnapshot doc = task.getResult();
                                assert doc != null;
                                if (doc.exists()) {
                                    Log.d(TAG, String.valueOf(doc.getData()));
                                    RollNo = doc.getString("rollno");
                                    Username = doc.getString("username");
                                    recursiveRequestCreator(RollNo, Username, recursionLimit);
                                }
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    private Map<String, Object> ReqRecData(String Username, String Rollno, String BookReqRecDate) {
        Map<String, Object> data = new HashMap<>();
        data.put("Username", Username);
        data.put("Rollno", Rollno);
        data.put("Bookname", booksModel.getBookname());
        data.put("Bookauthor", booksModel.getBookauthor());
        data.put("Bookedition", booksModel.getBookedition());
        data.put("Bookreqrecdate", BookReqRecDate);
        data.put("RecCode", "1");
        data.put("Copies", booksModel.getBookcopies());
        data.put("Abbr", booksModel.getBookabbr());
        return data;
    }

    private int recursionLimit = 1;

    private void recursiveRequestCreator(final String RollNo, final String Username, final int recursionLimit) {
        if (recursionLimit <= 3) {
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss z");
            docRef = firebaseFirestore.collection(ReqRecColID).document(RollNo.toUpperCase());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        assert doc != null;
                        if (doc.exists()) {
                            boolean isSameEdition = Objects.equals(doc.getString("Bookedition"), booksModel.getBookedition());
                            if (isSameEdition) {
                                //This if block logic says that same person can request a book only once
                                boolean isSameBookANDAuthor = Objects.equals(doc.getString("Bookname"), booksModel.getBookname())
                                        && Objects.equals(doc.getString("Bookauthor"), booksModel.getBookauthor());
                                if (isSameBookANDAuthor) {
                                    Toast.makeText(getActivity(), "This book(" + doc.getString("Bookname") + ") request sent already", Toast.LENGTH_SHORT).show();
                                } else {
                                    int Limit = recursionLimit;
                                    Limit++;
                                    Log.d(TAG, "book requests exists for " + RollNo);
                                    Log.d(TAG, String.valueOf(doc.getData()));
                                    // book requests creating
                                    recursiveRequestCreator(RollNo + "1", Username, Limit);
                                }

                            } else {
                                int Limit = recursionLimit;
                                Limit++;
                                Log.d(TAG, "book requests exists for " + RollNo);
                                Log.d(TAG, String.valueOf(doc.getData()));
                                // book requests creating
                                recursiveRequestCreator(RollNo + "1", Username, Limit);
                            }
                        } else {
                            if (booksModel.getBookcopies() > 0) {
                                docRef = firebaseFirestore.collection(ReqRecColID).document(RollNo.toUpperCase());
                                docRef.set(ReqRecData(Username, RollNo, sdf.format(new Date())))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Request for " + RollNo + " added successfully");
                                                Toast.makeText(getActivity(), "Request Sent Successfully for " + booksModel.getBookname(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Request for " + RollNo + " Failed");
                                    }
                                });
                            } else if (booksModel.getBookcopies() == 0) {
                                Toast.makeText(getActivity(), "Book Currently Unavailable", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Book Request Limit Reached", Toast.LENGTH_SHORT).show();
        }
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
