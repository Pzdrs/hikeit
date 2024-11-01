package cz.pycrs.hikeit.ui.settings;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Locale;

import cz.pycrs.hikeit.R;

public class SettingsFragment extends Fragment {


    private Spinner languageSpinner;
    private Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        this.saveButton = root.findViewById(R.id.settings_save_button);

        setupSpinner(root);

        saveButton.setOnClickListener(v -> {
            String selectedLanguage = (String) languageSpinner.getSelectedItem();
            if (List.of("English", "Anglicky").contains(selectedLanguage)) {
                setLocale("en");
            } else if (List.of("Czech", "Česky").contains(selectedLanguage)) {
                setLocale("cs");
            }
        });

        return root;
    }

    private void setLocale(String localeName) {
        Locale locale = new Locale(localeName);
        requireActivity().getResources().getConfiguration().setLocale(locale);

        requireActivity().recreate();
    }

    private void setupSpinner(View root) {
        languageSpinner = root.findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // initial
        String currentLanguage = Locale.getDefault().getLanguage();
        if (currentLanguage.equals("en")) {
            languageSpinner.setSelection(0);
        } else if (currentLanguage.equals("cs")) {
            languageSpinner.setSelection(1);
        }
    }
}