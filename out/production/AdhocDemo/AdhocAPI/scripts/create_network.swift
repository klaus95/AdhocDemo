#!/usr/bin/swift

import Foundation
import CoreWLAN

let interfaceName = CommandLine.arguments[1]
let networkName = CommandLine.arguments[2]
let password = CommandLine.arguments[3]
let channels = Int(CommandLine.arguments[4])

if let iface = CWWiFiClient.shared().interface(withName: interfaceName) {
    do {
        if password == "" {
            try iface.startIBSSMode(
                withSSID: networkName.data(using: String.Encoding.utf8)!,
                security: CWIBSSModeSecurity.none,
                channel: channels!,
                password: password as String
            )
        } else {
            try iface.startIBSSMode(
                withSSID: networkName.data(using: String.Encoding.utf8)!,
                security: CWIBSSModeSecurity.WEP104,
                channel: channels!,
                password: password as String
            )
        }
    } catch {
        print("Error: Network unable to start because \(error) (error=1,)")
        exit(1)
    }
} else {
    print("Error: Interface does not exists (error=2,)")
    exit(1)
}
