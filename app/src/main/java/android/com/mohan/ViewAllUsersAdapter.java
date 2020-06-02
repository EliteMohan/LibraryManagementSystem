package android.com.mohan;

import android.com.mohan.Models.UsersModel;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ViewAllUsersAdapter extends FirestoreRecyclerAdapter<UsersModel,ViewAllUsersAdapter.ViewHolder> {
    private Context aContext;
    private ContentLoadingProgressBar progressBar;
    ViewAllUsersAdapter(@NonNull FirestoreRecyclerOptions<UsersModel> options,Context aContext,ContentLoadingProgressBar progressBar) {
        super(options);
        this.aContext = aContext;
        this.progressBar = progressBar;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewAllUsersAdapter.ViewHolder holder, int position, @NonNull UsersModel model) {
        final DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());//gives document ID
        holder.username.setText(model.getUsername());
        holder.rollno.setText(model.getRollno());
        final Bundle args = new Bundle();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("users").document(doc.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snap = task.getResult();
                    assert snap != null;
                    if(snap.exists()){
                        args.putString("ADMINCODE",snap.getString("ADMINCODE"));
                    }
                }else {
                    Toast.makeText(aContext, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        boolean isCurrentUser = doc.getId().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        if(!isCurrentUser){
            //able to change admin permission only to others
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewUserDialog userDialog = new ViewUserDialog(aContext,progressBar,doc.getId());
                    userDialog.setArguments(args);
                    userDialog.show(((AppCompatActivity)aContext).getSupportFragmentManager(),"user dialog");
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewAllUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_all_users_single_item, parent, false);
        return new ViewAllUsersAdapter.ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username, rollno;
        private View mView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            username = itemView.findViewById(R.id.username_1);
            rollno = itemView.findViewById(R.id.roll_1);
        }
    }
}
