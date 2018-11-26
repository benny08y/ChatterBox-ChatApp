package tcss450.uw.edu.chatapp.dummy;

public class WeatherHourContent {

    private final String mCity;
    private final String mDt;
    private final String mIcon;
    private final String mTemperature;
    private final String mDetails;
    private final String mHumidity;
    private final String mPressure;

    public static class WeatherHourBuilder {
        private final String mCity;
        private final String mDt;
        private String mIcon = "";
        private String mTemperature = "";
        private String mDetails = "";
        private String mHumidity = "";
        private String mPressure = "";

        public WeatherHourBuilder(String city, String dt) {
            this.mCity = city;
            this.mDt = dt;
        }

        public WeatherHourBuilder addIcon(final String val) {
            mIcon = val;
            return this;
        }

        public WeatherHourBuilder addTemperature(final String val) {
            mTemperature = val;
            return this;
        }

        public WeatherHourBuilder addDetails(final String val) {
            mDetails = val;
            return this;
        }

        public WeatherHourBuilder addHumidity(final String val) {
            mHumidity = val;
            return this;
        }

        public WeatherHourBuilder addPressure(final String val) {
            mPressure = val;
            return this;
        }

        public WeatherHourContent build() {
            return new WeatherHourContent(this);
        }

    }

    private WeatherHourContent(final WeatherHourBuilder builder) {
        this.mCity = builder.mCity;
        this.mDt = builder.mDt;
        this.mIcon = builder.mIcon;
        this.mTemperature = builder.mTemperature;
        this.mDetails = builder.mDetails;
        this.mHumidity = builder.mHumidity;
        this.mPressure = builder.mPressure;
    }

    public String getCity() {
        return mCity;
    }

    public String getDt() {
        return mDt;
    }

    public String getIcon() {
        return mIcon;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getDetails() {
        return mDetails;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public String getPressure() {
        return mPressure;
    }
}
