package instagram.com.rogerwillian.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class FragmentHelper {

    public static void showFragment(FragmentManager fragmentManager, Fragment fragment, int viewShowId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewShowId, fragment).commit();
    }

}
