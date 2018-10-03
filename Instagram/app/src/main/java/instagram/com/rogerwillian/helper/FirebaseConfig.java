package instagram.com.rogerwillian.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseConfig {

    private static FirebaseAuth auth;
    private static DatabaseReference firebase;
    private static StorageReference firebaseStorage;

    public static DatabaseReference getFirebaseDatabase() {
        if (firebase == null)
            firebase = FirebaseDatabase.getInstance().getReference();
        return firebase;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null)
            auth = FirebaseAuth.getInstance();
        return auth;
    }

    public static StorageReference getFirebaseStorage() {
        if (firebaseStorage == null)
            firebaseStorage = FirebaseStorage.getInstance().getReference();
        return firebaseStorage;
    }
}
