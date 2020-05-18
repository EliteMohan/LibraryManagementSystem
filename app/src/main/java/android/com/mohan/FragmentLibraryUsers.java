package android.com.mohan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentLibraryUsers extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_users,container,false);
//        TextView search = view.findViewById(R.id.search_bookID);
        LinearLayoutCompat search = view.findViewById(R.id.fragmentPartLibOne);
        LinearLayoutCompat viewallbooks = view.findViewById(R.id.fragmentPartLibTwo);
        //changes fragment layout
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSearchBook searchBook = new FragmentSearchBook();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,searchBook);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        viewallbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentViewAllBooks viewAllBooks = new FragmentViewAllBooks();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,viewAllBooks);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}
