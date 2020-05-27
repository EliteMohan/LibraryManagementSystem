package android.com.mohan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentBookRecommend extends Fragment {
    private EditText Message;
    private TextView toMail, Subject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_new_book, container, false);
        //Textview
        toMail = view.findViewById(R.id.emailDev);
        //Edittexts
        Subject = view.findViewById(R.id.subject);
        Message = view.findViewById(R.id.message);
        //buttons
        Button send = view.findViewById(R.id.sendFeedbackMailFromAdmin);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        return view;
    }

    private void sendMail() {
        String recipientList = toMail.getText().toString();
        String[] recipients = recipientList.split(",");
        String subject = Subject.getText().toString();
        String message = Message.getText().toString();


        //send email intent procedure
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));

        FragmentLibraryUsers libraryUsers = new FragmentLibraryUsers();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, libraryUsers);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
