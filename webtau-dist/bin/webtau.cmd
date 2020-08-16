@echo off
SET CWD=%~dp0
java -Djava.system.class.loader=groovy.lang.GroovyClassLoader -cp "%CWD%\\lib\\*;%WEBTAU_CP%" org.testingisdocumenting.webtau.cli.WebTauCliApp %*
