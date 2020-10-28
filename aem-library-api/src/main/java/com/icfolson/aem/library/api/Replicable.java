package com.icfolson.aem.library.api;

import com.day.cq.replication.ReplicationStatus;

/**
 * A replicable resource (page or asset).
 */
public interface Replicable {

    /**
     * Get the replication status of this resource.
     *
     * @return replication status
     */
    ReplicationStatus getReplicationStatus();
}
