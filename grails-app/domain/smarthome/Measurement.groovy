package smarthome

class Measurement {
    String value
    Type type
    Room room
    Device device
    long time
    Date dateCreated

    static constraints = {

    }

    static mapping = {
        autoTimestamp true
    }
}
