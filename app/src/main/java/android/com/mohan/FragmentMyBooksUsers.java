package android.com.mohan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentMyBooksUsers extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_mybooks_user, container, false);
        Button viewBooksRequested = view.findViewById(R.id.view_booksRequestedID);
        Button viewBooksReceived = view.findViewById(R.id.view_booksRecievedID);
        Button allBooksRetDate = view.findViewById(R.id.retDateBookID);

        viewBooksRequested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentViewReqRes fragmentBooksRequested = new FragmentViewReqRes();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_1, fragmentBooksRequested, "Requested Books");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        viewBooksReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentViewReqRes fragmentBooksReceived = new FragmentViewReqRes();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_1, fragmentBooksReceived, "Received Books");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        allBooksRetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentViewReqRes fragmentBooksReceived = new FragmentViewReqRes();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_1, fragmentBooksReceived, "Return Dates");
                transaction.addToBackStack(null);
                transaction.commit();
                //                transaction.replace(R.id.fragment_container_1,booksReturnDates);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
        return view;
    }
}
