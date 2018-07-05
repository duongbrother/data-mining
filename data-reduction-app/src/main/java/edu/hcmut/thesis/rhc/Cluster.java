package edu.hcmut.thesis.rhc;

import edu.hcmut.thesis.TimeSeries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Cluster {

    private TimeSeries centroid;

    private Set<TimeSeries> members = new LinkedHashSet<>();

    public Cluster(TimeSeries centroid, Collection<TimeSeries> members) {
        this.centroid = centroid;
        this.members = new LinkedHashSet<>(members);
    }

    public Cluster(TimeSeries centroid) {
        this(centroid, new LinkedHashSet<TimeSeries>());
    }

    public TimeSeries getCentroid() {
        return centroid;
    }

    public void setCentroid(TimeSeries centroid) {
        this.centroid = centroid;
    }

    public Set<TimeSeries> getMembers() {
        return members;
    }

    public void setMembers(Set<TimeSeries> members) {
        this.members = new LinkedHashSet<>(members);
        this.members.add(this.centroid);
    }

    public void addMember(TimeSeries member) {
        members.add(member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return Objects.equals(centroid, cluster.centroid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centroid);
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "centroid=" + centroid +
                ", members=" + members +
                '}';
    }
}
