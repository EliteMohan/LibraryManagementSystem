package android.com.mohan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class FragmentAccount extends Fragment implements AccountDialog.AccountDialogListener,EmailUpdateDialog.EmailUpdateDialogListener {
    private TextView currentUser,currentUserEmail,currentUserPhone,currentUserRollNo;
    private AccountDialog accountDialog;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_account,container,false);
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        currentUser = view.findViewById(R.id.userNameupdate);
        currentUserEmail = view.findViewById(R.id.emailUpdate);
        currentUserPhone = view.findViewById(R.id.phoneUpdate);
        currentUserRollNo = view.findViewById(R.id.rollNoUpdate);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();//FirebaseAuth Instance
        firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();//getting current userID required to fetch user profile data

        currentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    accountDialog = new AccountDialog();
                    accountDialog.show(getChildFragmentManager(),"username");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        currentUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EmailUpdateDialog emailUpdateDialog = new EmailUpdateDialog();
                    emailUpdateDialog.show(Objects.requireNonNull(getChildFragmentManager()),"email");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        currentUserPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    accountDialog = new AccountDialog();
                    //assert getFragmentManager() != null;
                    accountDialog.show(Objects.requireNonNull(getChildFragmentManager()),"phone");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        currentUserRollNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    accountDialog = new AccountDialog();
                    //assert getFragmentManager() != null;
                    accountDialog.show(Objects.requireNonNull(getChildFragmentManager()),"rollno");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Read data
        DocumentReference docRef = firebaseFirestore.collection("users").document(userID);
        docRef.addSnapshotListener(Objects.requireNonNull(getActivity()), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                currentUser.setText(documentSnapshot.getString("username"));
                currentUserEmail.setText(documentSnapshot.getString("email"));
                currentUserPhone.setText(documentSnapshot.getString("phone"));
                currentUserRollNo.setText(documentSnapshot.getString("rollno"));
            }
        });
        return view;
    }

    @Override
    public void emailUpdateMethod(final String email,final String newemail, String pass) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, pass);
// Prompt the user to re-provide their sign-in credentials
        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }
        });
            user.updateEmail(newemail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, newemail.concat(" User email address updated."));
                                Map<String, Object> data = new HashMap<>();
                                data.put("email",newemail);
                                DocumentReference docRef = firebaseFirestore.collection("users").document(userID);
                                docRef.update(data);
                                Toast.makeText(getActivity(),newemail.concat(" Updated Successfully"),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            });
    }

    @Override
    public void textUpdaterMethod(String str,String tag) {
        Log.i("Info",str);
        Map<String, Object> data = new HashMap<>();
        switch (tag){
            case "username":
                data.put("username",str);
                break;
            case "phone":
                data.put("phone",str);
                break;
            default:
                data.put("rollno",str.toUpperCase());
                break;
        }
        //updating user/admin data
        DocumentReference docRef = firebaseFirestore.collection("users").document(userID);
        docRef.update(data);
        Toast.makeText(getActivity(),str.concat(" Updated Successfully"),Toast.LENGTH_SHORT).show();

    }
}
