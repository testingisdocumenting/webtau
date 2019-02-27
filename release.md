# Setup

## Sonatype/Maven Central

There is some one time setup required in order to publish to Maven Central.  First, you will need a Sonatype account.

Once you have an account, you will need to add credentials for Sonatype to your `~/.m2/settings.xml`.  If you don't have
one then copy the one below.  If you have one already then create or modify the `servers` section to include the server
as shown below:

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

## GPG

You will need to also obtain GPG files to be copied into your `~/.gnupg` and the corresponding passphrase which you should
store in your own 1Password vault.


# Prepare release

The first step is to prepare the release.  Make sure you are on master and up to date then run:

```
mvn release:clean release:prepare -DskipTests
```

This will do a number of things (let's assume you're trying to release version x.y.z):
* build webtau
* prompt you for a few version related things where you should generally accept the proposed values
* update the version number in all poms to x.y.z
* git commit the change
* tag git as x.y.z
* update versions to x.y.(z+1)-SNAPSHOT
* git commit the change

# Perform the release

```
# set GPG_TTY to $(tty) prior
# have GPG keyphrase handy 
mvn release:perform
```

# Quick Notes

Github release will be automatically created on tagging performed by previous step.
Use output of a next command as a basis for notes. Update versions accordingly. 

```
git log --oneline --pretty=format:"%s" 0.14.0...0.17.0
```
