package com.example.gnumber;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private TextView log;
    private EditText input;
    private String answer;
    private int counter;
    private int dig = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = findViewById(R.id.log);
        input = findViewById(R.id.input);

        initGame();

    }

    public void guess(View view) {
        counter++;
        String gText = input.getText().toString();
        String result = checkAB(gText);

        if(result.equals(dig + "A0B")){
            //Win
            showResultDialog(true);
        }else if(counter == 10){
            //Lose
            showResultDialog(false);
        }else{
            String mesg = counter + ". " + gText + ":" + result;
            showMessage(mesg);
            log.append(mesg + "\n");
            input.setText("");
        }

    }

    private void initGame(){
        counter = 0;
        answer = createAnswer(dig);
        Log.v("bradlog",answer);
        log.setText("");
        input.setText("");
    }

    private void showMessage(String mesg){
        new AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(mesg)
                .create()
                .show();
    }

    private void showResultDialog(boolean isWin){
        new AlertDialog.Builder(this)
                .setTitle(isWin?"Winner":"Lose")
                .setMessage(isWin?"恭喜":"answer = " + answer)
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initGame();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    private String checkAB(String g){
        int a,b; a=0; b=0;

        for(int i=0;i<answer.length();i++){
            if(answer.charAt(i)==g.charAt(i)){
                a++;
            }else if(answer.contains("" + g.charAt(i))){
                b++;
            }
        }

        return String.format("%dA%dB",a,b);
    }

    private String createAnswer(int d){
        HashSet<Integer> nums = new HashSet<>();
        while(nums.size()<d){
            nums.add((int)(Math.random()*10));
        }
        LinkedList<Integer> list = new LinkedList<>();
        for(Integer i:nums){
            list.add(i);
        }
        Collections.shuffle(list);
        StringBuffer sb = new StringBuffer();
        for(Integer i:list){
            sb.append(i);
        }
        return sb.toString();
    }

    public void exit(View view) {
        Log.v("bradlog","exit");
        new AlertDialog.Builder(this)
                .setMessage("Sure to Exit ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .setCancelable(false)
                .create()
                .show();
    }

    private long lastBackTime = 0;
    @Override
    public void onBackPressed() {
        Log.v("bradlog","onBackPrecess");
        if(System.currentTimeMillis() - lastBackTime < 3*1000) {
            super.onBackPressed();
        }else{
            lastBackTime = System.currentTimeMillis();
            Toast.makeText(this,"Please back again to exit ! ",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        Log.v("bradlog","finish");
        super.finish();
    }

    public void restartGame(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Restart Game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initGame();
                    }
                })
                .setNegativeButton("No",null)
                .setCancelable(false)
                .create()
                .show();
    }

    String[] items = {"2","3","4","5"};
    int tempWhich;
    public void setting(View view) {
        tempWhich = dig - 2;
        new AlertDialog.Builder(this)
                .setTitle("Setting")
                .setSingleChoiceItems(items, dig-2  , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.v("bradlog","which = "+ which);
                        tempWhich = which;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dig = tempWhich + 2;
                        Log.v("bradlog","dig = " + dig);
                    }
                })
                .setNegativeButton("Cancel",null)
                .setCancelable(false)
                .create()
                .show();
    }
}