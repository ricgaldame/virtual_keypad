package com.goodyear.virtual_keypad;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class MhhKeypad extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard keyboard2;

    private boolean isCaps = false;

    boolean cuurentKeyboard;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this,R.xml.qwerty);
        keyboard2 = new Keyboard(this,R.xml.qwerty2);

        kv.setKeyboard(keyboard2);
        kv.setOnKeyboardActionListener(this);

        cuurentKeyboard = false;

        return kv;
    }

    @Override
    public void onPress(int i) {
        //Toast.makeText(this,String.valueOf(i),Toast.LENGTH_SHORT).show();
        //playClick(i);
    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        //Toast.makeText(this,"Button onKey",Toast.LENGTH_SHORT).show();
        //System.out.println(i);
        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        switch (i){
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));
                break;
            case -6:
                changeKeyboard();
                break;
            default:
                char code = (char)i;
                if(Character.isLetter(code) && isCaps)
                    code = Character.toUpperCase(code);
                ic.commitText(String.valueOf(code),1);

        }

    }

    private void playClick(int i) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (i){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void changeKeyboard() {
        cuurentKeyboard = !cuurentKeyboard;
        if(cuurentKeyboard){
            kv.setKeyboard(keyboard);
        }
        else{
            kv.setKeyboard(keyboard2);
        }
    }
}