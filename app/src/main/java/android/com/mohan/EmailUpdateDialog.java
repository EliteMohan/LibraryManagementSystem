package android.com.mohan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class EmailUpdateDialog extends DialogFragment {
    private EmailUpdateDialogListener dialogListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.update_email_dialog, null);

        final AppCompatEditText oldEmail = view.findViewById(R.id.dialog_old_email);
        final AppCompatEditText passIN = view.findViewById(R.id.dialog_pass);
        final AppCompatEditText newEmail = view.findViewById(R.id.dialog_new_email);
        assert getTag() != null;
        Log.i("Info",getTag());
        if(getTag().equals("email")){
            Objects.requireNonNull(dialogBuilder).setTitle(getTag());
        //    retDialogBuilder = BuilderMethod(dialogBuilder,view,emailIn,passIN,getTag());
            dialogBuilder.setView(view)
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(TextUtils.isEmpty(Objects.requireNonNull(oldEmail.getText()).toString())
                                    ||TextUtils.isEmpty(Objects.requireNonNull(newEmail.getText()).toString())
                                    ||TextUtils.isEmpty(Objects.requireNonNull(passIN.getText()).toString())){
                                assert getTag() != null;
                                oldEmail.setError(getTag().concat(" is missing"));
                                newEmail.setError(getTag().concat(" is missing"));
                                passIN.setError("Password is missing");
                            }else {
                                String oldemail = oldEmail.getText().toString().trim();
                                String newemail = newEmail.getText().toString().trim();
                                String pass = passIN.getText().toString().trim();
                                dialogListener.emailUpdateMethod(oldemail,newemail,pass);
                            }
                        }
                    });
        }
        return dialogBuilder.create();
    }

    public interface EmailUpdateDialogListener{
        void emailUpdateMethod(String oldemail,String newemail,String pass);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            dialogListener = (EmailUpdateDialog.EmailUpdateDialogListener)getParentFragment();
        }catch (ClassCastException c){
            Log.e(TAG,"onAttach: ClassCastException".concat(Objects.requireNonNull(c.getMessage())));
        }
    }
}
