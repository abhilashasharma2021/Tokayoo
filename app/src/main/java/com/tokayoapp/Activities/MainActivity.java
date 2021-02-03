package com.tokayoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tokayoapp.Fragments.AccountFragment;
import com.tokayoapp.Fragments.CartFragment;
import com.tokayoapp.Fragments.FavoritesFragment;
import com.tokayoapp.Fragments.HomeFragment;
import com.tokayoapp.Fragments.RewardFragment;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.lang.reflect.Field;

/*huilala*/




public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    String customStatus="";
   // String backStatus="";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment = null;


        @SuppressLint("RestrictedApi")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.reward:
                    item.setCheckable(true);
                    fragment = new RewardFragment();
                  //  toolbar.setTitle("Reward");
                    showFragment(fragment);
                    return true;

                case R.id.home_page:
                    item.setCheckable(true);
                //    toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    showFragment(fragment);
                    return true;

                case R.id.nav_fav:
                    item.setCheckable(true);
                    fragment = new FavoritesFragment();
                    showFragment(fragment);
                    return true;

                case R.id.account:

                    item.setCheckable(true);
                    fragment = new AccountFragment();
                    showFragment(fragment);

                    return true;

                case R.id.cart:

                    item.setCheckable(true);
                    fragment = new CartFragment();
                    showFragment(fragment);


                    return true;

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        customStatus = AppConstant.sharedpreferences.getString(AppConstant.customStatus, "");
        String   backStatus = AppConstant.sharedpreferences.getString(AppConstant.Statusback, "");
         Log.e("fjdkfk",customStatus);
        if (customStatus.equals("1"))
        {

            Fragment fragment = new CartFragment();
            showFragment(fragment);


        }   else {

            Fragment fragment = new HomeFragment();
            showFragment(fragment);

        }



      /*  if (backStatus.equals("Account")){

            Fragment fragment = new AccountFragment();
            showFragment(fragment);

        } else {

            Fragment fragment = new HomeFragment();
            showFragment(fragment);

        }*/

        //toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        /*Fragment fragment = new HomeFragment();
        showFragment(fragment);*/




        BottomNavigationView bottomToolbar = (BottomNavigationView) findViewById(R.id.bottomToolbar);
        bottomToolbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomToolbar.getMenu().getItem(0).setCheckable(false);
        BottomNavigationViewHelper bottomNavigationViewHelper = new BottomNavigationViewHelper();
        bottomNavigationViewHelper.removeShiftMode(bottomToolbar);




    }

    class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }


    }

    private void showFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                /*  .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim ,R.anim.enter_anim, R.anim.enter_anim)*/
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }
}