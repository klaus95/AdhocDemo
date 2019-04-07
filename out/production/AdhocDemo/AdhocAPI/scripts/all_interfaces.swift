#!/usr/bin/swift

import Foundation
import CoreWLAN

var interfaces = Array<String>()
var index = 0

if let iface = CWWiFiClient.interfaceNames() {
    for interface in iface {
        interfaces.insert(interface, at: index)
        index += 1
    }
} else {
    print("Error: Interface does not exists  (error=2,)")
    exit(1)
}

for i in 0..<interfaces.count {
    if i == 0 {
        print("\(interfaces[i])", terminator: "")
    } else {
        print(":\(interfaces[i])", terminator: "")
    }
}
