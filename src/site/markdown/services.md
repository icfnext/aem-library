## Services

### Abstract Cache Service

`com.icfolson.aem.library.core.services.cache.AbstractCacheService`

This class can be extended by implementations of `com.icfolson.aem.library.core.services.cache.CacheService` to expose cache stats and cache variables.

### Abstract Sling Service

`com.icfolson.aem.library.core.services.AbstractSlingService`

The `com.icfolson.aem.library.services.AbstractSlingService` class can be used as a base for OSGi services that require an administrative `Session` and/or `ResourceResolver`.  Usage of this class does not imply any particular service type or implementation details but simply provides methods for acquiring and closing the underlying Sling and JCR administrative resources.

### Selective Replication Service

`com.icfolson.aem.library.core.services.replication.SelectiveReplicationService`

Service providing "selective" replication to activate/deactivate content to a subset of replication agents (rather than all agents, which is the AEM default behavior).

This service can be called directly but is also exposed by the corresponding Selective Replication Servlet as described on the [Servlets](https://github.com/Citytechinc/aem-library/wiki/servlets) page.