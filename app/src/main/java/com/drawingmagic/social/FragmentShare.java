package com.drawingmagic.social;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drawingmagic.R;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EFragment(R.layout.fragment_share)
public class FragmentShare extends Fragment implements OnRequestSocialPersonCompleteListener {

    @ViewById
    ImageView ivAvatar;
    @ViewById
    TextView tvUserName, tvUserId, info;

    @ViewById
    Button btnShare;
    RelativeLayout frame;

    @FragmentArg
    int networkId;

    public FragmentShare() {
    }

    @AfterViews
    void afterViews() {
        Social.requestCurrentPerson(networkId, this);
    }

    @Click
    void btnShare() {
        Social.shareViaNetwork(VkSocialNetwork.ID, this);
        Social.getSocialNetwork(networkId).setOnPostingPhotoCompleteListener((OnPostingCompleteListener) getActivity());
        Social.getSocialNetwork(networkId).requestPostPhoto(new File("/mnt/sdcard/Download/pGzGsKMYzPg.jpg"), "Ja kyrka i ja byhajy: ");
    }


    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        colorProfile(networkId);
        tvUserName.setText(socialPerson.name);
        tvUserId.setText(socialPerson.id);
        String socialPersonString = socialPerson.toString();
        String infoString = socialPersonString.substring(socialPersonString.indexOf("{") + 1, socialPersonString.lastIndexOf("}"));
        info.setText(infoString.replace(", ", "\n"));
        Picasso.with(getActivity())
                .load(socialPerson.avatarURL)
                .into(ivAvatar);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }


    private void colorProfile(int networkId) {
        int color = getResources().getColor(R.color.black);
        int image = R.drawable.ic_launcher;
        switch (networkId) {
            case VkSocialNetwork.ID:
                color = R.color.vk_color;
                image = R.drawable.ic_launcher;
                break;

        }
        frame.setBackgroundColor(color);
        tvUserName.setTextColor(color);
        btnShare.setBackgroundColor(color);
        ivAvatar.setBackgroundColor(color);
        ivAvatar.setImageResource(image);
    }
}
