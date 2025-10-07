package com.deendayalproject.model.request

data class CCTVComplianceRequest(

val loginId: String,
val imeiNo: String,
val appVersion: String,
val centralMonitor: String,
val centralMonitorFile: String,
val cctvConformance: String,
val cctvConformanceFile: String,
val cctvStorage: String,
val cctvStorageFile: String,
val dvrStaticIp: String,
val dvrStaticIpFile: String,
val ipEnabled: String,
val resolution: String,
val videoStream: String,
val remoteAccessBrowser: String,
val simultaneousAccess: String,
val supportedProtocols: String,
val colorVideoAudio: String,
val storageFacility: String,
val tcId: String,
val sanctionOrder: String
)
