package tcss450.uw.edu.chatapp.chats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Chats implements Serializable {

    private final String mEmail;
    private final String mNickname;
    private final String mLastMessage;
    private final String mTimeStamp;
    private final int mChatID;

    public static class Builder {
        private String mEmail = "";
        private final String mNickname;
        private  String mTimeStamp = "";
        private final int mChatID;
        private  String mLastMessage= "";


        public Builder(String nickname, int chatID) {
            this.mNickname = nickname;
            this.mChatID = chatID;
        }

        public Chats.Builder addEmail(final String val) {
            mEmail = val;
            return this;
        }

        public Chats.Builder addTimestamp(final String val) {
            mTimeStamp = val;
            return this;
        }

        public Chats.Builder addLastMessage(final String val) {
            mLastMessage = val;
            return this;
        }

        public Chats build() {
            return new Chats(this);
        }
    }

    private Chats(final Builder builder) {
        this.mEmail = builder.mEmail;
        this.mNickname = builder.mNickname;
        this.mTimeStamp = builder.mTimeStamp;
        this.mChatID = builder.mChatID;
        this.mLastMessage = builder.mLastMessage;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public String getLastMessage() { return mLastMessage; }

    public int getChatID()  {
        return mChatID;
    }
}
