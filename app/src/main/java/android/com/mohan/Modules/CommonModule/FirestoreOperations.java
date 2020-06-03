package android.com.mohan.Modules.CommonModule;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;
import java.util.Objects;

public class FirestoreOperations {
    private FirebaseFirestore firestore;
    private DocumentReference docRef;
    public FirestoreOperations() {
    }
    public void addData(String ColID,String DocID, Map<String,Object> data){
        docRef = firestore.collection(ColID).document(DocID);
        docRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Info: ","Added data");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Info: ", Objects.requireNonNull(e.getMessage()));
            }
        });
    }
    private  Map<String, Object> Data;
    private void setData(Map<String, Object> data){
        this.Data = data;
    }
    public Map<String, Object> getData(){
        return this.Data;
    }
    boolean readData(String ColID, String DocID){
        final boolean[] isAvailable = {false};
        docRef = firestore.collection(ColID).document(DocID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                if(documentSnapshot.exists()){
                    setData(documentSnapshot.getData());
                    isAvailable[0] = true;
                }
            }
        });
        return isAvailable[0];
    }
    public void updateData(String ColID,String DocID,Map<String, Object> data){
        docRef = firestore.collection(ColID).document(DocID);
        docRef.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Update: ","Successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("UpdateError: ","Error updating "+e.getMessage());
            }
        });
    }
    public void deleteData(String ColID,String DocID,Map<String, Object> data){
        firestore.collection(ColID).document(DocID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Deletion: ","Successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DeleteError:","Error deleting "+ e.getMessage());
            }
        });
    }


}
