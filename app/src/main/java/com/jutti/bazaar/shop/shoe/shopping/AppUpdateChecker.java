package com.jutti.bazaar.shop.shoe.shopping;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.jsoup.Jsoup;


public class AppUpdateChecker {
    private Activity activity;
    public AppUpdateChecker(Activity activity) {
        this.activity = activity;
    }
    //current version of app installed in the device
    private String getCurrentVersion(){
        PackageManager pm = activity.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(activity.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return pInfo.versionName;
    }
    private class GetLatestVersion extends AsyncTask<String, String, String> {
        private String latestVersion;
        private ProgressDialog progressDialog;
        private boolean manualCheck;
        GetLatestVersion(boolean manualCheck) {
            this.manualCheck = manualCheck;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (manualCheck)
            {
                if (progressDialog!=null)
                {
                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                }
            }
            String currentVersion = getCurrentVersion();
            //If the versions are not the same
            if(!currentVersion.equals(latestVersion)&&latestVersion!=null){

                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.dialogue_custom);
                dialog.show();
                dialog.setCancelable(false);
                TextView tv_dial_title = (TextView) dialog.findViewById(R.id.tv_dial_title);
                TextView tv_dial_subtitle = (TextView) dialog.findViewById(R.id.tv_dial_subtitle);
                TextView tv_dial_desc = (TextView) dialog.findViewById(R.id.tv_dial_desc);
                android.widget.ImageView img_dial = (android.widget.ImageView) dialog.findViewById(R.id.img_dial);


                /******************  Custom Dialogue ***********/
                TextView tv_action1 = (TextView) dialog.findViewById(R.id.tv_action1);
                TextView tv_action2 = (TextView) dialog.findViewById(R.id.tv_action2);


                tv_dial_title.setText(" Update to Continue");
                tv_dial_subtitle.setText("An Important Update is Available");
                tv_dial_desc.setText("To Improve your Experience and Compatibilty, We have updated our app, Please Update to Continue.");
                tv_action1.setText("Update");
                tv_action2.setText("Exit");
                img_dial.setImageResource(R.drawable.ic_notification);

                tv_action1.setBackgroundColor(Color.parseColor("#1EBEA5"));
                tv_action2.setBackgroundColor(Color.parseColor("#FFA1A1A1"));


                tv_action1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+activity.getPackageName())));
                        dialog.dismiss();
                    }
                });

                tv_action2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });



              /*  final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("An Update is Available");
                builder.setMessage("Its better to update now");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action

                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+activity.getPackageName())));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel button action
                        activity.finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();
           */ }else {
                if (manualCheck) {

                    Toast.makeText(activity, "No Update Available", Toast.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (manualCheck) {
                progressDialog=new ProgressDialog(activity);
                progressDialog.setMessage("Checking For Update.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return latestVersion;
            } catch (Exception e) {
                return latestVersion;
            }
        }
    }
    public void checkForUpdate(boolean manualCheck)
    {
        new GetLatestVersion(manualCheck).execute();
    }


}