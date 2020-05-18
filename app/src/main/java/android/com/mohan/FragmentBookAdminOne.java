package android.com.mohan;

import android.os.Bundle;
import android.annotation.SuppressLint;

import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.content.DialogInterface;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;

import android.text.TextUtils;
import android.util.Log;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.AppCompatEditText;

import static android.content.ContentValues.TAG;

public class FragmentBookAdminOne extends AppCompatDialogFragment {
    private static final String REQCODE = "1";
    private FragBookAdminOneListener bookAdminOneListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        AlertDialog retBuilder;
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.dialog_book, null);
        AppCompatEditText addBook = view.findViewById(R.id.add_book_dialog);
        AppCompatEditText abbr = view.findViewById(R.id.book_abbrev_dialog);
        AppCompatEditText author = view.findViewById(R.id.book_author_dialog);
        AppCompatEditText edition = view.findViewById(R.id.book_edition_dialog);
        AppCompatEditText isbnCode = view.findViewById(R.id.book_isbn_dialog);
        AppCompatEditText removeBook = view.findViewById(R.id.removeBookAbbrev_dialog);
        AppCompatEditText copies = view.findViewById(R.id.book_avail_dialog);

        TextInputLayout addBookIN = view.findViewById(R.id.add_book_dialog_in);
        TextInputLayout abbrIN = view.findViewById(R.id.book_abbrev_dialog_in);
        TextInputLayout authorIN = view.findViewById(R.id.book_author_dialog_in);
        TextInputLayout editionIN = view.findViewById(R.id.book_edition_dialog_in);
        TextInputLayout isbnCodeIN = view.findViewById(R.id.book_isbn_dialog_in);
//        TextInputLayout copiesIN = view.findViewById(R.id.book_avail_dialog_in);
        assert getTag() != null;
        switch (getTag()) {
            case "Add Book":
                builder.setTitle(getTag());
                removeBook.setVisibility(View.GONE);
                retBuilder = addBookDialogBuilder(builder, view, addBook, abbr, author, edition, isbnCode, copies);
                break;
            case "Remove Book":
                builder.setTitle(getTag());
                addBook.setVisibility(View.GONE);
                addBookIN.setVisibility(View.GONE);
                abbr.setVisibility(View.GONE);
                author.setVisibility(View.GONE);
                edition.setVisibility(View.GONE);
                isbnCode.setVisibility(View.GONE);
                abbrIN.setVisibility(View.GONE);
                authorIN.setVisibility(View.GONE);
                editionIN.setVisibility(View.GONE);
                isbnCodeIN.setVisibility(View.GONE);
                retBuilder = removeBookDialogBuilder(builder, view, removeBook, copies);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getTag());
        }

        return retBuilder;
    }

    private AlertDialog removeBookDialogBuilder(AlertDialog.Builder builder, View view, final AppCompatEditText removeBook, final AppCompatEditText copies) {
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(Objects.requireNonNull(removeBook.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(copies.getText()).toString())) {
                            removeBook.setError("Value Missing");
                            copies.setError("Value Missing");
                        } else {
                            String removeBookAbbr = removeBook.getText().toString();
                            int copiesInt = Integer.parseInt(copies.getText().toString());
                            bookAdminOneListener.removeBookData(removeBookAbbr, REQCODE, copiesInt);
                        }
                    }
                });
        return builder.create();
    }

    private AlertDialog addBookDialogBuilder(AlertDialog.Builder builder,
                                             View view, final AppCompatEditText addBook,
                                             final AppCompatEditText abbr, final AppCompatEditText author,
                                             final AppCompatEditText edition, final AppCompatEditText isbnCode,
                                             final AppCompatEditText copies) {
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean isFieldsEmpty = TextUtils.isEmpty(Objects.requireNonNull(addBook.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(abbr.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(author.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(edition.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(isbnCode.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(copies.getText()).toString());
                        if (isFieldsEmpty) {
                            addBook.setError("Value Missing");
                            abbr.setError("Value Missing");
                            author.setError("Value Missing");
                            edition.setError("Value Missing");
                            isbnCode.setError("Value Missing");
                            copies.setError("Value Missing");
                        } else {
                            String addBookStr = addBook.getText().toString();
                            String abbrStr = abbr.getText().toString();
                            String authorStr = author.getText().toString();
                            String editionStr = edition.getText().toString();
                            String isbnCodeStr = isbnCode.getText().toString();
                            int copiesInt = Integer.parseInt(copies.getText().toString());
                            if (copiesInt < 0) {
                                copies.setError("Negative values not allowed");
                            } else {
                                bookAdminOneListener.addBookData(addBookStr, abbrStr, authorStr, editionStr, isbnCodeStr, REQCODE, copiesInt);
                            }
                        }
                    }
                });
        return builder.create();
    }

    public interface FragBookAdminOneListener {
        void addBookData(String addBookStr, String abbrStr, String authorStr, String editionStr, String isbnCodeStr, String reqCode, int copiesInt);

        void removeBookData(String removeBookAbbr, String reqCode, int copiesInt);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bookAdminOneListener = (FragBookAdminOneListener) getParentFragment();
        } catch (ClassCastException c) {
            Log.e(TAG, "onAttach: ClassCastException".concat(Objects.requireNonNull(c.getMessage())));
        }
    }
}
