package com.interlinkj.android.remotecamera;

import java.util.Set;

import android.bluetooth.*;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class CameraSetting extends PreferenceActivity {
	public static final String RECENT_DEVICE_PREF_KEY = "device";		// 接続デバイス設定キー
	public static final String SAVE_PATH_PREF_KEY = "save_path";		// 保存先パス設定キー
	public static final String FILENAME_FMT_PREF_KEY = "format";		// 保存ファイル名書式設定キー

	private BluetoothAdapter mAdapter;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.preference);

		mAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> devices = mAdapter.getBondedDevices();
		String[] names = new String[devices.size()];
		String[] addrs = new String[devices.size()];

		ListPreference lp = (ListPreference)findPreference(RECENT_DEVICE_PREF_KEY);
		lp.setOnPreferenceChangeListener(mOnDeviceChange);
		int i = 0;
		for(BluetoothDevice device : devices) {
			names[i] = device.getName();
			addrs[i] = device.getAddress();
			i++;
		}
		lp.setEntries(names);
		lp.setEntryValues(addrs);
		String lpt = (String)lp.getEntry();
		if(lpt != null) {
			lp.setSummary(lpt);
		}

		EditTextPreference pathPref = (EditTextPreference)findPreference(SAVE_PATH_PREF_KEY);
		pathPref.setOnPreferenceChangeListener(mOnEditTextChange);
		String ept = pathPref.getText();
		if(ept != null) {
			pathPref.setSummary(ept);
		}

		EditTextPreference fmtPref = (EditTextPreference)findPreference(FILENAME_FMT_PREF_KEY);
		fmtPref.setOnPreferenceChangeListener(mOnEditTextChange);
		String fmtPrefText = fmtPref.getText();
		if(fmtPrefText != null) {
			fmtPref.setSummary(fmtPrefText);
		}

		setResult(RESULT_OK);
	}

	private OnPreferenceChangeListener mOnDeviceChange = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if(null != newValue) {
				preference.setSummary(mAdapter
						.getRemoteDevice((String)newValue).getName());
				return true;
			}
			return false;
		}
	};

	// 設定変更後にサマリに値を表示する
	private OnPreferenceChangeListener mOnEditTextChange = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if(null != newValue) {
				preference.setSummary((CharSequence)newValue);
				return true;
			}
			return false;
		}
	};
}
