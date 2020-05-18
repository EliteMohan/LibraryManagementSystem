package android.com.mohan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FragmentSettingsUsers extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings_users,container,false);
        LinearLayoutCompat feedback = view.findViewById(R.id.fragmentPartSettingsUserOne);
        TextView logout = view.findViewById(R.id.logoutID);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEmailFeedback emailFeedback = new FragmentEmailFeedback();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,emailFeedback);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getActivity(),"Logout Successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),LoginAndSignUpActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                } catch (Exception e) {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}
