package android.com.mohan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.com.mohan.Models.BooksModel;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ViewBookDialog extends AppCompatDialogFragment {
    private Context context;
    private String abbr;
    private String bookname, bookauthor, tag;
    private DocumentReference docRef;
    private AppCompatTextView bookCopies, bookEdition, bookISBN;
    private AlertDialog.Builder alertDialog;
    private FirebaseFirestore firebaseFirestore;
    private BooksModel booksModel;
    private String ReqRecColID = "REQREC";
    private ContentLoadingProgressBar progressBar;

    ViewBookDialog(Context context, String abbr, String bookname, String bookauthor, String tag, ContentLoadingProgressBar progressBar) {
        this.context = context;
        this.abbr = abbr;
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.tag = tag;
        this.progressBar = progressBar;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.layout_book_dialog, null);

        final AppCompatTextView bookName = view.findViewById(R.id.add_book_dialog);
        bookCopies = view.findViewById(R.id.book_avail_dialog);
        final AppCompatTextView bookAbbr = view.findViewById(R.id.book_abbrev_dialog);
        final AppCompatTextView bookAuthor = view.findViewById(R.id.book_author_dialog);
        bookEdition = view.findViewById(R.id.book_edition_dialog);
        bookISBN = view.findViewById(R.id.book_isbn_dialog);

        booksModel = new BooksModel();
//        String[] AbbrExtract1 = new String[0];
//        try {
//            AbbrExtract1 = bookauthor.split(" ");
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        final StringBuilder Abbr = new StringBuilder();
//        for (String str : AbbrExtract1) {
//            char Char = str.charAt(0);//takes 1st letter of each word
//            Abbr.append(Char);//appends 1st letter at end
//        }
        String Abbr = "Abbreviation: " + abbr;
        booksModel.setBookabbr(String.valueOf(abbr));
        booksModel.setBookname(bookname);
        booksModel.setBookauthor(bookauthor);

        bookAbbr.setText(Abbr);
        bookName.setText(bookname);
        bookAuthor.setText(bookauthor);
        Log.d("DialogInfo", tag);
        firebaseFirestore = FirebaseFirestore.getInstance();
        try {
            firebaseFirestore.collection("Books").document(String.valueOf(abbr).toUpperCase())
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                assert doc != null;
                                if(doc.exists()){
                                    booksModel.setBookedition(String.valueOf(doc.get("Book Edition")));
                                }
                            }
                        }
                    });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        switch (tag) {
            case "All Books":
            case "Remaining Books":
                docRef = firebaseFirestore.collection("Books").document(String.valueOf(abbr));
                GetBookFromFirestore();
                ViewBookAlertDialog(view);
                break;
            case "Search Book":
                docRef = firebaseFirestore.collection("Books").document(String.valueOf(abbr));
                GetBookFromFirestore();
                SendBookDialog(view);
                break;
        }

        return alertDialog.create();
    }

    private void SendBookDialog(View view) {
        alertDialog.setTitle("Book");
        alertDialog.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);

                        FirebaseAuth fAuth = FirebaseAuth.getInstance();
                        String userID;
                        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                        docRef = firebaseFirestore.collection("users").document(userID);
                        Log.d("INFO", "Working");
                        docRef = firebaseFirestore.collection("users").document(userID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
//                                    progressBar.setVisibility(View.GONE);
                                    DocumentSnapshot doc = task.getResult();
                                    assert doc != null;
                                    if (doc.exists()) {
                                        String RollNo = doc.getString("rollno");
                                        String Username = doc.getString("username");
                                        recursiveRequestCreator(RollNo, Username, recursionLimit,RollNo);
                                    }
                                }
                            }
                        });
                    }
                });
    }

    private void ViewBookAlertDialog(View view) {
        alertDialog.setTitle("Book");
        alertDialog.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

    }


    private void GetBookFromFirestore() {
        try {
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {

                    assert doc != null;
                    boolean isDocExists = false;
                    try {
                        isDocExists = doc.exists();
                    } catch (NullPointerException ex) {
                        FirebaseCrashlytics.getInstance().log(Objects.requireNonNull(ex.getMessage()));
                        ex.printStackTrace();
                    }

                    if (isDocExists) {
                        String copies = null;
                        try {
                            if(tag.equals("All Books")){
                                copies = "Copies: " + Objects.requireNonNull(doc.getLong("Total Copies")).intValue();
                            }else {
                                copies = "Copies: " + Objects.requireNonNull(doc.getLong("Copies")).intValue();
                            }

                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }

                        booksModel.setBookcopies(Objects.requireNonNull(doc.getLong("Copies")).intValue());
                        bookCopies.setText(copies);
                        String edition = "Edition: " + doc.getString("Book Edition");
                        bookEdition.setText(edition);
                        String isbn = "ISBN Code: " + doc.getString("Book ISBN Code");
                        bookISBN.setText(isbn);
                        booksModel.setImageurl(doc.getString("imageurl"));
                    } else {
                        try {
                            Toast.makeText(getActivity(), "No resource found", Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException ex) {
                            FirebaseCrashlytics.getInstance().log("Toast issue cleared " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private Map<String, Object> ReqRecData(String Username, String Rollno, String BookReqRecDate) {
        Map<String, Object> data = new HashMap<>();
        data.put("Username", Username);
        data.put("Rollno", Rollno.toUpperCase());
        data.put("Bookname", booksModel.getBookname());
        data.put("Bookauthor", booksModel.getBookauthor());
        data.put("Bookedition", booksModel.getBookedition());
        data.put("Bookreqrecdate", BookReqRecDate);
        data.put("RecCode", "1");
        data.put("Copies", booksModel.getBookcopies());
        data.put("Abbr", booksModel.getBookabbr());
        data.put("imageurl",booksModel.getImageurl());
        return data;
    }

    private int recursionLimit = 1;

    private void recursiveRequestCreator(final String RollNo, final String Username, final int recursionLimit, final String rollNo) {
        if (recursionLimit <= 3) {
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(context, "This book(" + doc.getString("Bookname") + ") request sent already", Toast.LENGTH_SHORT).show();
                                } else {
                                    int Limit = recursionLimit;
                                    Limit++;
                                    Log.d(TAG, "book requests exists for " + RollNo);
                                    Log.d(TAG, String.valueOf(doc.getData()));

                                    // book requests creating
                                    recursiveRequestCreator(RollNo + "1", Username, Limit, rollNo);
                                }

                            } else {
                                int Limit = recursionLimit;
                                Limit++;
                                // book requests creating
                                recursiveRequestCreator(RollNo + "1", Username, Limit, rollNo);
                            }
                        } else {
                            if (booksModel.getBookcopies() > 0) {
                                docRef = firebaseFirestore.collection(ReqRecColID).document(RollNo.toUpperCase());
                                docRef.set(ReqRecData(Username, rollNo, sdf.format(new Date())))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Request for " + RollNo + " added successfully");
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(context, "Request Sent Successfully for " + booksModel.getBookname(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Request for " + RollNo + " Failed");
                                    }
                                });
                            } else if (booksModel.getBookcopies() == 0) {
                                try {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(context, "Book Currently Unavailable", Toast.LENGTH_SHORT).show();
                                } catch (NullPointerException e) {
                                    FirebaseCrashlytics.getInstance().log(Objects.requireNonNull(e.getMessage()));
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Book Request Limit Reached", Toast.LENGTH_SHORT).show();
        }
    }
}
