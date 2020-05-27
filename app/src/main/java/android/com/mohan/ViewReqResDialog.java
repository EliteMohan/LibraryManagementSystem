package android.com.mohan;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ViewReqResDialog extends DialogFragment {
    private Context context;
    private String tag;
    private ContentLoadingProgressBar progressBar;
    private AlertDialog.Builder alertDialog;
    private AppCompatTextView bookname, bookauthor, bookedition, reqresdate, username, rollno;
    private String docid, bookName, abbr;
    private DocumentReference docRef;
    private FirebaseFirestore firebaseFirestore;
    private BookReqRecModel bookReqRecModel;

    ViewReqResDialog(Context context, String tag, ContentLoadingProgressBar progressBar, String id) {
        this.context = context;
        this.tag = tag;
        this.progressBar = progressBar;
        this.docid = id;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        alertDialog = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.layout_reqres_dialog, null);

        bookname = view.findViewById(R.id.add_book_dialog);
        bookedition = view.findViewById(R.id.book_edition_dialog);
        bookauthor = view.findViewById(R.id.book_author_dialog);
        reqresdate = view.findViewById(R.id.book_reqresdate_dialog);
        username = view.findViewById(R.id.book_username_dialog);
        rollno = view.findViewById(R.id.book_rollno_dialog);

        bookReqRecModel = new BookReqRecModel();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String reqRecColID = "REQREC";
        docRef = firebaseFirestore.collection(reqRecColID).document(docid);
        String recdate = "Received Date: ";
        String reqdate = "Requested Date: ";
        String retdate = "Return Dates: ";
        assert getTag() != null;
        Log.d("Date",getTag());
        switch (tag) {
            case "Return Dates":
                //Works Perfectly
                GetREQRESFromFirestore(retdate);
                viewreceived(view);
                break;
            case "Requested Books":
                //Works Perfectly
                GetREQRESFromFirestore(reqdate);
                viewrequested(view);
                break;
            case "Received Books":
                //Works Perfectly
                GetREQRESFromFirestore(recdate);
                viewreceived(view);
                break;
            case "Requested BooksAdmin":
                //Works Perfectly
                GetREQRESFromFirestore(reqdate);
                viewrequestedadmin(view);
                break;
            case "Received BooksAdmin":
                //Works Perfectly
                GetREQRESFromFirestore(recdate);
                viewreceivedadmin(view);
                break;
        }
        return alertDialog.create();
    }

    private void viewreceivedadmin(View view) {
        alertDialog.setTitle("Received Books");
        alertDialog.setView(view)
                .setPositiveButton("Receive", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            docRef.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            try {
                                                firebaseFirestore.collection("Books")
                                                        .document(abbr)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot doc = task.getResult();
                                                                    assert doc != null;
                                                                    if (doc.exists()) {
                                                                        Log.d("Book Copies & Abbr", Objects.requireNonNull(doc.getLong("Copies")).intValue() +" "+abbr);
                                                                        firebaseFirestore.collection("Books")
                                                                                .document(abbr)
                                                                                .update("Copies", Objects.requireNonNull(doc.getLong("Copies")).intValue() + 1)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                        Toast.makeText(context, "Book Received", Toast.LENGTH_SHORT).show();
                                                                                        Log.d(TAG, "request code of book updated");
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                        Log.d(TAG, "Error updating Book");
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            }
                                                        });
                                            } catch (NullPointerException e) {
                                                progressBar.setVisibility(View.GONE);
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(context, "Delete operation Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } catch (NullPointerException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public interface onAcceptSuccessListener {
        void onAccept();
    }

    private onAcceptSuccessListener acceptSuccessListener;

    void setOnAcceptListener(onAcceptSuccessListener acceptSuccessListener) {
        this.acceptSuccessListener = acceptSuccessListener;
    }

    private void viewrequestedadmin(View view) {
        alertDialog.setTitle("Requested Books");
        alertDialog.setView(view)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar calendar = Calendar.getInstance();
                        try {
                            calendar.setTime(Objects.requireNonNull(sdf.parse(String.valueOf(new Date()))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        calendar.add(Calendar.DATE, 7);
                        try {
                            docRef.update("RecCode", "0", "Bookreqrecdate", sdf.format(new Date()), "RetDate", sdf1.format(calendar.getTime()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            try {
                                                Log.d("Copies", bookReqRecModel.getCopies() + "");
                                                firebaseFirestore.collection("Books")
                                                        .document(bookReqRecModel.getBookabbr())
                                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot doc = task.getResult();
                                                            assert doc != null;
                                                            if (doc.exists()) {
                                                                int copies = Objects.requireNonNull(doc.getLong("Copies")).intValue();
                                                                if (copies > 0) {
                                                                    firebaseFirestore.collection("Books")
                                                                            .document(bookReqRecModel.getBookabbr())
                                                                            .update("Copies", copies - 1)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    progressBar.setVisibility(View.GONE);
                                                                                    acceptSuccessListener.onAccept();
                                                                                    Toast.makeText(context, "Book Request Accepted", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.d(TAG, "Error updating Book");
                                                                                    progressBar.setVisibility(View.GONE);
                                                                                    Toast.makeText(context, "Error Accepting Request", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                } else {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(context, "No Book Copies in Library", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        } else {
                                                            progressBar.setVisibility(View.GONE);
                                                            Toast.makeText(context, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(context, "Error Accepting Book", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void viewrequested(View view) {
        alertDialog.setTitle("Requested Books");
        alertDialog.setView(view)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        docRef.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(context, "Deleted Request Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(context, "Error Deleting Request", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void viewreceived(View view) {
        alertDialog.setTitle("Received Books");
        alertDialog.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    private void GetREQRESFromFirestore(final String reqdate) {
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
                        Log.d("Data", String.valueOf(doc.getData()));
                        try {
                            bookReqRecModel.setCopies(Objects.requireNonNull(doc.getLong("Copies")).intValue());
                            bookReqRecModel.setBookabbr(doc.getString("Abbr"));
                            abbr = doc.getString("Abbr");
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                        bookName = doc.getString("Bookname");
                        bookReqRecModel.setBookname(doc.getString("Bookname"));
                        bookname.setText(bookName);
                        bookReqRecModel.setBookauthor(doc.getString("Bookauthor"));
                        bookauthor.setText(bookReqRecModel.getBookauthor());
                        bookReqRecModel.setBookedition(doc.getString("Bookedition"));
                        String edition = "Edition: " + bookReqRecModel.getBookedition();
                        bookedition.setText(edition);

                        String reqrec = reqdate + doc.getString("Bookreqrecdate");
                        if(tag.equals("Return Dates")){
                            assert getTag() != null;
                            reqresdate.setText(getTag().trim());
                        }else {
                            reqresdate.setText(reqrec);
                        }

                        bookReqRecModel.setUsername(doc.getString("Username"));
                        username.setText(bookReqRecModel.getUsername());
                        bookReqRecModel.setRollno(Objects.requireNonNull(doc.getString("Rollno")).toUpperCase());
                        rollno.setText(bookReqRecModel.getRollno());
                    }
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
