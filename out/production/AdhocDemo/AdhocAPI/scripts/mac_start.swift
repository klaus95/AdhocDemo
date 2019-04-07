#!/usr/bin/swift

import Foundation
import CoreWLAN

//CommandLine.arguments[0] gets the first cmd arguments

let networkName = CommandLine.arguments[1]
let password = CommandLine.arguments[2]

if let iface = CWWiFiClient.shared().interface() {
    do {
        try iface.startIBSSMode(
            withSSID: networkName.data(using: String.Encoding.utf8)!,
            security: CWIBSSModeSecurity.WEP104,
            channel: 11,
            password: password as String
        )
        print("Success")
    } catch let error as NSError {
        print("Error", error)
        exit(1)
    }
} else {
    print("Invalid interface")
    exit(1)
}
