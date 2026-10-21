@REM
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM   http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.

@REM ------------------------------------------------------------------
@REM Maven Wrapper Startup Script for Windows
@REM ------------------------------------------------------------------

@if "%DEBUG%" == "" @echo off
@rem set title of command window
title %APP_NAME% %APP_BASE_NAME%

@rem Add default JVM options here. You can also use JAVA_OPTS and MAVEN_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto fail

:findJavaFromJavaHome
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\.mvn\wrapper\maven-wrapper.jar

set MAVEN_CMD="%JAVA_EXE%" %DEFAULT_JVM_OPTS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath "%CLASSPATH%" org.apache.maven.wrapper.MavenWrapperMain %*

%MAVEN_CMD%

:end
@endlocal & set ERRORLEVEL=%ERRORLEVEL%
goto :EOF

:fail
endlocal & set ERRORLEVEL=1
goto :EOF