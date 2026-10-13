#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")/.."
export _JAVA_OPTIONS="-Xms64m -Xmx3g -XX:ActiveProcessorCount=1"
exec ./gradlew --no-daemon --no-configuration-cache --max-workers=1 test build
