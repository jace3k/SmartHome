package smarthome

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'main')
        "500"(view:'/error')
        "404"(view:'/notFound')

        "/add_room"(controller: 'main', action: 'addRoom') // room_name

        "/history"(controller: 'main', action: 'getHistory') // max (opcjonalnie)

        "/rooms"(controller: 'main', action: 'allRooms')
        "/room/$room_name"(controller: 'main', action: 'roomProperty') // light (zapala wszystkie swiatła)
        "/room/$room_name/devices"(controller: 'main', action: 'getDevices')
        "/room/$room_name/add_device"(controller: 'main', action: 'addDevice') // device_id

        "/request/$device_id"(controller: 'main', action: 'requestFor') //  type, value

        "/waiting"(controller: 'main', action: 'unassignedDevices')

    }
}
