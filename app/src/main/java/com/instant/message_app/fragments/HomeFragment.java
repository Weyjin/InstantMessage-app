package com.instant.message_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instant.message_app.R;
import com.instant.message_app.activity.MainActivity;
import com.instant.message_app.utils.SharedPreferenceHelper;
import com.instant.message_app.zxing.android.CaptureActivity;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private LogoutListener listener;
    private SharedPreferenceHelper helper;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener= (LogoutListener) getContext();
        helper=new SharedPreferenceHelper(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        TextView name=view.findViewById(R.id.text_name);
        TextView signature=view.findViewById(R.id.text_signature);
        LinearLayout logout=view.findViewById(R.id.linear_logout);
        LinearLayout sweepCode=view.findViewById(R.id.linear_sweep_code);

        name.setText(helper.getUserName());
        signature.setText(helper.getUserSignature());

        sweepCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        logout.setOnClickListener(v -> listener.logout());
        return view;
    }


    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
               listener.resultContent(content);
            }
        }
    }

    public interface LogoutListener{
        void logout();
        void resultContent(String content);
    }
}
