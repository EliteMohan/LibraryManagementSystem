package android.com.mohan.Modules.AdminModule.FragmentComponents;

import android.com.mohan.Modules.AdminModule.FragmentRecyclerViewComponents.FragmentRecyclerViewAllUsers;
import android.com.mohan.Modules.CommonModule.FragmentRecyclerViewComponents.FragmentRecyclerViewReqRes;
import android.com.mohan.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUsersAdmin extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_admin,container,false);
        Button bookReqFromUsers = view.findViewById(R.id.booksReqUsersID);
        Button bookRecdUsers = view.findViewById(R.id.booksRecievedUsersID);
        Button viewAllUsers = view.findViewById(R.id.viewAllUsersID);

        bookReqFromUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRecyclerViewReqRes booksReqFromUser = new FragmentRecyclerViewReqRes();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin_1,booksReqFromUser,"Requested BooksAdmin");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        bookRecdUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRecyclerViewReqRes viewReqRes = new FragmentRecyclerViewReqRes();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin_1,viewReqRes,"Received BooksAdmin");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        viewAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRecyclerViewAllUsers viewAllUsers1 = new FragmentRecyclerViewAllUsers();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin_1,viewAllUsers1);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
