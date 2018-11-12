package com.example.gzy.test3.fragment;
/**
 * created by gzy on 2018.11.12
 */

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.TestActivity;
import com.example.gzy.test3.activity.TestFragment;

public class RegisterFragmentTwo extends Fragment {
    EditText mNickname;
    TextView mTextage, mTextHeight, mTextWeight;
    RadioGroup mSex;
    ImageButton mBtnAge, mBtnHeight, mBtnWeight, mBtnConfirm;
    String sex;
    int age, weight, height;

    public static RegisterFragmentTwo newInstance(int position, String name) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("name", name);
        RegisterFragmentTwo fragment = new RegisterFragmentTwo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final int position = getArguments().getInt("position");
        String name = getArguments().getString("name");
        View view = inflater.inflate(R.layout.register_info, container, false);
        mNickname = (EditText) view.findViewById(R.id.nickname);
        mSex = (RadioGroup) view.findViewById(R.id.Sex);
        mTextage = (TextView) view.findViewById(R.id.TextAge);
        mBtnAge = (ImageButton) view.findViewById(R.id.BtnAge);
        mTextHeight = (TextView) view.findViewById(R.id.TextHeight);
        mBtnHeight = (ImageButton) view.findViewById(R.id.BtnHeight);
        mTextWeight = (TextView) view.findViewById(R.id.TextWeight);
        mBtnWeight = (ImageButton) view.findViewById(R.id.BtnWeight);
        mSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sex = i == R.id.Sex_male ? "男" : "女";
            }
        });


        mBtnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(getActivity());
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom_age, null);
                NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.np_age);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setValue(18);
                age=np.getValue();
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        age = newVal;//将新值存入变量，
                    }
                });
                dialogView.findViewById(R.id.btn_Cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog1.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btn_Conserve).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTextage.setText(age + "岁");
                        bottomSheetDialog1.dismiss();
                    }
                });
                bottomSheetDialog1.setContentView(dialogView);
                bottomSheetDialog1.show();
            }
        });
        mBtnHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(getActivity());
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom_height, null);
                NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.np_height);
                np.setMinValue(30);
                np.setMaxValue(242);
                np.setValue(180);
                height=np.getValue();
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        height = newVal;//将新值存入变量，
                    }
                });
                dialogView.findViewById(R.id.btn_Cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog2.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btn_Conserve).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTextHeight.setText(height + "厘米");
                        bottomSheetDialog2.dismiss();
                    }
                });
                bottomSheetDialog2.setContentView(dialogView);
                bottomSheetDialog2.show();
            }
        });
        mBtnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog3 = new BottomSheetDialog(getActivity());
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom_weight, null);
                NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.np_weight);
                np.setMinValue(6);
                np.setValue(120);
                weight=np.getValue();
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        weight = newVal;//将新值存入变量，
                    }
                });
                dialogView.findViewById(R.id.btn_Cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog3.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btn_Conserve).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTextWeight.setText(weight + "厘米");
                        bottomSheetDialog3.dismiss();
                    }
                });
                bottomSheetDialog3.setContentView(dialogView);
                bottomSheetDialog3.show();
            }
        });
//        Button btn = new Button(container.getContext(), null);
//        btn.setGravity(Gravity.CENTER);
//        btn.setText("当前状态:" + name);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((TestActivity) getActivity()).setPosition(position);
//            }
//        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
