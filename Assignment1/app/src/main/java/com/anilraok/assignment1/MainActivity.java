package com.anilraok.assignment1;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.input_texts) EditText inputFields;
    @BindView(R.id.input_texts_container) TextInputLayout inputTextContainer;

    @BindView(R.id.from_text) EditText sourceTextField;
    @BindView(R.id.from_text_container) TextInputLayout sourceTextContainer;

    @BindView(R.id.dest_text) EditText destTextField;
    @BindView(R.id.dest_text_container) TextInputLayout destTextContainer;

    @BindView(R.id.result_view) TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.calculate_button)
    public void onCalculateClick(){

        String[] arrayOfInputs = inputFields.getText().toString().split(",");
        String sourceString = sourceTextField.getText().toString().trim();
        String destString = destTextField.getText().toString().trim();

        if(!validateInputs(arrayOfInputs, sourceString, destString))
            return;

        int shortHopToFinal = 0;

        int numberOfLettersMisMatch = 0;

        for (int i = 0; i < destString.trim().length(); i++) {
            if(sourceString.charAt(i) != destString.charAt(i))
                numberOfLettersMisMatch++;
        }

        Log.e("numberOfLettersMismatch",numberOfLettersMisMatch+"");

        for (String temp : arrayOfInputs) {
            for (int j = 0; j < arrayOfInputs.length; j++) {
                String arrayOfInput = arrayOfInputs[j].trim();

                int numberOfLettersMismatchFromSourceToTest = 0;
                for (int i = 0; i < arrayOfInput.trim().length(); i++) {
                    if (sourceString.charAt(i) != arrayOfInput.charAt(i))
                        numberOfLettersMismatchFromSourceToTest++;
                }

                if (numberOfLettersMismatchFromSourceToTest == 1){

                    int numberOfLettersMismatchIfHopped = 0;
                    for (int i = 0; i < destString.trim().length(); i++) {
                        if(arrayOfInput.charAt(i) != destString.charAt(i))
                            numberOfLettersMismatchIfHopped++;
                    }

                    if (numberOfLettersMismatchIfHopped == (numberOfLettersMisMatch-1)){
                        sourceString = arrayOfInput;
                        numberOfLettersMisMatch--;
                        shortHopToFinal++;

                        Log.e("newSourceString",sourceString);
                    }
                }
            }
        }

        if (sourceString.equals(destString)){
            resultTextView.setText(String.format("%1$d are the minimum hops",shortHopToFinal));
        }
        else {
            resultTextView.setText("Can't hop to final value");
        }
        Log.e("shortHop",shortHopToFinal+"");
    }

    /**
     *
     * @param arrayOfInputs words entered in first field
     * @param sourceString word from which we need to transition
     * @param destString word to which we need to transition to
     * @return true if validation passes
     */
    private boolean validateInputs(String[] arrayOfInputs, String sourceString, String destString) {
        boolean validationsPassed = true;

        if (arrayOfInputs.length == 0){
            validationsPassed = false;
            inputTextContainer.setError("Enter multiple words each having 3 characters separated by commas");
        }
        else {

            if (arrayOfInputs.length == 1 && arrayOfInputs[0].trim().length() == 0){
                validationsPassed = false;
                inputTextContainer.setError("Enter multiple words each having 3 characters separated by commas");
            }
            else {
                for (String arrayOfInput : arrayOfInputs) {
                    if (arrayOfInput.trim().length() != 3){
                        validationsPassed = false;
                        inputTextContainer.setError("Each word must have exactly 3 characters");
                        break;
                    }
                }
            }

            if (validationsPassed)
                inputTextContainer.setError("");
        }

        if (sourceString.length() != 3){
            validationsPassed = false;
            sourceTextContainer.setError("Word must have 3 characters");
        }
        else if (!inputFields.getText().toString().contains(sourceString)){
            validationsPassed = false;
            sourceTextContainer.setError("word must be present in the original list");
        }
        else
            sourceTextContainer.setError("");

        if (destString.length() != 3){
            validationsPassed = false;
            destTextContainer.setError("Word must have 3 characters");
        }
        else if (!inputFields.getText().toString().contains(destString)){
            validationsPassed = false;
            destTextContainer.setError("word must be present in the original list");
        }
        else
            destTextContainer.setError("");

        return validationsPassed;
    }
}
