package android.com.mohan;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BookListAdapter extends FirestoreRecyclerAdapter<BooksModel, BookListAdapter.ViewHolder> {

    private Context context;

    BookListAdapter(@NonNull FirestoreRecyclerOptions<BooksModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final BooksModel model) {

        int pos = position + 1;
//        Context mContext = context;
        String concat = pos + ". " + model.getBookname();
        holder.bookName.setText(concat);
        Log.d("Checking", String.valueOf(TextUtils.isEmpty(model.getBookname())));
        holder.bookAuthor.setText(model.getBookauthor());
        String[] AbbrExtract = model.getBookname().split(" ");
        final StringBuilder Abbr = new StringBuilder();
        for (String str : AbbrExtract) {
            char Char = str.charAt(0);
            Abbr.append(Char);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewBookDialog bookDialog = new ViewBookDialog(context,Abbr,model.getBookname(),model.getBookauthor());
                bookDialog.show(((AppCompatActivity)context).getSupportFragmentManager(),"BookDialog");
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName, bookAuthor;
        private View mView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            bookName = itemView.findViewById(R.id.book_name_1);
            bookAuthor = itemView.findViewById(R.id.book_author_1);
        }
    }

}
