#!/usr/bin/swift

import Foundation
import CoreWLAN

if let iface = CWWiFiClient.shared().interface() {
    print(iface.interfaceName!);
} else {
    print("Error : No current interface  (error=2,)")
    exit(1)
}
