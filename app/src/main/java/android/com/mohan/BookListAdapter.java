package android.com.mohan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BookListAdapter extends FirestoreRecyclerAdapter<BooksModel, BookListAdapter.ViewHolder> {

    private Context context;
    private String tag;
    private  ContentLoadingProgressBar progressBar;
    BookListAdapter(@NonNull FirestoreRecyclerOptions<BooksModel> options, Context context, String tag, ContentLoadingProgressBar progressBar) {
        super(options);
        this.context = context;
        this.tag = tag;
        this.progressBar = progressBar;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final BooksModel model) {

        int pos = position + 1;
        String concat = pos + ". " + model.getBookname();

        if(model.getImageurl().equals("noimage")){
            holder.bookImage.setImageResource(R.mipmap.book_cover);
        }else {
            Glide.with(context).load(model.getImageurl()).into(holder.bookImage);
        }

        holder.bookName.setText(concat);
        holder.bookAuthor.setText(model.getBookauthor());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSnapshot doc = getSnapshots().getSnapshot(holder.getAdapterPosition());//gives document ID
                ViewBookDialog bookDialog = new ViewBookDialog(context, doc.getId(), model.getBookname(), model.getBookauthor(),tag,progressBar);
                bookDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "BookDialog");
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName, bookAuthor;
        private View mView;
        private AppCompatImageView bookImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            bookImage = itemView.findViewById(R.id.image_sample);
            bookName = itemView.findViewById(R.id.book_name_1);
            bookAuthor = itemView.findViewById(R.id.book_author_1);
        }
    }

}
