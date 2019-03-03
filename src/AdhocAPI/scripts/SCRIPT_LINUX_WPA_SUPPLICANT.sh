#!/bin/bash

service network-manager stop
wpa_supplicant  -i $1 -c ../../adhocapis/wpa.conf