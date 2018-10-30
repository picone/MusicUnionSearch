package chien.com.musicunionsearch.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import chien.com.musicunionsearch.R;
import chien.com.musicunionsearch.fragment.SettingFragment;

@EActivity(R.layout.activity_setting)
public class SettingActivity extends AppCompatActivity {

    @AfterViews
    public void afterView() {
        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new SettingFragment());
        ft.commit();
    }
}
