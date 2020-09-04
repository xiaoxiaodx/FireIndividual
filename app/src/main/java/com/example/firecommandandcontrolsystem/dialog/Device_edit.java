package com.example.firecommandandcontrolsystem.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.firecommandandcontrolsystem.R;

public class Device_edit extends Dialog {

    public String edit_name;
    public String edit_group;
    public String edit_deviceid;
    public int selectindexColor;

    public Device_edit(Context context) {
        super(context);

    }

    public static class Builder {
        private Context context;
        private String positiveButtonText;
        private String negativeButtonText;

        private String mtitle ;
        private String mname;
        private String mgroup;
        private String mid;
        private int mcolorindex = -1;

        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }


        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setName(String name){
            mname = name;
            return this;
        }
        public Builder setTitle(String title){
            mtitle = title;
            return this;
        }
        public Builder setGroup(String group){
            mgroup = group;
            return this;
        }
        public Builder setColorSelect(int index){
            mcolorindex = index;
            return this;
        }
        public Builder setDeviceID(int id){
            mid  = String.valueOf(id);
            return this;
        }
        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Device_edit create() {
            if(context == null)
                return null;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final Device_edit dialog = new Device_edit(context);
            final View layout = inflater.inflate(R.layout.dialog_edit_device, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            if(mtitle != null){
                ((TextView) layout.findViewById(R.id.person_title))
                        .setText(mtitle);
            }
            if(mid != null){
                ((EditText) layout.findViewById(R.id.person_deviceid))
                        .setText(mid);
            }
            if(mgroup != null){
                ((EditText) layout.findViewById(R.id.person_group))
                        .setText(mgroup);
            }
            if(mname != null){
                ((EditText) layout.findViewById(R.id.person_name))
                        .setText(mname);
            }

            setRadioBtnColorSelect(layout,mcolorindex);


            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("WrongViewCast")
                                public void onClick(View v) {

                                    dialog.edit_deviceid = ((EditText)layout.findViewById(R.id.person_deviceid)).getText().toString();
                                    dialog.edit_group = ((EditText)layout.findViewById(R.id.person_group)).getText().toString();
                                    dialog.edit_name = ((EditText)layout.findViewById(R.id.person_name)).getText().toString();
                                    dialog.selectindexColor = 1;
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }

    static void  setRadioBtnColorSelect(View layout ,int index){
        switch (index){

            case 0:
                ((RadioButton)layout.findViewById(R.id.radio_color1)).setChecked(true);
                break;
            case 1:
                ((RadioButton)layout.findViewById(R.id.radio_color2)).setChecked(true);
                break;
            case 2:
                ((RadioButton)layout.findViewById(R.id.radio_color3)).setChecked(true);
                break;
            case 3:
                ((RadioButton)layout.findViewById(R.id.radio_color4)).setChecked(true);
                break;
            case 4:
                ((RadioButton)layout.findViewById(R.id.radio_color5)).setChecked(true);
                break;
            case 5:
                ((RadioButton)layout.findViewById(R.id.radio_color6)).setChecked(true);
                break;
            case 6:
                ((RadioButton)layout.findViewById(R.id.radio_color7)).setChecked(true);
                break;
            case 7:
                ((RadioButton)layout.findViewById(R.id.radio_color8)).setChecked(true);
                break;
            case 8:
                ((RadioButton)layout.findViewById(R.id.radio_color9)).setChecked(true);
                break;
            case 9:
                ((RadioButton)layout.findViewById(R.id.radio_color10)).setChecked(true);
                break;
        }

    }

    void setRadioGroupListen(View layout ){

        layout.findViewById(R.id.radio_color1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){
                    selectindexColor = 1;
                }
            }
        });

        layout.findViewById(R.id.radio_color2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 2;
                }
            }
        });

        layout.findViewById(R.id.radio_color3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor =3;
                }
            }
        });

        layout.findViewById(R.id.radio_color4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 4;
                }
            }
        });

        layout.findViewById(R.id.radio_color1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 5;
                }
            }
        });
        layout.findViewById(R.id.radio_color1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 5;
                }
            }
        });
        layout.findViewById(R.id.radio_color6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 6;
                }
            }
        });
        layout.findViewById(R.id.radio_color7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 7;
                }
            }
        });
        layout.findViewById(R.id.radio_color8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 8;
                }
            }
        });
        layout.findViewById(R.id.radio_color9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 9;
                }
            }
        });
        layout.findViewById(R.id.radio_color10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                if(radioButton.isChecked()){

                    selectindexColor = 10;
                }
            }
        });



    }
}