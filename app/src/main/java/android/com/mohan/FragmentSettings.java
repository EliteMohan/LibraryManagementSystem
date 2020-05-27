package android.com.mohan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FragmentSettings extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_settings,container,false);
        View view =  inflater.inflate(R.layout.fragment_settings,container,false);
        Button feedback = view.findViewById(R.id.FeedbackID);
        Button aboutus = view.findViewById(R.id.AboutUsID);
        Button logout = view.findViewById(R.id.logoutID);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEmailFeedback emailFeedback = new FragmentEmailFeedback();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin,emailFeedback);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAboutUsPage aboutUsPage = new FragmentAboutUsPage();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_admin,aboutUsPage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder logoutDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    logoutDialog.setTitle("Are you sure to logout");
                    logoutDialog.setView(getLayoutInflater().inflate(R.layout.logout_dialog,null)).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getActivity(),"Logout Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(),LoginAndSignUpActivity.class));
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}
