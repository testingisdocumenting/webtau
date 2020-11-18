package scenarios.db

import groovy.grape.Grape

Grape.grab(group:'com.h2database', module: 'h2', version: '1.4.200', // auto download DB driver dependency (in this case H2 db)
        classLoader: ClassLoader.getSystemClassLoader())

dbUrl = "jdbc:h2:file:~/customerdb/h2-db;AUTO_SERVER=TRUE" // primary DB JDBC connection url
dbDriverClassName = "org.h2.Driver" // JDBC driver class name
dbUserName = "sa"
dbPassword = "password"
