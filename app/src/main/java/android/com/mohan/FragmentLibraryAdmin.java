package android.com.mohan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class FragmentLibraryAdmin extends Fragment implements FragmentBookAdminOne.FragBookAdminOneListener {
    private AppCompatTextView RemainBooks;
    private FragmentBookAdminOne bookAdminOne;
    private FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_admin, container, false);
        AppCompatTextView addBook = view.findViewById(R.id.add_bookID);
        AppCompatTextView removeBook = view.findViewById(R.id.removeBookID);
        LinearLayoutCompat viewAllBooks = view.findViewById(R.id.fragmentPartAdminTwo);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bookAdminOne = new FragmentBookAdminOne();
                    bookAdminOne.show(getChildFragmentManager(), "Add Book");
                } catch (Exception e) {
                    Log.d("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        viewAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentViewAllBooks fragmentViewAllBooks = new FragmentViewAllBooks();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin,fragmentViewAllBooks);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        removeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bookAdminOne = new FragmentBookAdminOne();
                    bookAdminOne.show(getChildFragmentManager(), "Remove Book");
                } catch (Exception e) {
                    Log.d("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        });


        return view;
    }

    @Override
    public void addBookData(final String addBookStr, final String abbrStr, String authorStr, String editionStr, String isbnCodeStr, String reqCode, int copiesInt) {
        firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance
        //creating collection(database) users with document(tables) userID
        DocumentReference documentReference = firebaseFirestore.collection("Books").document(abbrStr.toUpperCase());
        //Creating and Storing data as key/value pairs
        Map<String, Object> Book = new HashMap<>();
        Book.put("Book Name", addBookStr.toUpperCase());
        Book.put("Book Abbr", abbrStr);
        Book.put("Book Author", authorStr.toUpperCase());
        Book.put("Book Edition", editionStr);
        Book.put("Book ISBN Code", isbnCodeStr);
        Book.put("Copies", copiesInt);
        //adds book to Books collection
        //success and failure listeners
        documentReference.set(Book).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Book ".concat(addBookStr).concat(" Added Successfully"), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ErrorInfo", "onFailure ".concat(e.toString()));
            }
        });
        DocumentReference documentReference1 = firebaseFirestore.collection("BooksTwo").document(abbrStr.toUpperCase());
        documentReference1.set(Book).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Book ".concat(addBookStr).concat(" Added Successfully"), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ErrorInfo", "onFailure ".concat(e.toString()));
            }
        });
    }

    @Override
    public void removeBookData(final String removeBookAbbr, String reqCode, final int copiesInt) {
        firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance
        firebaseFirestore.collection("Books").document(removeBookAbbr)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            assert doc != null;
                            if (doc.exists()) {
                                Long copies = doc.getLong("Copies");
                                @SuppressWarnings("ConstantConditions") final Long finalCopies = copies - copiesInt;
                                if (finalCopies > 0) {
                                    firebaseFirestore.collection("Books").document(removeBookAbbr)
                                            .update("Copies", finalCopies)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), copiesInt + " books removed from server", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else if (finalCopies < 0) {
                                    Toast.makeText(getActivity(), "Only " + copies + " books available,you entered " + copiesInt, Toast.LENGTH_SHORT).show();
                                } else {
                                    firebaseFirestore.collection("Books").document(removeBookAbbr)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    Toast.makeText(getActivity(), "Book data removed completely", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });

                                }
                            } else {
                                Toast.makeText(getActivity(), "This Book data doesn't exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
