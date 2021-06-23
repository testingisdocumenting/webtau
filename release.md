# Setup

## Sonatype/Maven Central

There is some one time setup required in order to publish to Maven Central.  First, you will need a Sonatype account which
you can create at https://issues.sonatype.org/secure/Signup!default.jspa.  You will also need permissions to publish to
the `org.testingisdocumenting.webtau` group ID, for that raise a similar JIRA to https://issues.sonatype.org/browse/OSSRH-41183.

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

You will need to also obtain GPG files to be copied into your `~/.gnupg` and the corresponding passphrase.


# Prepare release

The first step is to prepare the release.  Make sure you are on master and up to date then run:

```
mvn release:clean release:prepare
```

This will do a number of things (let's assume you're trying to release version x.y.z):
* build webtau
* prompt you for a few version related things where you should generally accept the proposed values by pressing enter with no input
* update the version number in all poms to x.y.z
* git commit and push the change
* tag git as x.y.z
* update versions to x.y.(z+1)-SNAPSHOT
* git commit and push the change

Please note some of these git operations are performed against master, you therefore need to be an admin on the project.

# Perform the release

```
export GPG_TTY=$(tty) mvn release:perform
```

This will prompt you for the GPG passphrase.  It will then build and test webtau and publish all artifacts to Maven Central.

# Sit back, relax and enjoy the wait

At this stage, **be patient**.  There is some delay between completion of the `release:perform` step and the artifacts being
available in Maven Central.  This will be at least an hour.  You can keep refreshing https://search.maven.org/search?q=g:org.testingisdocumenting.webtau%20AND%20a:webtau&core=gav
until you see your version.
