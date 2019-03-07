## Services

### Abstract Cache Service

`com.icfolson.aem.library.core.services.cache.AbstractCacheService`

This class can be extended by implementations of `com.icfolson.aem.library.core.services.cache.CacheService` to expose cache stats and cache variables.

### Selective Replication Service

`com.icfolson.aem.library.core.services.replication.SelectiveReplicationService`

Service providing "selective" replication to activate/deactivate content to a subset of replication agents (rather than all agents, which is the AEM default behavior).

This service can be called directly but is also exposed by the corresponding Selective Replication Servlet as described on the [Servlets](https://github.com/icfnext/aem-library/wiki/servlets) page.