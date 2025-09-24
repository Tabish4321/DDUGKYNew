package com.deendayalproject.model.response

data class IpEnableRes(
    val wrappedList: List<CctvItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class CctvItem(
    val supportedProtocol: String?,
    val dvrStaticIp: String?,
    val ipEnable: String?,
    val colorVideoAudit: String?,
    val cctvStorageImagePath: String?,
    val centralMonitorImagePath: String?,
    val dvrStaticIpImagePath: String?,
    val resolution: String?,
    val cctvConformance: String?,
    val cctvConformanceImagePath: String?,
    val centralMonitor: String?,
    val videoStream: String?,
    val remoteAccessBrowser: String?,
    val cctvStorage: String?,
    val simultaneousAccess: String?,
    val storageFacility: String?
)
