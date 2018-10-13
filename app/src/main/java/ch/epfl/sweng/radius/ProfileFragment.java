package ch.epfl.sweng.radius;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import android.content.Context;
/*import android.content.res.Resources;
import android.content.*/


public class ProfileFragment extends Fragment {
    
    ImageView userPhoto;
    ImageButton changeProfilePictureButton;
    TextView userNickname;
    TextView userStatus;
    TextInputEditText statusInput;
    TextInputEditText nicknameInput;
    SeekBar radiusBar;
    TextView radiusValue;
    MaterialButton saveButton;
    private Button selectLanguagesButton;
    private ArrayList<String> selectableLanguages;
    private boolean[] checkedLanguages;
    private ArrayList<Integer> spokenLanguages;
    private TextView selectedLanguages;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // set a change listener on the SeekBar
        radiusBar = view.findViewById(R.id.radiusBar);
        radiusBar.setOnSeekBarChangeListener(seekBarChangeListener);
        radiusValue = view.findViewById(R.id.radiusValue);

        if ( savedInstanceState != null) {
            int progress = savedInstanceState.getInt("radius", radiusBar.getProgress());
            radiusBar.setProgress(savedInstanceState.getInt("radius", 50));
            radiusValue.setText(progress + " Km");
            selectableLanguages = savedInstanceState.getStringArrayList("languageList");
            spokenLanguages = savedInstanceState.getIntegerArrayList("spokenLanguages");
        } else {
            int progress = radiusBar.getProgress();
            radiusValue.setText(progress + "Km");
            selectableLanguages = readLanguagesFromFile();
            spokenLanguages = new ArrayList<Integer>();
        }

        selectedLanguages =  view.findViewById(R.id.spokenLanguages);
        checkedLanguages = new boolean[selectableLanguages.size()];
        selectLanguagesButton = view.findViewById(R.id.languagesButton);
        selectLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pick the languages you speak");
                builder.setMultiChoiceItems(selectableLanguages.toArray(new String[ProfileFragment.this.selectableLanguages.size()]), checkedLanguages, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!spokenLanguages.contains(position)) {
                                spokenLanguages.add(position);
                            }
                        } else if (spokenLanguages.contains(position)) {
                            spokenLanguages.remove(position);
                        }
                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < spokenLanguages.size(); i++) {
                            item = item + selectableLanguages.get(spokenLanguages.get(i));

                            if (i != spokenLanguages.size() - 1) {
                                item = item + ", ";
                            }
                        }

                        selectedLanguages.setText(item);
                    }
                });
                builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton(R.string.clearAll_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedLanguages.length; i++) {
                            checkedLanguages[i] = false;
                        }
                        spokenLanguages.clear();
                        selectedLanguages.setText("");
                    }
                });
                AlertDialog languageDialog = builder.create();
                languageDialog.show();
            }
        });

        Fragment homeFragment = HomeFragment.newInstance(radiusBar.getProgress());
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);

        outstate.putInt("radius", radiusBar.getProgress());
        outstate.putStringArrayList("languageList", selectableLanguages);
        outstate.putIntegerArrayList("spokenLanguages", spokenLanguages);
    }

    private ArrayList<String> readLanguagesFromFile() {
        try {

            InputStream inputStream = getActivity().getResources().openRawResource(R.raw.languages);
            ArrayList<String> languages = new ArrayList<String>();
            // File f = new File("languages.txt");
            Scanner scan = new Scanner(inputStream);

            while (scan.hasNext()) {
                languages.add(scan.nextLine().trim());
            }

            return languages;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            radiusValue.setText(progress + " Km");
            Fragment homeFragment = HomeFragment.newInstance(radiusBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
}
