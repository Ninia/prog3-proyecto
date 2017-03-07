#!/usr/bin/env bash
sensors | grep -oP 'Physical.*?\+\K[0-9.]+'
