@echo off
SET CWD=%~dp0
java -Djava.system.class.loader=groovy.lang.GroovyClassLoader -Dgroovy.grape.report.downloads=true -cp "%CWD%\\lib\\*;%WEBTAU_CP%;:%CWD%\\libs\\*" org.testingisdocumenting.webtau.cli.WebTauCliApp %*
