package smarthome

import grails.converters.JSON

class MainController {
    PublisherService publisher = new PublisherService()

    def index() {
        def rooms = Room.findAll()
        [rooms: rooms]
    }

    def addRoom() {
        def roomName = params.room_name as String
        Room room = new Room(name: roomName)
        if (room.save(flush: true)) {
            render([message: 'success'] as JSON)
        } else {
            render([message: 'failed'] as JSON)
        }
    }

    def addDevice() {
        def deviceId = params.device_id as long
        def roomName = params.room_name as String
        Room room = Room.findByName(roomName)
        Device device = Device.findByDevice_id(deviceId)
        if (device) {
            device.setRoom(room)
            device.setAssigned(true)
            if (device.save(flush: true)) {
                render([message: "Dodano urządzenie $deviceId do $room.name.", success: true] as JSON)
            } else {
                render([message: 'Błąd wewnętrzny.', success: false] as JSON)
            }
        } else {
            render([message: 'Nie znaleziono urządzenia.', success: false] as JSON)
        }

    }

    def getDevices() {
        def roomName = params.room_name as String
        def room = Room.findByName(roomName)
        if (room) {
            def devices = Device.findAllByRoom(room)
            render devices as JSON
        } else {
            render([message: 'Nie znaleziono pokoju.', success: false] as JSON)
        }
    }

    def getHistory() {
        def max
        if (params.max) {
            max = params.max
        } else {
            max = 100
        }
        def history = Measurement.list(max: max, sort: 'dateCreated', order: 'desc')
        def historyList = []

        history.each {
            historyList.add([
                    id  : it.id,
                    room: [name: it.room.name, id: it.room.id],
                    time: it.time,
                    device: [id: it.id, device_id: it.deviceId],
                    value: it.value,
                    type: [id: it.type.id, name: it.type.name]
            ] as JSON)
        }


        render historyList
    }

    def roomProperties(String room_name) {
        def roomName = room_name as String
        Room room = Room.findByName(roomName)
        if (!room) {
            return ['message': 'Nie znaleziono pokoju.', success: false] as JSON
        }
        def devices = Device.findAllByRoom(room)

        def roomMap = [:]
        roomMap.put('name', roomName)

        Type.all.each {
            def measureMap = [:]
            def measure = Measurement.findAllByTypeAndRoom(it, room)
            if (measure) {
                measureMap.put('value', measure.last().value)
                measureMap.put('last_change', measure.last().time)
                roomMap.put(it.name, measureMap)
            }

        }

        def devicesList = []
        devices.each {
            devicesList.add(['id': it.device_id])
        }
        roomMap.put('devices', devicesList)


        return roomMap as JSON
    }

    def allRooms() {
        def rooms = Room.all
        def roomsList = []
        rooms.each {
            def temperatureValue = Measurement.findAllByTypeAndRoom(Type.findByName('temperature'), it)
            if (!temperatureValue) temperatureValue = 0
            else temperatureValue = temperatureValue.last().value
            def humidityValue = Measurement.findAllByTypeAndRoom(Type.findByName('humidity'), it)
            if (!humidityValue) humidityValue = 0
            else humidityValue = humidityValue.last().value
            def roomProperties = [
                    name    : it.name,
                    measures: [
                            'temperature': temperatureValue,
                            'humidity'   : humidityValue
                    ]
            ]
            if (it.name != 'none') roomsList.add(roomProperties as JSON)
        }
        render(roomsList)
    }

    def roomProperty() {
        if(params.light) {
            render changeLight(params.light as int)
        } else {
            render roomProperties(params.room_name as String)
        }

    }

    def requestFor() {
        def type = params.type
        def value = params.value
        def device_id = params.device_id as long
        if (type == null || value == null) {
            render(['message': 'Parametry type i value nie mogą być puste.', success: false] as JSON)
        } else {
            def map = [
                    request: type,
                    value  : value as int,
                    success: true
            ] as JSON
            publisher.setDevice(device_id)
            publisher.message(map as String)

            render(map)
        }
    }

    def types() {
        render Type.all
    }

    def unassignedDevices() {
        def devices = []

        Device.all.each {
            if (!it.assigned) {
                devices.add([id: it.device_id] as JSON)
            }
        }
        render(devices)
    }

    def changeLight(int value) {
        def map = [
                request: 'set_lighting',
                value  : value as int,
                success: true
        ] as JSON

        def devices = Device.findAllByRoom(Room.findByName(params.room_name as String))

        devices.each {
            publisher.setDevice(it.device_id)
            publisher.message(map as String)
        }
        return map
    }
}
