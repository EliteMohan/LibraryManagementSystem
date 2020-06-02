package android.com.mohan;

import android.com.mohan.Models.BookReqRecModel;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ReqResListAdapter extends FirestoreRecyclerAdapter<BookReqRecModel, ReqResListAdapter.ViewHolder> {

    private Context context;
    private String tag;
    private ContentLoadingProgressBar progressBar;
    private FragmentViewReqRes fragmentViewReqRes;

    ReqResListAdapter(FirestoreRecyclerOptions<BookReqRecModel> firestoreRecyclerOptions, Context context, String tag, ContentLoadingProgressBar progressBar) {
        super(firestoreRecyclerOptions);
        this.context = context;
        this.tag = tag;
        this.progressBar = progressBar;
    }



    void setFragmentViewReqRes(FragmentViewReqRes fragmentViewReqRes) {
        this.fragmentViewReqRes = fragmentViewReqRes;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ReqResListAdapter.ViewHolder holder, final int position, @NonNull final BookReqRecModel model) {
        int pos = position + 1;
        String concat = pos + ". " + model.getBookname();

        try {
            if (tag.equals("Return Dates")) {
                final String retdate = "     "+"Return Date: "+model.getRetdate();
                if(model.getImageurl().equals("noimage")){
                   holder.bookImage.setImageResource(R.mipmap.book_cover);
                }else {
                    Glide.with(context).load(model.getImageurl()).into(holder.bookImage);
                }
                holder.reqcode.setVisibility(View.GONE);
                holder.bookName.setText(concat);
                holder.userRollno.setText(retdate);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());//gives document ID
                        ViewReqResDialog reqResDialog = new ViewReqResDialog(context, tag, progressBar, doc.getId());
                        reqResDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), retdate);
                    }
                });

            } else {
                if (model.getReccode().equals("1")) {
                    if ((tag.equals("Requested Books"))) {
                        Log.d("INFO", concat + " " + model.getRollno() + " " + model.getReccode());
                        if(model.getImageurl().equals("noimage")){
                            holder.bookImage.setImageResource(R.mipmap.book_cover);
                        }else{
                            Glide.with(context).load(model.getImageurl()).into(holder.bookImage);
                        }
                        holder.reqcode.setVisibility(View.GONE);
                        holder.bookName.setText(concat);
                        Log.d("roll no",model.getRollno());
                        holder.userRollno.setText(model.getRollno());
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        Log.d("userID",userID);
                        final String[] rollno = new String[1];
                        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
                        mFirestore.collection("users").document(userID)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult();
                                    assert doc != null;
                                    if(doc.exists()){
                                        rollno[0] = doc.getString("rollno");
                                    }
                                }else {
                                    Log.d("Error in adapter", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                                }
                            }
                        });
                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // allows only requested user to delete their request
                                    if(model.getRollno().equals(rollno[0].toUpperCase())){
                                    DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());//gives document ID
                                    ViewReqResDialog reqResDialog = new ViewReqResDialog(context, tag, progressBar, doc.getId());
                                    reqResDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "REQREC Dialog");
                                }else {
                                    Toast.makeText(context,"Click on your book request to modify",Toast.LENGTH_SHORT).show();
                                }

                            }
                            });
                    } else if (tag.equals("Requested BooksAdmin")) {
                        Log.d("INFO", concat + " " + model.getRollno() + " " + model.getReccode());
                        if(model.getImageurl().equals("noimage")){
                            holder.bookImage.setImageResource(R.mipmap.book_cover);
                        }else {
                            Glide.with(context).load(model.getImageurl()).into(holder.bookImage);
                        }
                        holder.reqcode.setVisibility(View.GONE);
                        holder.bookName.setText(concat);
                        holder.userRollno.setText(model.getRollno());
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Info fragment object", String.valueOf(fragmentViewReqRes));
                                DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());//gives document ID
                                ViewReqResDialog reqResDialog = new ViewReqResDialog(context, tag, progressBar, doc.getId());
                                reqResDialog.setOnAcceptListener(fragmentViewReqRes);
                                reqResDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "REQREC Dialog");
                            }
                        });

                    }
                } else if (model.getReccode().equals("0")) {
                    if (tag.equals("Received Books")) {
                        Log.d("INFO", concat + " " + model.getRollno() + " " + model.getReccode());
                        if(model.getImageurl().equals("noimage")){
                            holder.bookImage.setImageResource(R.mipmap.book_cover);
                        }else {
                            Glide.with(context).load(model.getImageurl()).into(holder.bookImage);
                        }
                        holder.reqcode.setVisibility(View.GONE);
                        holder.bookName.setText(concat);
                        holder.userRollno.setText(model.getRollno());
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Info fragment object", String.valueOf(fragmentViewReqRes));
                                DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());//gives document ID
                                ViewReqResDialog reqResDialog = new ViewReqResDialog(context, tag, progressBar, doc.getId());
                                reqResDialog.setOnAcceptListener(fragmentViewReqRes);
                                reqResDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "REQREC Dialog");
                            }
                        });

                    } else if (tag.equals("Received BooksAdmin")) {
                        if(model.getImageurl().equals("noimage")){
                            holder.bookImage.setImageResource(R.mipmap.book_cover);
                        }else {
                            Glide.with(context).load(model.getImageurl()).into(holder.bookImage);
                        }
                        holder.reqcode.setVisibility(View.GONE);
                        holder.bookName.setText(concat);
                        holder.userRollno.setText(model.getRollno());
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());//gives document ID
                                reqResDialog_n = new ViewReqResDialog(context, tag, progressBar, doc.getId());
                                reqResDialog_n.show(((AppCompatActivity) context).getSupportFragmentManager(), "REQREC Dialog");
                            }
                        });
                    }
                }
            }

        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ReqResListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reqres_single_item, parent, false);
        return new ViewHolder(view);
    }

    private ViewReqResDialog reqResDialog_n;


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userRollno, bookName, reqcode;
        private View mView;
        private AppCompatImageView bookImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            bookImage = itemView.findViewById(R.id.image_sample);
            reqcode = itemView.findViewById(R.id.book_reqcode_1);
            bookName = itemView.findViewById(R.id.book_name_1);
            userRollno = itemView.findViewById(R.id.book_author_1);
        }
    }
}
