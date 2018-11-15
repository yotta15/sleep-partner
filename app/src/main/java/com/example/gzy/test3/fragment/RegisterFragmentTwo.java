package com.example.gzy.test3.fragment;
/**
 * created by gzy on 2018.11.12
 */

import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gzy.test3.R;
import com.example.gzy.test3.activity.RegisterActivity;
import com.example.gzy.test3.model.UserModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterFragmentTwo extends Fragment {
    EditText mEmail;
    TextView mTextage, mTextHeight, mTextWeight;
    RadioGroup mSex;
    ImageButton mBtnAge, mBtnHeight, mBtnWeight;
    Button mBtnConfirm;
    String sex="男";
    int age=18, weight=120, height=180;

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
        mEmail = (EditText) view.findViewById(R.id.email);
        mEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
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
                np.setMinValue(30);
                np.setMaxValue(10000);
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
                        mTextWeight.setText(weight + "斤");
                        bottomSheetDialog3.dismiss();
                    }
                });
                bottomSheetDialog3.setContentView(dialogView);
                bottomSheetDialog3.show();
            }
        });
        mBtnConfirm=(Button)view.findViewById(R.id.btnConfirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Log.e("update",sex+"aaaaaaa"+age+"aaaaaaa"+weight+"aaaaaaa"+height);
                UserModel newUser = new UserModel();
                newUser.setSex(sex);
                newUser.setAge(age);
                newUser.setWeight(weight);
                newUser.setHeight(height);
                newUser.setEmail(mEmail.getText().toString().trim());
                BmobUser bmobUser = BmobUser.getCurrentUser();
                newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
                            ((RegisterActivity) getActivity()).setPosition(position);
                        }else{
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
