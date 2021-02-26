package com.app.tryitat.ui.notification.interfaces;

import android.view.View;

import com.app.tryitat.ui.home.model.PostResponse;

public interface NotificationListenner {
    void onClick (View view, int position, boolean isLongClick);
}
