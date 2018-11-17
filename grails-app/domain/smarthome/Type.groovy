package smarthome

class Type {
    String name

    static constraints = {
        name nullable: false, unique: true
    }
}
