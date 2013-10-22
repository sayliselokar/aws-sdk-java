/*
 * Copyright 2011-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.annotation.NotThreadSafe;

/**
 * Used both as a base class and a minimal support of timing info.
 * <p>
 * In contrast to {@link TimingInfoFullSupport}, which is intended to be a full
 * support of the timing info, this class only provides a minimal support of
 * start and end time (ie with no-ops for sub-event measurements) for backward
 * compatiblity reasons.
 * <p>
 * This class is instantiated instead of {@link TimingInfoFullSupport} when
 * request metric collection is not required during a particular service
 * request/response cycle.
 */
@NotThreadSafe
public class TimingInfo {
    static final int UNKNOWN = -1;
    /**
     * The wall clock time (as the number of milliseconds since Epoch)
     * of when the timing measurement starts; or null if unknown.
     * This field is not meant to be used for timing measurement.
     * For more info, see:
     * https://blogs.oracle.com/dholmes/entry/inside_the_hotspot_vm_clocks
     */
    private final Long startEpochTimeMilli;
    /** 
     * Start time in nanosecond used for timing measurement.
     * Note the value in this field may have nothing to do with
     * the wall clock time.
     * The wall clock time of when the timing measurement starts
     * can optionally be captured in {@link #startEpochTimeMilli}.
     * For more info, see:
     * https://blogs.oracle.com/dholmes/entry/inside_the_hotspot_vm_clocks
     * <p>
     * Note System.nanoTime() can return negative values.
     */
    private final long startTimeNano;
    /** 
     * End time in nanosecond used for timing measurement or null if unknown.
     * Note the value in this field is only meant to be used for timing
     * measurement, and is not directly related to the wall clock time.
     * For more info, see:
     * https://blogs.oracle.com/dholmes/entry/inside_the_hotspot_vm_clocks
     * <p>
     * Note System.nanoTime() can return negative values.
     */
    private Long endTimeNano;

    /**
     * Captures the current wall clock time (since epoch in millisecond)
     * and the current time (in nanosecond) used for timing measurement.
     * For more info, see:
     * https://blogs.oracle.com/dholmes/entry/inside_the_hotspot_vm_clocks
     */
    public static TimingInfo startTiming() {
        return new TimingInfo(Long.valueOf(System.currentTimeMillis()), System.nanoTime(), null);
    }

    /**
     * A private ctor to facilitate the deprecation of using millisecond and
     * migration to using nanosecond for timing measurement.
     * 
     * @param startEpochTimeMilli start time since epoch in millisecond
     * @param startTimeNano start time in nanosecond
     * @param endTimeNano end time in nanosecond; or null if not known
     */
    protected TimingInfo(Long startEpochTimeMilli, long startTimeNano, Long endTimeNano) {
        this.startEpochTimeMilli = startEpochTimeMilli;
        this.startTimeNano = startTimeNano;
        this.endTimeNano = endTimeNano;
    }

    @Deprecated
    public final long getStartTime() {
        return isStartEpochTimeMilliKnown()
             ? startEpochTimeMilli
               // best effort even though technically this is incorrect
             : TimeUnit.NANOSECONDS.toMillis(startTimeNano)
             ;
    }

    @Deprecated
    public final long getStartEpochTimeMilli() {
        Long v = getStartEpochTimeMilliIfKnown();
        return v == null ? UNKNOWN : v.longValue();
    }

    public final Long getStartEpochTimeMilliIfKnown() {
        return startEpochTimeMilli;
    }

    public final long getStartTimeNano() {
        return startTimeNano;
    }

    @Deprecated
    public final long getEndTime() {
        return getEndEpochTimeMilli();
    }

    @Deprecated
    public final long getEndEpochTimeMilli() {
        Long v = getEndEpochTimeMilliIfKnown();
        return v == null ? UNKNOWN : v.longValue();
    }

    public final Long getEndEpochTimeMilliIfKnown() {
        return isStartEpochTimeMilliKnown() && isEndTimeKnown()
               // make use of the wall clock time and elpased time
             ? startEpochTimeMilli.longValue()
                 + TimeUnit.NANOSECONDS.toMillis(endTimeNano.longValue() - startTimeNano)
             : null;
    }

    public final long getEndTimeNano() {
        return endTimeNano == null ? UNKNOWN : endTimeNano;
    }

    public final Long getEndTimeNanoIfKnown() {
        return endTimeNano;
    }

    @Deprecated
    public final double getTimeTakenMillis() {
        Double v = getTimeTakenMillisIfKnown();
        return v == null ? UNKNOWN : v.doubleValue();
    }

    public final Double getTimeTakenMillisIfKnown() {
        if (isEndTimeKnown()) {
            double micros = (double)TimeUnit.NANOSECONDS.toMicros(endTimeNano - startTimeNano);
            return micros / 1000.0; // convert microseconds to milliseconds in double rather than long, preserving the precision
        }
        return null;
    }

    @Deprecated
    public final long getElapsedTimeMillis() {
        Double v = getTimeTakenMillisIfKnown();
        return v == null ? UNKNOWN : v.longValue();
    }

    public final boolean isEndTimeKnown() {
        return endTimeNano != null;
    }

    public final boolean isStartEpochTimeMilliKnown() {
        return startEpochTimeMilli != null;
    }

    public final String toString() {
        return String.valueOf(getTimeTakenMillis());
    }

    @Deprecated
    public final void setEndTime(long endTimeMilli) {
        this.endTimeNano = Long.valueOf(TimeUnit.MILLISECONDS.toNanos(endTimeMilli));
    }

    public final void setEndTimeNano(long endTimeNano) {
        this.endTimeNano = endTimeNano;
    }

    public final TimingInfo endTiming() {
        this.endTimeNano = Long.valueOf(System.nanoTime());
        return this;
    }

    public void addSubMeasurement(String subMeasurementName, TimingInfo timingInfo) {}
    public TimingInfo getSubMeasurement(String subMeasurementName) { return null; }
    public TimingInfo getSubMeasurement(String subMesurementName, int index) { return null; }
    public TimingInfo getLastSubMeasurement(String subMeasurementName) { return null; }
    public List<TimingInfo> getAllSubMeasurements(String subMeasurementName) { return null; }
    public Map<String, List<TimingInfo>> getSubMeasurementsByName() { return null; }
    public Number getCounter(String key) { return null; }
    public Map<String, Number> getAllCounters() { return Collections.emptyMap(); }
    public void setCounter(String key, long count) {}
    public void incrementCounter(String key) {}
}