package com.xywy.livevideo.chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.chat.interfaces.IChatModel;
import com.xywy.livevideo.chat.interfaces.IChatView;
import com.xywy.livevideo.chat.model.ChatModelImpl;
import com.xywy.livevideo.chat.model.LiveChatContent;
import com.xywy.livevideo.common_interface.LiveRequest;
import com.xywy.livevideolib.R;

import java.util.List;
import java.util.Set;

public class LiveChatFragment extends Fragment implements IChatView {
    private static final String TAG = LiveChatFragment.class.getSimpleName();
    RecyclerView recyclerView;
    ChatMessageAdapter adapter;
    IChatModel model;
    Set<String> roomIds= LiveManager.getInstance().getConfig().chatRoomIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ChatModelImpl(this, roomIds);
    }

    public void addRoomId(String roomId) {
        roomIds.add(roomId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.rv_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatMessageAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        model.refreshMsgs();
        LiveRequest.getGiftList(1, null);
//        test(0);
    }

    private void test(final int i) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i%2==0){
                    model.sendGiftMsg("1",1);
                }else {
                    model.sendMsg(i+"");
                }
                if (i+1<200)
                test(i+1);
            }
        },500);
    }

    @Override
    public void onDestroy() {
        if (model != null) {
            model.onFinish();
        }
        super.onDestroy();
    }

    final static int Scroll_To_Last = 100;
    Handler h = new Handler() {
        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case Scroll_To_Last:
                    if (adapter.getDatas().size() > 0) {
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void refreshChatView(final List<LiveChatContent> msgs) {
        // you should not call getAllMessages() in UI thread
        // otherwise there is problem when refreshing UI and there is new message arrive
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                adapter.setData(msgs);
                adapter.notifyDataSetChanged();
//                h.removeMessages(Scroll_To_Last);
                h.sendEmptyMessage(Scroll_To_Last);

            }
        });
    }

    //// TODO: 2017/2/24
    public void sendGiftMsg(String gitId, int count) {
        model.sendGiftMsg(gitId, count);
    }

    //// TODO: 2017/2/24
    public void showMsgInputBox() {
        model.showMsgInputBox(getActivity());
    }
}
