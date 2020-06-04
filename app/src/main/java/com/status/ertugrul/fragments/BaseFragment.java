package com.status.ertugrul.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.status.ertugrul.R;
import com.status.ertugrul.utilities.SuccessFailureInterface;

public class BaseFragment extends Fragment {

    Dialog dialog;

    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }


    public void showNoInternetDialog(final Activity context, String message, String button_txt) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_alert_dialog);
        dialog.setCancelable(false);
        LinearLayout dialog_layout = dialog.findViewById(R.id.dialog_layout);
        dialog_layout.setBackgroundColor(context.getResources().getColor(R.color.white));


        if (!context.isFinishing()) {
            //show dialog
            dialog.show();
        }
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        ImageView image = dialog.findViewById(R.id.image);
        ImageView image_internet = dialog.findViewById(R.id.image_internet);

        image.setVisibility(View.GONE);
        image_internet.setVisibility(View.VISIBLE);

        RelativeLayout rlparent = dialog.findViewById(R.id.rlParent);
        rlparent.getBackground().setAlpha(100);
        Button btnOkay = dialog.findViewById(R.id.btnOkay);
        tvMessage.setText(message);
        btnOkay.setText(button_txt);
        GradientDrawable gd = (GradientDrawable) btnOkay.getBackground();
//To shange the solid color
        gd.setColor(context.getResources().getColor(R.color.blue));
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }

    protected ProgressDialog progressDialog;

    protected void showProgressDialog(String message) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }


    protected void hideprogressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }



    protected void genaricDialog(Context context, String title, final String message, String button_main_text, String button_close_text, final SuccessFailureInterface anInterface) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_general_message);
        dialog.setCancelable(false);
        if (!((AppCompatActivity) context).isFinishing()) {
            //show dialog
            dialog.show();
        }

        RelativeLayout rlparent = dialog.findViewById(R.id.rlParent);
        rlparent.getBackground().setAlpha(100);
        TextView alertTitle = dialog.findViewById(R.id.tvTitle);
        TextView alertMessage = dialog.findViewById(R.id.tvMessage);
        TextView tvYes = dialog.findViewById(R.id.tvYes);
        TextView tvNo = dialog.findViewById(R.id.tvNo);

        CardView btnTwo = dialog.findViewById(R.id.btnCancel);
        CardView btnOne = dialog.findViewById(R.id.btnOk);
        btnTwo.setVisibility(View.GONE);

        alertTitle.setText(title);
        alertMessage.setText(message);

        tvYes.setText(button_main_text);
        if (button_close_text.isEmpty())
            btnTwo.setVisibility(View.GONE);
        else
            tvNo.setText(button_close_text);


        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ScreenUtils.hideSoftKeyboard(BaseActivity.this);
                dialog.dismiss();
                if (anInterface != null)
                    anInterface.onSuccess();

            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ScreenUtils.hideSoftKeyboard(BaseActivity.this);
                dialog.dismiss();
                if (anInterface != null)
                    anInterface.onFailure();

            }
        });
    }

}
