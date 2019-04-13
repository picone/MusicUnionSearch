package chien.com.musicunionsearch.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.leon.lfilepickerlibrary.LFilePicker;

import chien.com.musicunionsearch.R;

public class SettingFragment extends PreferenceFragment {

    private static final int REQUEST_SELECT_FILE_PATH = 1;
    private Preference downloadPathPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_base);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        String defaultDownloadPath = Environment.DIRECTORY_DOWNLOADS;
        if (defaultDownloadPath == null) {
            defaultDownloadPath = getString(R.string.preference_download_path_default);
        }
        downloadPathPreference = findPreference(getString(R.string.preference_download_path_key));
        downloadPathPreference.setDefaultValue(defaultDownloadPath);
        downloadPathPreference.setSummary(sharedPreferences.getString(getString(R.string.preference_download_path_key), defaultDownloadPath));
        downloadPathPreference = findPreference(getString(R.string.preference_download_path_key));
        downloadPathPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new LFilePicker()
                        .withFragment(SettingFragment.this)
                        .withMutilyMode(false)
                        .withChooseMode(false)
                        .withMaxNum(1)
                        .withRequestCode(REQUEST_SELECT_FILE_PATH)
                        .start();
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE_PATH && data != null) {
            String path = data.getStringExtra("path");
            if (!TextUtils.isEmpty(path)) {
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                editor.putString(getString(R.string.preference_download_path_key), path);
                editor.apply();
                downloadPathPreference.setSummary(path);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
