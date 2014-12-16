This may seem odd, but this META-INF/spring folder contains artifacts specifically for testing Roo code.

Since the Roo code is generated automatically by Roo we can't change it, so it's looking for spring config files in META-INF/spring.

Therefore this folder contains mostly mocked out spring configuration.  There is an alternative folder META-INF/spring-test which contains testing artifacts for non-roo classes.