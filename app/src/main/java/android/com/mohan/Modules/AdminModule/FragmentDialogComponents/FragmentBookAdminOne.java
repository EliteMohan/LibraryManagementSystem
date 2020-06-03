package android.com.mohan.Modules.AdminModule.FragmentDialogComponents;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.com.mohan.Models.BooksModel;
import android.com.mohan.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FragmentBookAdminOne extends AppCompatDialogFragment {
    private static final String REQCODE = "1";
    private FragBookAdminOneListener bookAdminOneListener;
    private Context context;
    private AppCompatEditText addBook, author, edition, isbnCode, removeBook, copies;
    private TextView bookImageName;
    private BooksModel booksModel = new BooksModel();
    private ContentLoadingProgressBar progressBar;

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        AlertDialog retBuilder;
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.dialog_book, null);
        addBook = view.findViewById(R.id.add_book_dialog);
        author = view.findViewById(R.id.book_author_dialog);
        edition = view.findViewById(R.id.book_edition_dialog);
        isbnCode = view.findViewById(R.id.book_isbn_dialog);
        removeBook = view.findViewById(R.id.removeBookAbbrev_dialog);
        copies = view.findViewById(R.id.book_avail_dialog);
        bookImageName = view.findViewById(R.id.book_image_name);
        Button choose = view.findViewById(R.id.file_chooser);
        LinearLayout imagelayout = view.findViewById(R.id.imagelayout);
        progressBar = view.findViewById(R.id.progressBarSend);
        progressBar.setVisibility(View.GONE);
        TextInputLayout addBookIN = view.findViewById(R.id.add_book_dialog_in);
        TextInputLayout authorIN = view.findViewById(R.id.book_author_dialog_in);
        TextInputLayout editionIN = view.findViewById(R.id.book_edition_dialog_in);
        TextInputLayout isbnCodeIN = view.findViewById(R.id.book_isbn_dialog_in);

//        if (getActivity().getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMarginEnd(R.dimen._44sdp);
//            choose.setLayoutParams(params);
//        }
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        assert getTag() != null;
        switch (getTag()) {
            case "Add Book":
                builder.setTitle(getTag());
                removeBook.setVisibility(View.GONE);
                retBuilder = addBookDialogBuilder(builder, view);
                break;
            case "Remove Book":
                builder.setTitle(getTag());
                addBook.setVisibility(View.GONE);
                addBookIN.setVisibility(View.GONE);
                author.setVisibility(View.GONE);
                edition.setVisibility(View.GONE);
                isbnCode.setVisibility(View.GONE);
                authorIN.setVisibility(View.GONE);
                editionIN.setVisibility(View.GONE);
                isbnCodeIN.setVisibility(View.GONE);
                imagelayout.setVisibility(View.GONE);
                retBuilder = removeBookDialogBuilder(builder, view);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getTag());
        }

        return retBuilder;
    }

    private final int IMAGE_REQUEST_CODE = 1;

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private AlertDialog removeBookDialogBuilder(AlertDialog.Builder builder, View view) {
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

    private AlertDialog addBookDialogBuilder(final AlertDialog.Builder builder,
                                             View view) {
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
                                || TextUtils.isEmpty(Objects.requireNonNull(author.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(edition.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(isbnCode.getText()).toString())
                                || TextUtils.isEmpty(Objects.requireNonNull(copies.getText()).toString());
                        if (isFieldsEmpty) {
                            addBook.setError("Value Missing");
                            author.setError("Value Missing");
                            edition.setError("Value Missing");
                            isbnCode.setError("Value Missing");
                            copies.setError("Value Missing");
                        } else {
                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            String[] AbbrExtract = new String[0];
                            String[] AbbrExtract1 = new String[0];
                            try {
                                AbbrExtract = Objects.requireNonNull(addBook.getText()).toString().split(" ");//split book name into words
                                AbbrExtract1 = Objects.requireNonNull(author.getText()).toString().split(" ");//split author name into words
                            } catch (NullPointerException e) {
                                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                            }
                            final StringBuilder Abbr = new StringBuilder();
                            for (String str : AbbrExtract) {
                                char Char = str.charAt(0);//takes 1st letter of each word
                                Abbr.append(Char);//appends 1st letter at end
                            }
                            Abbr.append(Objects.requireNonNull(edition.getText()).toString());
                            for (String str : AbbrExtract1) {
                                char Char = str.charAt(0);
                                Abbr.append(Char);
                            }
                            Log.d("Abbr", String.valueOf(Abbr));

                            firebaseFirestore.collection("Books").document(String.valueOf(Abbr))
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        assert doc != null;
                                        if (doc.exists()) {
                                            new AlertDialog.Builder(Objects.requireNonNull(context))
                                                    .setTitle("This book already exists in server\nDo you want to update copies!")
                                                    .setView(R.layout.logout_dialog)
                                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    firebaseFirestore.collection("Books").document(String.valueOf(Abbr))
                                                            .update("Copies", Integer.parseInt(Objects.requireNonNull(copies.getText()).toString()))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(context, "Successfully Updated Book Copies", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }).show();
                                        } else {
                                            String addBookStr = addBook.getText().toString();
                                            String abbrStr = String.valueOf(Abbr);
                                            String authorStr = author.getText().toString();
                                            String editionStr = edition.getText().toString();
                                            String isbnCodeStr = isbnCode.getText().toString();
                                            int copiesInt = Integer.parseInt(Objects.requireNonNull(copies.getText()).toString());
                                            if (copiesInt < 0) {
                                                copies.setError("Negative values not allowed");
                                            } else {
                                                bookAdminOneListener.addBookData(addBookStr, abbrStr, authorStr, editionStr, isbnCodeStr, REQCODE, copiesInt, booksModel.getImageurl());
                                            }

                                        }
                                    } else {
                                        Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
        return builder.create();
    }

    private StorageReference storageReference;
    private String filename;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String filepath = data.getData().getPath();
            assert filepath != null;
            Log.d("FILEPATH", filepath);
            filename = filepath.substring(filepath.lastIndexOf("/") + 1);//gives filename
            Uri imageURI = data.getData();
            storageReference = FirebaseStorage.getInstance().getReference(filename);
            UploadTask uploadTask = storageReference.putFile(imageURI);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        bookImageName.setText(filename);
                        Uri uri = task.getResult();
                        assert uri != null;
                        Log.d("Image URI-3", uri.toString());
                        booksModel.setImageurl(uri.toString());
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Log.d(TAG, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                    }
                }
            });
        }
    }

    public interface FragBookAdminOneListener {
        void addBookData(String addBookStr, String abbrStr, String authorStr, String editionStr, String isbnCodeStr, String reqCode, int copiesInt, String s);

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
