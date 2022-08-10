package fr.rushcubeland.commons;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Date;
import java.util.UUID;

public class AStats implements Cloneable {

    private UUID uuid;
    private long parcoursTimer;
    private Date firstConnection;
    private Date lastConnection;

    public AStats() {
    }

    public AStats(UUID uuid, long parcoursTimer, Date firstConnection, Date lastConnection) {
        this.uuid = uuid;
        this.parcoursTimer = parcoursTimer;
        this.firstConnection = firstConnection;
        this.lastConnection = lastConnection;
    }

    public String getParcoursTimerFormat(){
        return DurationFormatUtils.formatDurationHMS(this.parcoursTimer);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getParcoursTimer() {
        return parcoursTimer;
    }

    public void setParcoursTimer(long parcoursTimer) {
        this.parcoursTimer = parcoursTimer;
    }

    public Date getFirstConnection(){
        return this.firstConnection;
    }

    public Date getLastConnection(){
        return this.lastConnection;
    }

    public void setFirstConnection(Date firstConnection) {
        this.firstConnection = firstConnection;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    public AStats clone(){
        try {

            return (AStats) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
