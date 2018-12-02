package tcss450.uw.edu.chatapp.model;

import java.io.Serializable;

/**
 * Class to encapsulate a ChatApp contact. Building a contact requires a nickname and email.
 * Optional fields include first name and last name.
 *
 * @author Aaron Bardsley
 * @version 11/17/2018
 * ************************************************************************************************
 * Adapted from:
 *
 * Class to encapsulate a Phish.net Blog Post. Building an Object requires a publish date and title.
 *
 * Optional fields include URL, teaser, and Author.
 *
 *
 * @author Charles Bryan
 * @version 14 September 2018
 */
public class Contacts implements Serializable {

    private final String mNickname;
    private final String mEmail;
    private final String mFirstName;
    private final String mLastName;


    public static class Builder {
        private final String mNickname;
        private final String mEmail;
        private  String mFirstName = "";
        private  String mLastName = "";


        public Builder(String nickname, String email) {
            this.mNickname = nickname;
            this.mEmail = email;
        }

        public Builder addFirstName(final String val) {
            mFirstName = val;
            return this;
        }

        public Builder addLastName(final String val) {
            mLastName = val;
            return this;
        }

        public Contacts build() {
            return new Contacts(this);
        }

    }

    private Contacts(final Builder builder) {
        this.mNickname = builder.mNickname;
        this.mEmail = builder.mEmail;
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
    }

    public String getNickname() {
        return mNickname;
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
}
