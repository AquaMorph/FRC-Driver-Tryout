package com.cacolglazier.frcdrivertryout;

import android.content.res.Configuration;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t1;
    Button lapButton;
    Button resetButton;
    TextView lapsLeft;
    String[][] number = {{"one", "first"}, {"two", "second"}, {"three", "third"}};
    final int totalNumPath = 3;
    final int totalTraversePath = 6;
    ArrayList<Integer> path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lapButton = findViewById(R.id.lapbutton);
        resetButton = findViewById(R.id.resetbutton);
        lapsLeft = findViewById(R.id.lapsleft);

        reset();

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                    t1.setSpeechRate(1.3f);
                }
            }
        }, "com.google.android.tts");

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path.size() > 0) {
                    t1.speak(getPhrase(), TextToSpeech.QUEUE_FLUSH, null);
                    updateDisplay();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private String getPhrase() {
        String[] pathText = getPathNumber();
        String[] phrase = {"Go through route " + pathText[0],
                "Take path " + pathText[0],
                "Go through " + pathText[0],
                pathText[0], "Go " + pathText[0],
                "Take the " + pathText[1] + " path",
                pathText[1] + " route"};
        int randomNum = ThreadLocalRandom.current().nextInt(0, phrase.length);
        Log.i("Message", phrase[randomNum]);
        return phrase[randomNum];
    }

    private String[] getPathNumber() {
        Log.i("Main", Integer.toString(path.get(0)));
        String[] text = number[path.get(0)];
        path.remove(0);
        return text;
    }

    private void reset() {
        path = new ArrayList<>();
        for(int i = 0; i < totalTraversePath; i++) {
            path.add(i % totalNumPath);
        }
        Collections.shuffle(path);
        updateDisplay();
    }

    private void updateDisplay() {
        lapsLeft.setText(String.valueOf(path.size()));
    }
}
