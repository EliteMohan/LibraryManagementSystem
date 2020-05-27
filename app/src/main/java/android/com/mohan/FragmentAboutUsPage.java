package android.com.mohan;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.net.URL;
import java.util.Objects;

public class FragmentAboutUsPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us,container,false);
        MaterialTextView devTitle = view.findViewById(R.id.devTitleID);
        AppCompatImageView mohan,mounika,jahnavi,mahesh,jhansi;
        mohan = view.findViewById(R.id.mohan_id);
        mounika = view.findViewById(R.id.mounika_id);
        jahnavi = view.findViewById(R.id.jahnavi_id);
        mahesh = view.findViewById(R.id.mahesh_id);
        jhansi = view.findViewById(R.id.jhansi_id);
        String title = "Siddharth Library v1.0";
        devTitle.setText(title);
        Glide.with(Objects.requireNonNull(getActivity())).load("https://firebasestorage.googleapis.com/v0/b/loginandregister-5ad14.appspot.com/o/mohan.jpg?alt=media&token=16edef73-8a45-4ba9-b231-8a18c47bed77").into(mohan);
        Glide.with(Objects.requireNonNull(getActivity())).load("https://firebasestorage.googleapis.com/v0/b/loginandregister-5ad14.appspot.com/o/Mounika.jpg?alt=media&token=83de81a5-883d-46ce-9b8b-a9ce99a93578").into(mounika);
        Glide.with(Objects.requireNonNull(getActivity())).load("https://firebasestorage.googleapis.com/v0/b/loginandregister-5ad14.appspot.com/o/Mahesh.jpg?alt=media&token=105a5e3d-df6f-4936-9a95-bbbdfd193eaf").into(mahesh);
        Glide.with(Objects.requireNonNull(getActivity())).load("https://firebasestorage.googleapis.com/v0/b/loginandregister-5ad14.appspot.com/o/jhansi.jpg?alt=media&token=e15067f0-9294-4b72-ae17-23255f41b8e5").into(jhansi);
        Glide.with(Objects.requireNonNull(getActivity())).load("https://firebasestorage.googleapis.com/v0/b/loginandregister-5ad14.appspot.com/o/Jahnavi.jpg?alt=media&token=67c35915-4704-4c1f-8a33-4c201ac04452").into(jahnavi);
        return view;
    }
}
