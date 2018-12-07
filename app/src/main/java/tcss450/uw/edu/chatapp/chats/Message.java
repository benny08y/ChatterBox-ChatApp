package tcss450.uw.edu.chatapp.chats;

import java.io.Serializable;

/**
 * Benjamin Yuen
 * Message class utilizing Builder design pattern for easy instantiation.
 */
public class Message implements Serializable {

    private final String mEmail;
    private final String mFirstName;
    private final String mLastName;
    private final String mNickname;
    private final String mMessage;
    private final String mTimeStamp;
    private final String mChatName;
    private final int mChatId;

    public static class Builder {
        private final String mEmail;
        private final String mNickname;
        private String mMessage="";
        private final int mChatId;
        private String mFirstName="";
        private String mLastName="";
        private String mTimeStamp="";
        private String mChatName="";

        public Builder(String email, String nickname, int chatid) {
            this.mEmail = email;
            this.mNickname = nickname;
            this.mChatId = chatid;
        }

        public Message.Builder addTimeStamp(final String timeStamp){
            mTimeStamp = timeStamp;
            return this;
        }

        public Message.Builder addFirstName(final String val){
            mFirstName = val;
            return this;
        }

        public Message.Builder addLastName(final String lastname){
            mLastName = lastname;
            return this;
        }

        public Message.Builder addMessage(final String msg){
            mMessage = msg;
            return this;
        }

        public Message.Builder addChatName(final String chatNa){
            mChatName = chatNa;
            return this;
        }
        public Message build() {
            return new Message(this);
        }
    }

    private Message(final Builder builder) {
        this.mEmail = builder.mEmail;
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mMessage = builder.mMessage;
        this.mTimeStamp = builder.mTimeStamp;
        this.mChatId = builder.mChatId;
        this.mNickname = builder.mNickname;
        this.mChatName = builder.mChatName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public int getChatId() {
        return mChatId;
    }

    public String getNickname() {
        return mNickname;
    }
    public String getChatName() {
        return mChatName;
    }

}