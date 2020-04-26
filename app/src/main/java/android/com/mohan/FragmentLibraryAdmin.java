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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class FragmentLibraryAdmin extends Fragment implements FragmentBookAdminOne.FragBookAdminOneListener {
    private AppCompatTextView viewAllBooks,RemainBooks;
    private FragmentBookAdminOne bookAdminOne;
    private FirebaseFirestore firebaseFirestore;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_admin,container,false);
        AppCompatTextView addBook = view.findViewById(R.id.add_bookID);
        AppCompatTextView removeBook = view.findViewById(R.id.removeBookID);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bookAdminOne = new FragmentBookAdminOne();
                    bookAdminOne.show(getChildFragmentManager(),"Add Book");
                } catch (Exception e) {
                    Log.d("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        });
        removeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bookAdminOne = new FragmentBookAdminOne();
                    bookAdminOne.show(getChildFragmentManager(),"Remove Book");
                } catch (Exception e) {
                    Log.d("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        });


        return view;
    }

    @Override
    public void addBookData(final String addBookStr, final String abbrStr, String authorStr, String editionStr, String isbnCodeStr, String reqCode) {

        firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance
        //creating collection(database) users with document(tables) userID
        DocumentReference documentReference = firebaseFirestore.collection("Books").document(abbrStr.toUpperCase());
        //Creating and Storing data as key/value pairs
        Map<String,Object> Book = new HashMap<>();
        Book.put("Book Name",addBookStr);
        Book.put("Book Abbr",abbrStr);
        Book.put("Book Author",authorStr);
        Book.put("Book Edition",editionStr);
        Book.put("Book ISBN Code",isbnCodeStr);
        Book.put("Book Req Code",reqCode);
        //adds book to Books collection
        //success and failure listeners
        documentReference.set(Book).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d("userInfo","User data Successfully added to user ".concat(abbrStr.toUpperCase()));
                Toast.makeText(getActivity(), "Book ".concat(addBookStr).concat(" Added Successfully"), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ErrorInfo","onFailure ".concat(e.toString()));
            }
        });
    }

    @Override
    public void removeBookData(String removeBookAbbr, String reqCode) {
        if(reqCode.equals("1")) {
            firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance
            firebaseFirestore.collection("Books").document(removeBookAbbr)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            Toast.makeText(getActivity(), "Book removed successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }else{
            Toast.makeText(getActivity(),"Book Cannot be removed because book not yet returned by the user",Toast.LENGTH_SHORT).show();
        }
    }
}
