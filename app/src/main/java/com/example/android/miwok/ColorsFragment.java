package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    // Declare mMediaPlayer variable to be used in application with MediaPlayer
    private MediaPlayer mMediaPlayer;

    // Declare mAudioManager variable to be used in application with AudioManager
    private AudioManager audioManager;

    // A listener that allows app to understand if there were any audio focus changes.
    // If there are any audio focus changes, the application responds accordingly
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            // If the focusChange is short-term or it can be ducked,
            // the MediaPlayer will pause. Due to the short audio files in this application,
            // the MediaPlayer will reset to the beginning.
            if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            }
            // Else, if the focus comes back to the application, the MediaPlayer will stop
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            }
            // Else, if the focus is lost, the MediaPlayer will be released.
            // See more in method: releaseMediaPlayer()
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    // OnCompletionListener that becomes active once the media has finished playing
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            // See this method at the bottom of the code
            releaseMediaPlayer();
        }
    };


    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize a new view that inflates the layout containing the listView
        // This listView will be populated once it is connected to the WordAdapter
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Initialize audioManager variable so that it can be used to evaluate audio in the application
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_colors);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               // Releases the media player before we begin playing a new song so that we ensure there is enough memory to play the new song
                // and that there is nothing playing over it.
                releaseMediaPlayer();

                // Request the audio focus
                // Initialized to either AUDIOFOCUS_REQUEST_GRANTED or AUDIOFOCUS_REQUEST_FAILED
                int access = audioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                // If we are granted access, begin the audio file and set the completion listener
                if (access == AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(getActivity(), words.get(i).getAudioResouceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        // If the application is stopped, we need to release the MediaPlayer so that audio stops playing out of the users device
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the MediaPlayer is not empty, we need to release it so that we provide a quality user experience
        if (mMediaPlayer != null) {
            // releases the MediaPlayer
            mMediaPlayer.release();
            // sets the MediaPlayer to null so that it can be filled with a new audio file
            mMediaPlayer = null;
            // abandons audioFocus allowing other applications to pick up the phone's audio, if necessary
            audioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }

}
