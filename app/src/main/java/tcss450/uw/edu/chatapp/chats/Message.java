package tcss450.uw.edu.chatapp.chats;

import java.io.Serializable;

public class Message implements Serializable {

    private final String mEmail;
    private final String mFirstName;
    private final String mLastName;
    private final String mMessage;
    private final String mTimeStamp;

    public static class Builder {
        private final String mEmail;
        private final String mFirstName;
        private final String mLastName;
        private final String mMessage;
        private final String mTimeStamp;

        public Builder(String email, String firstName, String lastName, String message, String timeStamp) {
            this.mEmail = email;
            this.mFirstName = firstName;
            this.mLastName = lastName;
            this.mMessage = message;
            this.mTimeStamp = timeStamp;
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

}