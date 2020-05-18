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
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class ViewBookDialog extends AppCompatDialogFragment {
    private Context context;
    private StringBuilder abbr;
    private String bookname, bookauthor;

    ViewBookDialog(Context context, StringBuilder abbr, String bookname, String bookauthor) {
        this.context = context;
        this.abbr = abbr;
        this.bookname = bookname;
        this.bookauthor = bookauthor;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.layout_book_dialog, null);

        final AppCompatTextView bookName = view.findViewById(R.id.add_book_dialog);
        final AppCompatTextView bookCopies = view.findViewById(R.id.book_avail_dialog);
        final AppCompatTextView bookAbbr = view.findViewById(R.id.book_abbrev_dialog);
        final AppCompatTextView bookAuthor = view.findViewById(R.id.book_author_dialog);
        final AppCompatTextView bookEdition = view.findViewById(R.id.book_edition_dialog);
        final AppCompatTextView bookISBN = view.findViewById(R.id.book_isbn_dialog);
        String Abbr = "Abbreviation: "+ abbr;
        bookAbbr.setText(Abbr);
        bookName.setText(bookname);
        bookAuthor.setText(bookauthor);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firebaseFirestore.collection("Bookstwo").document(String.valueOf(abbr));
        try {
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                    assert doc != null;
                    if(doc.exists()){
                        Log.d("Data", String.valueOf(doc.getData()));
                        String copies = "Copies: "+ Objects.requireNonNull(doc.getLong("Copies")).intValue();
                        bookCopies.setText(copies);
                        String edition = "Edition: "+ doc.getString("Book Edition");
                        bookEdition.setText(edition);
                        String isbn = "ISBN Code: "+doc.getString("Book ISBN Code");
                        bookISBN.setText(isbn);
                    }else {
                        Toast.makeText(getActivity(),"No resource found",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        alertDialog.setTitle("Book");
        alertDialog.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return alertDialog.create();
    }
}
