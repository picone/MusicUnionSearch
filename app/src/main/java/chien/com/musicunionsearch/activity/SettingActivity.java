package chien.com.musicunionsearch.activity;

import android.preference.PreferenceActivity;

import java.util.List;

import chien.com.musicunionsearch.R;
import chien.com.musicunionsearch.fragment.SettingFragment;

public class SettingActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_header, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingFragment.class.getName().equals(fragmentName);
    }
}
