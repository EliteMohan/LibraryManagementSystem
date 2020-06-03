package android.com.mohan.Modules.AdminModule.FragmentDialogComponents;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.com.mohan.R;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ViewUserDialog extends DialogFragment {
    private AlertDialog.Builder alertDialog;
    private Context context;
    private ContentLoadingProgressBar progressBar;
    private String id;
    private AppCompatTextView username, rollno, email, phone, gender, college;
    private DocumentReference docRef;
    private View view;

    public ViewUserDialog(Context context, ContentLoadingProgressBar progressBar, String id) {
        this.context = context;
        this.progressBar = progressBar;
        this.id = id;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        alertDialog = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.layout_view_user_dialog, null);
        username = view.findViewById(R.id.username_dialog);
        rollno = view.findViewById(R.id.rollno_dialog);
        email = view.findViewById(R.id.email_dialog);
        phone = view.findViewById(R.id.phone_dialog);
        gender = view.findViewById(R.id.gender_dialog);
        college = view.findViewById(R.id.college_dialog);
        Bundle mArgs = getArguments();
        assert mArgs != null;
        String ADMINCODE = mArgs.getString("ADMINCODE");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        docRef = firebaseFirestore.collection("users").document(id);
        getUserDataFromFirestore();
        assert ADMINCODE != null;
        DialogLogic(ADMINCODE);
        return alertDialog.create();
    }

    private void DialogLogic(final String ADMINCODE) {
        String positiveBtnText;
        if (ADMINCODE.equals("0")) {
            positiveBtnText = "Grant Admin Permission";
        } else {
            positiveBtnText = "Remove Admin Permission";
        }

        alertDialog.setTitle("User Data");
        alertDialog.setView(view);
        alertDialog.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.VISIBLE);

                if (ADMINCODE.equals("0")) {
                    docRef.update("ADMINCODE", "1")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(context, "Admin Permission Granted", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    docRef.update("ADMINCODE", "0")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(context, "Admin Permission Removed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void getUserDataFromFirestore() {
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    username.setText(snapshot.getString("username"));
                    rollno.setText(snapshot.getString("rollno"));
                    email.setText(snapshot.getString("email"));
                    phone.setText(snapshot.getString("phone"));
                    gender.setText(snapshot.getString("gender"));
                    college.setText(snapshot.getString("college"));
                } else {
                    Log.d(TAG, "Current data: null");
                }

            }
        });
    }
}
