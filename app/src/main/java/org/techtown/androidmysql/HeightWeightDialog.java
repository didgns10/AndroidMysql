package org.techtown.androidmysql;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

//키,몸무게 입력 다이얼로그 팝업생성 그리고 데이터 저장처리하기.
public class HeightWeightDialog extends Dialog {


    private Button mPositiveButton;
    private Button mNegativeButton;

    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.heightweightdialog);

        //셋팅
        mPositiveButton=(Button)findViewById(R.id.pbutton);


        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mPositiveButton.setOnClickListener(mPositiveListener);

    }

    //생성자 생성
    public HeightWeightDialog(@NonNull Context context, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
    }

}
