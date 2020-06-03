package android.com.mohan.Modules.AdminModule.FragmentComponents;

import android.com.mohan.Modules.AdminModule.FragmentDialogComponents.FragmentBookAdminOne;
import android.com.mohan.Modules.CommonModule.FragmentRecyclerViewComponents.FragmentRecyclerViewAllBooks;
import android.com.mohan.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class FragmentLibraryAdmin extends Fragment implements FragmentBookAdminOne.FragBookAdminOneListener {

    private FragmentBookAdminOne bookAdminOne;
    private FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_admin, container, false);
        Button addBook = view.findViewById(R.id.add_bookID);
        Button removeBook = view.findViewById(R.id.removeBookID);
        Button viewAllBooks = view.findViewById(R.id.viewAllBooksID);
        Button remainBooks = view.findViewById(R.id.remainBookID);

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
                FragmentRecyclerViewAllBooks fragmentRecyclerViewAllBooks = new FragmentRecyclerViewAllBooks();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin_1, fragmentRecyclerViewAllBooks, "All Books");
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

        remainBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRecyclerViewAllBooks fragmentRecyclerViewAllBooks = new FragmentRecyclerViewAllBooks();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin_1, fragmentRecyclerViewAllBooks, "Remaining Books");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void addBookData(final String addBookStr, final String abbrStr, String authorStr, String editionStr, String isbnCodeStr, String reqCode, int copiesInt, String s) {
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
        Book.put("Total Copies", copiesInt);
        Book.put("imageurl", s);
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
                                final String imageURL = doc.getString("imageurl");
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
                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                    assert imageURL != null;
                                                    StorageReference imageRef = storage.getReferenceFromUrl(imageURL);
                                                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                            Toast.makeText(getActivity(), "Book data removed completely", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, "Filed to delete file");
                                                            Toast.makeText(getActivity(), "Filed to delete file", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

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
