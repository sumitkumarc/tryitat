package com.app.tryitat.ui.home.interfaces;

import com.app.tryitat.ui.home.model.PostResponse;

public interface HomeAdapterListener {
    void onLikedBtnClick(int position, PostResponse postRes);
    void onCommentBtnClick(PostResponse postResponse);
    void onTriedBtnClick(int position, PostResponse postResponse);
    void onLocationBtnClick(int position, String postId);
    void onFollowBtnClick();
}
