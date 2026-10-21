#!/bin/sh
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

# ------------------------------------------------------------------
# Maven Wrapper Startup Script
# ------------------------------------------------------------------
# This script loads the maven-wrapper.jar and executes Maven with
# the provided arguments.
# ------------------------------------------------------------------

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
darwin=false
mingw=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true ;;
  MINGW*) mingw=true ;;
esac

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG="`dirname "$PRG"`/$link"
  fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Maven"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and MAVEN_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn() {
  echo "$*"
}

die() {
  echo
  echo "ERROR: $*"
  echo
  exit 1
}

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
darwin=false
mingw=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true ;;
  MINGW*) mingw=true ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched.
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# For Mingw, ensure paths are in Windows format before anything is touched.
if $mingw ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cmd //c "for %i in (\"$JAVA_HOME\") do @echo %~si"`
fi

# Attempt to find Java
if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`dirname \`dirname \$(readlink -f /usr/bin/java)\``
  elif [ -r /etc/fedora-release ] || [ -r /etc/redhat-release ] || [ -r /etc/centos-release ] ; then
    JAVA_HOME=/usr/lib/jvm/java
  elif [ -r /etc/SuSE-release ] ; then
    JAVA_HOME=/usr/lib/jvm/java
  elif [ -r /etc/debian_version ] ; then
    JAVA_HOME=/usr/lib/jvm/default-java
  elif [ -r /etc/arch-release ] ; then
    JAVA_HOME=/usr/lib/jvm/java-8-openjdk
  elif [ -r /etc/alpine-release ] ; then
    JAVA_HOME=/usr/lib/jvm/default-jvm
  fi
fi

# Check that Java is installed
if [ ! -x "$JAVA_HOME/bin/java" ] ; then
  die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME\n\nPlease set the JAVA_HOME variable in your environment to match the\nlocation of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
  MAX_FD_LIMIT=`ulimit -H -n`
  if [ $? -eq 0 ] ; then
    if [ "$MAX_FD_LIMIT" != "unlimited" ] ; then
      MAX_FD=$MAX_FD_LIMIT
    fi
  else
    warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
  fi
fi
if [ "$MAX_FD" != "unlimited" -a "$MAX_FD" != "max" ] ; then
  if ! `ulimit -n $MAX_FD 2>/dev/null` ; then
    warn "Could not set maximum file descriptors to $MAX_FD"
  fi
fi

# For Darwin, add the -Xdock:name option to the JVM options
if $darwin; then
  DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS -Xdock:name=$APP_NAME"
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --windows "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$APP_HOME" ] && APP_HOME=`cygpath --windows "$APP_HOME"`
  [ -n "$MAVEN_OPTS" ] && MAVEN_OPTS=`cygpath --path --windows "$MAVEN_OPTS"`
fi

# Build the command line
MAVEN_CMD="$JAVA_HOME/bin/java"
MAVEN_OPTS="${MAVEN_OPTS:-}"
MAVEN_DEBUG_OPTS="${MAVEN_DEBUG_OPTS:-}"

# Escape application args
# For Windows, we need to quote args containing spaces
WRAPPER_JAR="$APP_HOME/.mvn/wrapper/maven-wrapper.jar"
MAVEN_CMD_LINE_ARGS=("$@")
MAVEN_CMD_LINE="\"$MAVEN_CMD\" $DEFAULT_JVM_OPTS $MAVEN_OPTS $MAVEN_DEBUG_OPTS -classpath \"$WRAPPER_JAR\" org.apache.maven.wrapper.MavenWrapperMain ${1+\"\$@\"}"

# Execute Maven
exec "$MAVEN_CMD" $DEFAULT_JVM_OPTS $MAVEN_OPTS $MAVEN_DEBUG_OPTS -classpath "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"