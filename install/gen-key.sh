#!/bin/bash

keytool -genkey -alias alias -keypass changeit -keystore test.keystore -storepass changeit

#keytool -genkey -keyalg RSA -alias selfsigned -keystore $1 -storepass testpassword -validity 360 -keysize 2048
#keytool -genkey -keyalg RSA -alias selfsigned -keystore $1 -storepass changeit -validity 360 -keysize 2048