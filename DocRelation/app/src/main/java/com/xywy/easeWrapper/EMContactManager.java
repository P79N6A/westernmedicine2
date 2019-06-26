package com.xywy.easeWrapper;

import com.hyphenate.chat.EMClient;

/**
 * Created by shijiazi on 16/8/17.
 */
public class EMContactManager {
    public static com.hyphenate.chat.EMContactManager getInstance() {
        return EMClient.getInstance().contactManager();
    }
}
