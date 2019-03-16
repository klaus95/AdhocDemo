#!/bin/bash

service network-manager stop
sudo ifconfig $1 down
sudo iwconfig $1 mode ad-hoc
sudo iwconfig $1 channel $2
echo "passed setting mode and channel"
if [ $5 = restricted ] 
then
    echo "setting pass and restricted mode"
    sudo iwconfig $1 key $5 $4
else
    echo "not setting restricted mode"
    sudo iwconfig $1 key $4
fi
sudo iwconfig $1 essid $3
sudo ifconfig $1 up
sudo ip addr add 169.254.93.54/8 broadcast 10.255.255.255 dev $1

