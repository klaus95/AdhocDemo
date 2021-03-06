#!/usr/bin/swift

import Foundation
import CoreWLAN

if let iface = CWWiFiClient.shared().interface() {
    iface.disassociate()
} else {
    print("Error: Interface does not exists (error=2,)")
    exit(1)
}
