package com.kylecorry.trail_sense.diagnostics.diagnostic2

import com.kylecorry.trail_sense.shared.database.Identifiable

enum class DiagnosticCode2(override val id: Long) : Identifiable {
    /**
     * There is no compass sensor
     */
    NoCompass(1),

    /**
     * The compass sensor is poor quality
     */
    PoorCompass(2),

    /**
     * There is no GPS
     */
    NoGPS(3),

    /**
     * The GPS is poor quality
     */
    PoorGPS(4),

    /**
     * The fine location permission is not granted
     */
    NoFineLocationPermission(5),

    /**
     * The background location permission is not granted
     */
    NoBackgroundLocationPermission(6),

    /**
     * The GPS is using a manual location
     */
    ManualLocation(7),

    /**
     * The GPS is using a manual location, but it is unset
     */
    UnsetManualLocation(8),

    /**
     * The GPS has timed out
     */
    TimedOutGPS(9),

    /**
     * The GPS exists, but is disabled
     */
    DisabledGPS(10),

    /**
     * There is no barometer sensor
     */
    NoBarometer(11),

    /**
     * The barometer sensor is poor quality
     */
    PoorBarometer(12),

    /**
     * The altimeter is using a manual elevation
     */
    ManualElevation(13),

    /**
     * There is no pedometer sensor
     */
    NoPedometer(14),

    /**
     * The activity recognition permission is not granted
     */
    NoActivityRecognitionPermission(15),

    /**
     * There is no gyroscope sensor
     */
    NoGyroscope(16),

    /**
     * The gyroscope sensor is poor quality
     */
    PoorGyroscope(17),

    /**
     * There is no camera
     */
    NoCamera(18),

    /**
     * The camera permission is not granted
     */
    NoCameraPermission(19),

    /**
     * The exact alarm permission is not granted
     */
    NoScheduleExactAlarmPermission(20),

    /**
     * The power saving mode is on
     */
    PowerSavingMode(21),

    /**
     * The battery usage is restricted
     */
    BatteryUsageRestricted(22),

    /**
     * The battery health is poor
     */
    BatteryHealthPoor(23),

    /**
     * There is no flashlight
     */
    NoFlashlight(24),

    /**
     * There is no light sensor
     */
    NoLightSensor(25),

    /**
     * All notifications are blocked
     */
    NotificationsBlocked(26),

    /**
     * The flashlight notification is blocked
     */
    FlashlightNotificationsBlocked(27),

    /**
     * The sunset notification is blocked
     */
    SunsetAlertsBlocked(28),

    /**
     * The storm notification is blocked
     */
    StormAlertsBlocked(29),

    /**
     * The daily forecast notification is blocked
     */
    DailyForecastNotificationsBlocked(30),

    /**
     * The pedometer notification is blocked
     */
    PedometerNotificationsBlocked(31),

    /**
     * The weather notification is blocked
     */
    WeatherNotificationsBlocked(32),

    /**
     * The astronomy notification is blocked
     */
    AstronomyAlertsBlocked(33),

    /**
     * The clock sync notification is blocked
     */
    ClockSyncNotificationBlocked(34),

    /**
     * The water boil notification is blocked
     */
    WaterBoilNotificationBlocked(35),

    /**
     * The white noise notification is blocked
     */
    WhiteNoiseNotificationBlocked(36),

    /**
     * The distance alert notification is blocked
     */
    DistanceAlertsBlocked(37),

    /**
     * The backtrack notification is blocked
     */
    BacktrackNotificationBlocked(38),

    /**
     * The weather monitor is disabled
     */
    WeatherMonitorDisabled(39),

    /**
     * The sunset alerts are disabled
     */
    SunsetAlertsDisabled(40),

    /**
     * The pedometer is disabled
     */
    PedometerDisabled(41),

    /**
     * Backtrack is disabled
     */
    BacktrackDisabled(42),

    /**
     * There is no accelerometer
     */
    NoAccelerometer(43),

    /**
     * The accelerometer is poor quality
     */
    PoorAccelerometer(44),
}