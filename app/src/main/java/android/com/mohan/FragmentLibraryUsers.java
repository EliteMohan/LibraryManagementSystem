package android.com.mohan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentLibraryUsers extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_users,container,false);
//        TextView search = view.findViewById(R.id.search_bookID);
        Button search = view.findViewById(R.id.search_bookID);
        Button viewAllBooks = view.findViewById(R.id.viewAllBooksID);
        Button recommendNewBook = view.findViewById(R.id.recommendBookID);
        Button FeedbackAboutBook = view.findViewById(R.id.feedbackBookID);

        //changes fragment layout
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentViewAllBooks viewAllBooks = new FragmentViewAllBooks();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_1,viewAllBooks,"Search Book");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        viewAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentViewAllBooks viewAllBooks = new FragmentViewAllBooks();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_1,viewAllBooks,"All Books");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
       recommendNewBook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FragmentBookRecommend bookRecommend = new FragmentBookRecommend();
               assert getFragmentManager() != null;
               FragmentTransaction transaction = getFragmentManager().beginTransaction();
               transaction.replace(R.id.fragment_container,bookRecommend);
               transaction.addToBackStack(null);
               transaction.commit();
           }
       });
        FeedbackAboutBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFeedbackBook feedbackBook = new FragmentFeedbackBook();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,feedbackBook);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}
