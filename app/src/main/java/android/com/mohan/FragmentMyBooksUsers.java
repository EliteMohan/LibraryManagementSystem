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

public class FragmentMyBooksUsers extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_mybooks_user,container,false);
        LinearLayoutCompat viewBooksRequested = view.findViewById(R.id.fragmentPartMyBooksUserOne);
        LinearLayoutCompat viewBooksReceived = view.findViewById(R.id.fragmentPartMyBooksUserTwo);
        LinearLayoutCompat allBooksRetDate = view.findViewById(R.id.fragmentPartMyBooksFour);

        viewBooksRequested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBooksRequested fragmentBooksRequested = new FragmentBooksRequested();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragmentBooksRequested);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        viewBooksReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBooksReceived fragmentBooksReceived = new FragmentBooksReceived();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragmentBooksReceived);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        allBooksRetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBooksReturnDates booksReturnDates = new FragmentBooksReturnDates();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,booksReturnDates);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}
