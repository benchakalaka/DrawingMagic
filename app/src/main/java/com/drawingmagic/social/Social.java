package com.drawingmagic.social;

import android.support.v4.app.Fragment;

import com.drawingmagic.SuperActivity;
import com.drawingmagic.utils.Conditions;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.vk.VkSocialNetwork;

import java.util.ArrayList;
import java.util.List;

import static com.drawingmagic.GlobalConstants.VKConstatns.VK_KEY;
import static com.drawingmagic.GlobalConstants.VKConstatns.VK_SCOPE;

/**
 * Created by ihor.karpachev on 05/10/2015.
 * Project: DrawingMagic
 * Package: com.drawingmagic.social
 * Datascope Systems Ltd.
 */
public class Social {
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    private static SocialNetworkManager socialNetworkManager;

    public static List<SocialNetwork> getInitializedSocialNetworks() {
        if (Conditions.isNull(socialNetworkManager)) {
            return new ArrayList<>();
        }
        return socialNetworkManager.getInitializedSocialNetworks();
    }

    public static SocialNetwork getSocialNetwork(int networkID) {
        return socialNetworkManager.getSocialNetwork(networkID);
    }

    /**
     * Share photo via social networks
     *
     * @param networkId id of network to be shared with
     */
    public static void shareViaNetwork(int networkId, Fragment fShare) {

        // Init network - lazy init
        if (Conditions.isNull(socialNetworkManager)) {
            initSocialManager(fShare);
            return;
        }

        SocialNetwork socialNetwork = socialNetworkManager.getSocialNetwork(networkId);

        // check is network connected, if nope - request login
        if (!socialNetwork.isConnected()) {
            socialNetwork.requestLogin();
            return;
        }

        switch (networkId) {
            case VkSocialNetwork.ID:
                shareVK((SuperActivity) fShare.getActivity());
                //startProfile(VkSocialNetwork.ID, activity);
                break;

            default:
                break;
        }
    }

//    private static void startProfile(int networkId, SuperActivity activity) {
//        FragmentShare profile = FragmentShare.newInstance(networkId);
//        activity.getSupportFragmentManager().beginTransaction()
//                .addToBackStack("profile")
//                .replace(R.id.container, profile)
//                .commit();
//    }

    private static void shareVK(SuperActivity activity) {
        VkSocialNetwork socialNetwork = (VkSocialNetwork) getSocialNetwork(VkSocialNetwork.ID);
        socialNetwork.requestPostMessage("Message has been posted from Android");

    }

    private static void shareTwitter() {
    }


    private static void shareFacebook() {
    }


    private static void shareOdnoklassniki() {
    }

    private static void initSocialManager(Fragment fShare) {
        socialNetworkManager = (SocialNetworkManager) fShare.getActivity().getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);

        if (Conditions.isNull(socialNetworkManager)) {
            socialNetworkManager = new SocialNetworkManager();

            //Init and add to manager VkSocialNetwork
            VkSocialNetwork vkNetwork = new VkSocialNetwork(fShare, VK_KEY, VK_SCOPE);
            socialNetworkManager.addSocialNetwork(vkNetwork);

            setListeners(fShare);

            //Initiate every network from socialNetworkManager
            fShare.getActivity().getSupportFragmentManager().beginTransaction().add(socialNetworkManager, SOCIAL_NETWORK_TAG).commit();
            socialNetworkManager.setOnInitializationCompleteListener((SocialNetworkManager.OnInitializationCompleteListener) fShare.getActivity());
        } else {
            setListeners(fShare);
        }
    }

    private static void setListeners(Fragment fShare) {
        //if manager exist - get and setup login only for initialized SocialNetworks
        if (!socialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
            List<SocialNetwork> socialNetworks = socialNetworkManager.getInitializedSocialNetworks();
            for (SocialNetwork socialNetwork : socialNetworks) {
                socialNetwork.setOnLoginCompleteListener((OnLoginCompleteListener) fShare.getActivity());
                socialNetwork.setOnPostingMessageCompleteListener((OnPostingCompleteListener) fShare.getActivity());
            }
        }
    }

    public static void requestCurrentPerson(int networkId, FragmentShare fragmentShare) {
        if (Conditions.isNull(socialNetworkManager)) {
            initSocialManager(fragmentShare);
        }

        VkSocialNetwork network = (VkSocialNetwork) socialNetworkManager.getSocialNetwork(networkId);
        if (!network.isConnected()) {
            network.requestLogin();
            return;
        }
        network.setOnRequestCurrentPersonCompleteListener(fragmentShare);
        network.requestCurrentPerson();
    }
}
