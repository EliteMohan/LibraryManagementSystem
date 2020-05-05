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

public class FragmentUsersAdmin extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_admin,container,false);
        LinearLayoutCompat bookReqFromUsers = view.findViewById(R.id.fragmentPartAdminOne);
        LinearLayoutCompat bookRecdUsers = view.findViewById(R.id.fragmentPartAdminThree);
        bookReqFromUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBooksReqFromUser booksReqFromUser = new FragmentBooksReqFromUser();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin,booksReqFromUser);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        bookRecdUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBooksRecdUsersAdmin booksRecdUsersAdmin = new FragmentBooksRecdUsersAdmin();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin,booksRecdUsersAdmin);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}
