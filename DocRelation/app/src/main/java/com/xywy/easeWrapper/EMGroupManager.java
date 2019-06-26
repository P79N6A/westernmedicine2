package com.xywy.easeWrapper;

import com.hyphenate.chat.EMClient;

/**
 * Created by shijiazi on 16/8/17.
 */
public class EMGroupManager {
    public static com.hyphenate.chat.EMGroupManager getInstance() {
        return EMClient.getInstance().groupManager();
    }
}
