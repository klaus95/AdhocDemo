#!/usr/bin/swift

import Foundation
import CoreWLAN

let interface = CommandLine.arguments[1]
let ssid = CommandLine.arguments[2]
let password = CommandLine.arguments[3]

if let iface = CWWiFiClient.shared().interface(withName: interface) {
    do {
        if let avaiableNetworks = try iface.scanForNetworks(withName: ssid).first {
            try iface.associate(to: avaiableNetworks, password: ssid)
        } else {
            print("Error: Network with \(ssid) was not found  (error=7,)")
            exit(1)
        }
    } catch {
        print("Error: Unable to connect to network  (error=8,)")
        exit(1)
    }
} else {
    print("Error: Interface does not exists  (error=2,)")
    exit(1)
}
