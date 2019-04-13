package chien.com.musicunionsearch.activity;

import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.leon.lfilepickerlibrary.ui.LFilePickerActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import chien.com.musicunionsearch.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SettingActivityTest {

    @Rule
    public IntentsTestRule<SettingActivity> settingActivity = new IntentsTestRule<>(SettingActivity.class);

    @Test
    public void testSetDownloadPath() {
        onView(withText(R.string.preference_title_base)).perform(click());
        onView(withText(R.string.preference_download_path))
                .perform(click());
        intended(allOf(hasComponent(LFilePickerActivity.class.getName()), hasExtraWithKey("param")));
        onView(withId(R.id.btn_addbook)).perform(click());
    }
}
