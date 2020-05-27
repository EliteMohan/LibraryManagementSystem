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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentAccount extends Fragment implements AccountDialog.AccountDialogListener {
    private TextView currentUser, currentUserEmail, currentUserPhone, currentUserRollNo;
    private AccountDialog accountDialog;
    private FirebaseFirestore firebaseFirestore;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_account,container,false);
        View view = inflater.inflate(R.layout.fragment_account, container, false);
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
                    accountDialog.show(getChildFragmentManager(), "username");
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
                    accountDialog.show(Objects.requireNonNull(getChildFragmentManager()), "phone");
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
                try {
                    currentUser.setText(documentSnapshot.getString("username"));
                    currentUserEmail.setText(documentSnapshot.getString("email"));
                    currentUserPhone.setText(documentSnapshot.getString("phone"));
                    currentUserRollNo.setText(Objects.requireNonNull(documentSnapshot.getString("rollno")).toUpperCase());
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void textUpdaterMethod(String str, String tag) {
        Log.i("Info", str);
        Map<String, Object> data = new HashMap<>();
        switch (tag) {
            case "username":
                data.put("username", str);
                break;
            case "phone":
                data.put("phone", str);
                break;
        }
        //updating user/admin data
        DocumentReference docRef = firebaseFirestore.collection("users").document(userID);
        docRef.update(data);
        Toast.makeText(getActivity(), str.concat(" Updated Successfully"), Toast.LENGTH_SHORT).show();

    }
}
