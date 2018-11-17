package smarthome.components

class Config {
    private static String RABBIT_LOGIN = "jacek"
    private static String RABBIT_PASSWORD = "123qaz"
    private static String PASS_PATH = "/home/jacek/passwords/pass"
    private static String RABBIT_HOST = '80.211.175.184'

    static boolean getCredentials() {
        return true
    }

    static String getRabbitLogin() {
        return RABBIT_LOGIN
    }

    static String getRabbitPassword() {
        return RABBIT_PASSWORD
    }

    static String getRabbitHost() {
        return RABBIT_HOST
    }
}
