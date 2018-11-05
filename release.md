# Setup

in `settings.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>ossrh</id>
            <username>name</username>
            <password>password</password>
        </server>
    </servers>
</settings>
```

# Release Steps

```
# set GPG_TTY to $(tty) prior
# have GPG keyphrase handy 
mvn release:clean release:prepare -DskipTests
mvn release:perfrom
```

# Quick Notes

Github release will be automatically created on tagging performed by previous step.
Use output of a next command as a basis for notes. Update versions accordingly. 

```
git log --oneline --pretty=format:"%s" 0.14.0...0.17.0
```