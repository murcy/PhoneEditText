package com.yellowballstudio.phoneedittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class PhoneEditText extends EditText implements TextWatcher, OnFocusChangeListener {

    private static final String PATTERN_NOT_NUMBERS = "[^0-9]*";
    private static final String PATTERN_NUMBERS = "[0-9]*";
    private static final String EMPTY = new String();

    private static final int KOD = 7;
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String SPACE = " ";

    private static final int MIN_LENGTH = 0;
    private static final int MAX_LENGTH = 11;

    private static final int PART1_LEN = 1;
    private static final int PART2_LEN = 4;
    private static final int PART3_LEN = 7;
    private static final int PART4_LEN = 9;

    private boolean mUpdated;
    private int mPosition;

    public PhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
        setOnFocusChangeListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            mUpdated = false;
            getHint();
            return;
        }
        if (mUpdated) {
            mUpdated = !mUpdated;
            return;
        }
        String number = s.toString().replaceAll(PATTERN_NOT_NUMBERS, EMPTY);
        if (number.length() > MAX_LENGTH) {
            number = new String(number.substring(0, MAX_LENGTH));
        } else if (number.length() == 0) {
            getHint();
        }
        mUpdated = !mUpdated;
        int length = number.length();
        if (length > 0) {
            String result = PLUS + KOD;
            mPosition++;
            if (length > PART1_LEN) {
                result += SPACE + OPEN_BRACKET
                        + (length >= PART2_LEN ? number.substring(PART1_LEN, PART2_LEN)
                        : number.substring(PART1_LEN));
                mPosition += 2;
                if (length > PART2_LEN) {
                    result += CLOSE_BRACKET
                            + SPACE
                            + (length >= PART3_LEN ? number.substring(PART2_LEN, PART3_LEN) : number
                            .substring(PART2_LEN));
                    mPosition += 2;
                    if (length > PART3_LEN) {
                        result += MINUS
                                + (length >= PART4_LEN ? number.substring(PART3_LEN, PART4_LEN)
                                : number
                                        .substring(PART3_LEN));
                        mPosition++;
                        if (length > PART4_LEN) {
                            result += MINUS
                                    + (length >= MAX_LENGTH ? number
                                    .substring(PART4_LEN, MAX_LENGTH) : number
                                    .substring(PART4_LEN));
                            mPosition++;
                        }
                    }
                }
            }
            setText(result);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!mUpdated) {
            mPosition = start + count - (s.toString().replaceAll(PATTERN_NUMBERS, EMPTY).length());
            mPosition = mPosition > MAX_LENGTH ? MAX_LENGTH : mPosition;
        } else {
            setSelection(mPosition);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (isEnabled()) {
            String text = getText().toString();
            if (hasFocus && text.length() == MIN_LENGTH) {
                getHint();
            } else if (text.length() == (PART1_LEN + 1)) {
                setText(EMPTY);
            }
        }
    }

}
