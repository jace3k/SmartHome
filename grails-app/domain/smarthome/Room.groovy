package smarthome

class Room {
    String name

    static constraints = {
        name nullable: false, unique: true, minSize: 1
    }
}
