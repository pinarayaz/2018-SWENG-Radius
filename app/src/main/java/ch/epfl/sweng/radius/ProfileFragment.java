package ch.epfl.sweng.radius;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


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
            radiusBar.setProgress(savedInstanceState.getInt("radius", 50));//radiusBar.getProgress();  //50 is the default value of the slider - might want to put a constant for it later
            radiusValue.setText(progress + " Km");
        } else {
            int progress = radiusBar.getProgress();
            radiusValue.setText(progress + "Km");
        }

        Fragment homeFragment = HomeFragment.newInstance(radiusBar.getProgress());
        //Fragment homeFragment = HomeFragment.newInstance(radiusBar.getProgress());
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);

        //outstate.putString("radiusValue", radiusValue.getText().toString());
        outstate.putInt("radius", radiusBar.getProgress());

        /*Bundle profileData = new Bundle();
        Fragment homeFragment = new HomeFragment();
        profileData.putDouble("radiusValue", radiusBar.getProgress());
        homeFragment.setArguments(profileData);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fcontainer, homeFragment).addToBackStack(null).commit();*/
    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb

            radiusValue.setText(progress + " Km");
            Fragment homeFragment = HomeFragment.newInstance(radiusBar.getProgress());
            /*Bundle profileData = new Bundle();
            Fragment homeFragment = new HomeFragment();
            profileData.putDouble("radiusValue", radiusBar.getProgress());
            homeFragment.setArguments(profileData);*/
            //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.fcontainer, homeFragment).addToBackStack(null).commit();
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
