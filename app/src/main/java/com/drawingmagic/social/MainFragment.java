package com.drawingmagic.social;

/**
 * Created by ihor.karpachev on 06/10/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.social
 * Datascope Systems Ltd.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.drawingmagic.R;
import com.drawingmagic.utils.Log;
import com.drawingmagic.utils.Notification;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestGetFriendsCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.vk.VkSocialNetwork;

import java.util.ArrayList;
import java.util.List;

import static com.drawingmagic.GlobalConstants.VKConstatns.VK_KEY;
import static com.drawingmagic.GlobalConstants.VKConstatns.VK_SCOPE;
import static com.drawingmagic.social.Social.SOCIAL_NETWORK_TAG;

public class MainFragment extends Fragment implements SocialNetworkManager.OnInitializationCompleteListener, OnRequestSocialPersonCompleteListener {
    public static SocialNetworkManager mSocialNetworkManager;
    /**
     * SocialNetwork Ids in ASNE:
     * 1 - Twitter
     * 2 - LinkedIn
     * 3 - Google Plus
     * 4 - Facebook
     * 5 - Vkontakte
     * 6 - Odnoklassniki
     * 7 - Instagram
     */
    private Button facebook;
    private Button twitter;
    private Button linkedin;
    private Button googleplus;

    private SocialNetwork socialNetwork;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
//        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
//        // init buttons and set Listener
        facebook = (Button) rootView.findViewById(R.id.facebook);
        facebook.setOnClickListener(loginClick);
        twitter = (Button) rootView.findViewById(R.id.twitter);
        twitter.setOnClickListener(loginClick);
        linkedin = (Button) rootView.findViewById(R.id.linkedin);
        linkedin.setOnClickListener(loginClick);
        googleplus = (Button) rootView.findViewById(R.id.googleplus);
        googleplus.setOnClickListener(loginClick);

        //Get Keys for initiate SocialNetworks
//        String TWITTER_CONSUMER_KEY = getActivity().getString(R.string.twitter_consumer_key);
//        String TWITTER_CONSUMER_SECRET = getActivity().getString(R.string.twitter_consumer_secret);
//        String TWITTER_CALLBACK_URL = "oauth://ASNE";
//        String LINKEDIN_CONSUMER_KEY = getActivity().getString(R.string.linkedin_consumer_key);
//        String LINKEDIN_CONSUMER_SECRET = getActivity().getString(R.string.linkedin_consumer_secret);
//        String LINKEDIN_CALLBACK_URL = "https://asneTutorial";

        //Chose permissions
//        ArrayList<String> fbScope = new ArrayList<String>();
//        fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));
//        //String linkedInScope = "r_basicprofile+r_fullprofile+rw_nus+r_network+w_messages+r_emailaddress+r_contactinfo";

        //Use manager to manage SocialNetworks
        // mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

//            //Init and add to manager FacebookSocialNetwork
//            FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
//            mSocialNetworkManager.addSocialNetwork(fbNetwork);

            //Init and add to manager TwitterSocialNetwork
//            TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(this, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, TWITTER_CALLBACK_URL);
//            mSocialNetworkManager.addSocialNetwork(twNetwork);

            //Init and add to manager LinkedInSocialNetwork
//            LinkedInSocialNetwork liNetwork = new LinkedInSocialNetwork(this, LINKEDIN_CONSUMER_KEY, LINKEDIN_CONSUMER_SECRET, LINKEDIN_CALLBACK_URL, linkedInScope);
//            mSocialNetworkManager.addSocialNetwork(liNetwork);
//
//            //Init and add to manager LinkedInSocialNetwork
//            GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(this);
//            mSocialNetworkManager.addSocialNetwork(gpNetwork);
            //Init and add to manager VkSocialNetwork
            VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VK_KEY, VK_SCOPE);
            mSocialNetworkManager.addSocialNetwork(vkNetwork);

            //Initiate every network from mSocialNetworkManager
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    // socialNetwork.setOnLoginCompleteListener(this);
                    initSocialNetwork(socialNetwork);
                }
            }
        }
        return rootView;
    }

    private void initSocialNetwork(SocialNetwork socialNetwork) {
        if (socialNetwork.isConnected()) {
            switch (socialNetwork.getID()) {
//                case FacebookSocialNetwork.ID:
//                    facebook.setText("Show Facebook profile");
//                    break;
//                case TwitterSocialNetwork.ID:
//                    twitter.setText("Show Twitter profile");
//                    break;
//                case LinkedInSocialNetwork.ID:
//                    linkedin.setText("Show LinkedIn profile");
//                    break;
//                case GooglePlusSocialNetwork.ID:
//                    googleplus.setText("Show GooglePlus profile");
//                    break;
                case VkSocialNetwork.ID:
                    Notification.showSuccess(getActivity(), "VK");
                    break;
            }
        }
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            // socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }

    //Login listener

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int networkId = VkSocialNetwork.ID;
//            switch (view.getId()) {
//
////                case R.id.facebook:
////                    networkId = FacebookSocialNetwork.ID;
////                    break;
////                case R.id.twitter:
////                    networkId = TwitterSocialNetwork.ID;
////                    break;
////                case R.id.linkedin:
////                    networkId = LinkedInSocialNetwork.ID;
////                    break;
////                case R.id.googleplus:
////                    networkId = GooglePlusSocialNetwork.ID;
////                    break;
//            }
            final SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
            if (socialNetwork.isConnected()) {
                startProfile(socialNetwork.getID());
            } else {
                socialNetwork.setOnRequestCurrentPersonCompleteListener(new OnRequestSocialPersonCompleteListener() {
                    @Override
                    public void onRequestSocialPersonSuccess(int socialNetworkId, SocialPerson socialPerson) {
                        Log.e("sda sd" + socialPerson.toString());
                    }

                    @Override
                    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
                        Log.e("sda sd");
                    }
                });
                socialNetwork.requestCurrentPerson();
                //socialNetwork.setOnLoginCompleteListener(MainFragment.this);
                socialNetwork.requestLogin(new OnLoginCompleteListener() {
                    @Override
                    public void onLoginSuccess(int socialNetworkID) {
                        Log.e(socialNetworkID + "");
                    }

                    @Override
                    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
                        Log.e(socialNetworkID + "");
                    }
                });
            }
        }
    };

//    @Override
//    public void onLoginSuccess(int networkId) {
//        startProfile(networkId);
//    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    private void startProfile(int networkId) {
//        ProfileFragment profile = ProfileFragment.newInstannce(networkId);
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .addToBackStack("profile")
//                .replace(R.id.container, profile)
//                .commit();
        socialNetwork = MainFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        socialNetwork.requestCurrentPerson();
    }

    @Override
    public void onRequestSocialPersonSuccess(int socialNetworkId, SocialPerson socialPerson) {
        Log.e(socialPerson.name);
    }
}
