#!/usr/bin/swift

import Foundation
import CoreWLAN

if CommandLine.arguments.count == 2 {
    let interface = CommandLine.arguments[1]
    
    var channels = Set<Int>()
    
    if let iface = CWWiFiClient.shared().interface(withName: interface) {
        for channel in iface.supportedWLANChannels()! {
            channels.insert(channel.channelNumber)
        }
    } else {
        print("Error: Interface does not exists  (error=2,)")
        exit(1)
    }
    
    let unique = Array(Set(channels).sorted())
    
    for i in 0..<unique.count {
        if i == 0 {
            print("\(unique[i])", terminator: "")
        } else {
            print(":\(unique[i])", terminator: "")
        }
    }
} else {
    print("Error: Not enough arguments to get all the channels  (error=3,)")
    exit(1)
}
