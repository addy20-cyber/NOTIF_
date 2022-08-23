package com.mad.notif;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = MyReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    TextToSpeech textToSpeech;

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "",r = "",strSender;
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];

            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }


                // Build the message to show.
                strMessage += msgs[i].getMessageBody();
                strSender = msgs[i].getOriginatingAddress();




                String[] mssgs = strMessage.split(" ");

                if(strMessage.contains("BOI")) {
                    for (String s : mssgs) {
                        if ((strMessage.contains("credited") || strMessage.contains("Credited")) & s.contains("Rs")) {
                            String p = s.replace("Rs.", "");
                            String q = p.replace(".00", "");
                            r = "Received " + q + " rupees";
                            Toast.makeText(context.getApplicationContext(), "Received " + q + " rupees", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if ((strMessage.contains("debited") || strMessage.contains("Debited")) && s.contains("Rs")) {
                            String p = s.replace("Rs.", "");
                            String q = p.replace(".00", "");
                            r = "Debited " + q + " rupees";
                            Toast.makeText(context.getApplicationContext(), "Debited " + q + " rupees", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                if(strMessage.contains("SBI")) {
                    for (String s : mssgs) {
                        if ((strMessage.contains("credited") || strMessage.contains("Credited")) & s.contains("Rs")) {
                            String p = s.replace("Rs.", "");
                            String q = p.replace(".00", "");
                            r = "Received " + q + " rupees";
                            Toast.makeText(context.getApplicationContext(), "Received " + q + " rupees", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if ((strMessage.contains("debited") || strMessage.contains("Debited")) && s.contains("Rs")) {
                            String p = s.replace("Rs.", "");
                            String q = p.replace(".00", "");
                            r = "Debited " + q + " rupees";
                            Toast.makeText(context.getApplicationContext(), "Debited " + q + " rupees", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                if(strMessage.contains("HDFC")) {
                    int ii=0;
                    if(strMessage.contains("credited") || strMessage.contains("Credited")) {
                        for (String s : mssgs) {
                            if (s.contains("Rs")) {
                                String q = mssgs[ii+1].replace(".00","");
                                r = "Received "+q+" rupees";
                                Toast.makeText(context.getApplicationContext(), "Received "+q+" rupees", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            ii++;
                        }
                    }
                    if((strMessage.contains("debited") || strMessage.contains("Debited"))){
                        for (String s : mssgs) {
                            if (s.contains("Rs")) {
                                String q = mssgs[ii+1].replace(".00","");
                                r = "Debited "+q+" rupees";
                                Toast.makeText(context.getApplicationContext(), "Debited "+q+" rupees", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            ii++;
                        }
                    }
                }

                if(strMessage.contains("IDBI")) {
                    int ii=0;
                    if(strMessage.contains("credited") || strMessage.contains("Credited")) {
                        for (String s : mssgs) {
                            if (s.contains("INR")) {
                                String q = mssgs[ii+1].replace(".00","");
                                r = "Received "+q+" rupees";
                                Toast.makeText(context.getApplicationContext(), "Received "+q+" rupees", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            ii++;
                        }
                    }
                    if((strMessage.contains("debited") || strMessage.contains("Debited"))){
                        for (String s : mssgs) {
                            if (s.contains("INR")) {
                                String q = mssgs[ii+1].replace(".00","");
                                r = "Debited "+q+" rupees";
                                Toast.makeText(context.getApplicationContext(), "Debited "+q+" rupees", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            ii++;
                        }
                    }
                }



                String finalR = r;
                textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i!=TextToSpeech.ERROR){
                            textToSpeech.setLanguage(Locale.UK);
                            textToSpeech.speak(finalR,TextToSpeech.QUEUE_FLUSH,null);
                        }
                    }
                });
            }
        }
    }
}