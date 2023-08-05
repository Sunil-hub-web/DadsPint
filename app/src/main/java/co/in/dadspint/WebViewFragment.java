package co.in.dadspint;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WebViewFragment extends Fragment {

    WebView webview;
    Dialog dialog;
    String str_url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_web_view,container,false);

        DeshBoardActivity.realBack.setVisibility(View.GONE);

        str_url = getArguments().getString("weburl");
        // Find the WebView by its unique ID
        WebView webView = view.findViewById(R.id.webview);
        ImageView backimage = view.findViewById(R.id.backimage);

        // loading https://www.geeksforgeeks.org url in the WebView.
        webView.loadUrl(str_url);

        // this will enable the javascript.
        webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.setWebViewClient(new WebViewClient());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
