package smarthome

class Device {
    long device_id
    Room room
    boolean assigned

    static hasMany = [measurements: Measurement]

    static constraints = {
        room nullable: false
        device_id nullable: false, unique: true
    }
}
