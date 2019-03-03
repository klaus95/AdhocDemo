#!/bin/bash

ip addr | awk '/state UP/ {print $2}'