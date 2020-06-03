package android.com.mohan.Modules.CommonModule.FragmentDialogComponents;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.com.mohan.R;
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
import androidx.appcompat.app.AppCompatDialogFragment;


import java.util.Objects;

import static android.content.ContentValues.TAG;


public class AccountDialog extends AppCompatDialogFragment {
    private AccountDialogListener dialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        AlertDialog retDialogBuilder;
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.layout_dialog_user, null);

        EditText updateUsername = view.findViewById(R.id.dialog_username);
        EditText updateRollNo = view.findViewById(R.id.dialog_rollno);
        EditText updatePhone = view.findViewById(R.id.dialog_phone);
        assert getTag() != null;
        Log.i("Info",getTag());
        switch (getTag()) {
            case "username":
                Objects.requireNonNull(dialogBuilder).setTitle(getTag());
                updatePhone.setVisibility(View.GONE);
                updateRollNo.setVisibility(View.GONE);
                retDialogBuilder = BuilderMethod(dialogBuilder,view,updateUsername,getTag());
                break;
            case "phone":
                Objects.requireNonNull(dialogBuilder).setTitle(getTag());
                updateUsername.setVisibility(View.GONE);
                updateRollNo.setVisibility(View.GONE);
                retDialogBuilder = BuilderMethod(dialogBuilder,view,updatePhone,getTag());
                break;
            default:
                Objects.requireNonNull(dialogBuilder).setTitle(getTag());
                updatePhone.setVisibility(View.GONE);
                updateUsername.setVisibility(View.GONE);
                retDialogBuilder = BuilderMethod(dialogBuilder,view,updateRollNo,getTag());
                break;
        }

        //updateUsername.setVisibility(View.GONE);
        return retDialogBuilder;
    }

    private AlertDialog BuilderMethod(final AlertDialog.Builder dialogBuilder, View view, final EditText updateUsername, final String tag) {
        dialogBuilder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(updateUsername.getText().toString())){
                            assert getTag() != null;
                            updateUsername.setError(getTag().concat(" is missing"));
                        }else {
                            String userdata = updateUsername.getText().toString().trim();
                            dialogListener.textUpdaterMethod(userdata,tag);
                        }
                    }
                });
        return dialogBuilder.create();
    }


    public interface AccountDialogListener{
        void textUpdaterMethod(String str,String tag);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            dialogListener = (AccountDialogListener)getParentFragment();
        }catch (ClassCastException c){
            Log.e(TAG,"onAttach: ClassCastException".concat(Objects.requireNonNull(c.getMessage())));
        }
    }
}
