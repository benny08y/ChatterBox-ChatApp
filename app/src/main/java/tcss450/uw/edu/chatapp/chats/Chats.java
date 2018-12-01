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
    private final String mFirstName;
    private final String mLastName;
    private final String mNickname;
    private final String mLastMessage;
    private final String mTimeStamp;
    private final int mChatID;
    private final String mChatname;

    public static class Builder {
        private final String mEmail;
        private final String mFirstName;
        private final String mLastName;
        private String mNickname="";
        private  String mTimeStamp = "";
        private int mChatID=0;
        private  String mLastMessage= "";
        private  String mChatName= "";

        public Builder(String email, String firstName, String lastName) {
            this.mEmail = email;
            this.mFirstName = firstName;
            this.mLastName = lastName;
        }

        public Chats.Builder addChatID(final int val) {
            mChatID = val;
            return this;
        }

        public Chats.Builder addNickname(final String val) {
            mNickname = val;
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

        public Chats.Builder addChatName(final String val) {
            mChatName = val;
            return this;
        }
        public Chats build() {
            return new Chats(this);
        }
    }

    private Chats(final Builder builder) {
        this.mEmail = builder.mEmail;
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mNickname = builder.mNickname;
        this.mTimeStamp = builder.mTimeStamp;
        this.mChatID = builder.mChatID;
        this.mLastMessage = builder.mLastMessage;
        this.mChatname = builder.mChatName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstname() {
        return mFirstName;
    }

    public String getLastname() {
        return mLastName;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getChatName() {
        return mChatname;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public String getLastMessage() { return mLastMessage; }

    public int getChatID()  {
        return mChatID;
    }
}
