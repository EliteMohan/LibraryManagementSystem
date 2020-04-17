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
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FragmentSettings extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_settings,container,false);
        View view =  inflater.inflate(R.layout.fragment_settings,container,false);
        TextView logout = view.findViewById(R.id.logoutID);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(),"Logout Successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),LoginAndSignUpActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        return view;
    }
}
